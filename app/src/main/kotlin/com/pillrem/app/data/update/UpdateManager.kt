package com.pillrem.app.data.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.pillrem.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class UpdateManager(private val context: Context) {

    private val gson = Gson()
    private val repoOwner = "justcooking"
    private val repoName = "PillRem"
    private val latestReleaseApiUrl = "https://api.github.com/repos/$repoOwner/$repoName/releases/latest"

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()

    suspend fun checkForUpdates(silent: Boolean = false) = withContext(Dispatchers.IO) {
        if (!silent) {
            _updateState.value = UpdateState.Checking
        }

        try {
            val connection = openConnectionWithRedirects(latestReleaseApiUrl)
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
            connection.setRequestProperty("User-Agent", "PillRem-Android/${BuildConfig.VERSION_NAME}")
            connection.connectTimeout = 10000
            connection.readTimeout = 15000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // No releases published yet on this repository
                if (!silent) {
                    _updateState.value = UpdateState.UpToDate(BuildConfig.VERSION_NAME)
                }
                return@withContext
            }

            if (responseCode != HttpURLConnection.HTTP_OK) {
                if (!silent) {
                    _updateState.value = UpdateState.Error("GitHub responded with status $responseCode")
                }
                return@withContext
            }

            val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
            val release = gson.fromJson(jsonString, GithubRelease::class.java)

            if (release?.tagName == null) {
                if (!silent) {
                    _updateState.value = UpdateState.UpToDate(BuildConfig.VERSION_NAME)
                }
                return@withContext
            }

            val isNewer = isNewerVersion(remoteTag = release.tagName, localVersionName = BuildConfig.VERSION_NAME)
            if (isNewer) {
                val apkAsset = release.assets.firstOrNull { it.name.endsWith(".apk", ignoreCase = true) }
                val downloadUrl = apkAsset?.downloadUrl ?: release.htmlUrl ?: "https://github.com/$repoOwner/$repoName/releases/latest"
                val apkSize = apkAsset?.size ?: 0L

                val info = ReleaseInfo(
                    tagName = release.tagName,
                    title = release.name ?: "Release ${release.tagName}",
                    changelog = release.body ?: "No changelog provided.",
                    downloadUrl = downloadUrl,
                    apkSize = apkSize,
                    htmlUrl = release.htmlUrl ?: "https://github.com/$repoOwner/$repoName"
                )
                _updateState.value = UpdateState.UpdateAvailable(info)
            } else {
                if (!silent) {
                    _updateState.value = UpdateState.UpToDate(BuildConfig.VERSION_NAME)
                }
            }
        } catch (e: Exception) {
            if (!silent) {
                _updateState.value = UpdateState.Error(
                    if (e is java.net.UnknownHostException) "No internet connection to check for updates."
                    else "Check failed: ${e.localizedMessage ?: "Unknown network error"}"
                )
            }
        }
    }

    suspend fun downloadAndPrepareUpdate(releaseInfo: ReleaseInfo) = withContext(Dispatchers.IO) {
        val updatesDir = File(context.cacheDir, "updates")
        if (!updatesDir.exists()) {
            updatesDir.mkdirs()
        }

        val apkFile = File(updatesDir, "PillRem-${releaseInfo.tagName}.apk")
        if (apkFile.exists()) {
            apkFile.delete()
        }

        try {
            _updateState.value = UpdateState.Downloading(
                progressPercent = 0,
                bytesDownloaded = 0,
                totalBytes = releaseInfo.apkSize
            )

            val connection = openConnectionWithRedirects(releaseInfo.downloadUrl)
            connection.setRequestProperty("User-Agent", "PillRem-Android/${BuildConfig.VERSION_NAME}")
            connection.connectTimeout = 15000
            connection.readTimeout = 30000

            val totalBytes = if (connection.contentLengthLong > 0) connection.contentLengthLong else releaseInfo.apkSize

            connection.inputStream.use { input ->
                FileOutputStream(apkFile).use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    var totalRead: Long = 0
                    var lastReportedPercent = -1

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalRead += bytesRead

                        val percent = if (totalBytes > 0) ((totalRead * 100) / totalBytes).toInt() else 0
                        if (percent != lastReportedPercent) {
                            lastReportedPercent = percent
                            _updateState.value = UpdateState.Downloading(
                                progressPercent = percent,
                                bytesDownloaded = totalRead,
                                totalBytes = totalBytes
                            )
                        }
                    }
                }
            }

            _updateState.value = UpdateState.ReadyToInstall(
                releaseInfo = releaseInfo,
                apkPath = apkFile.absolutePath
            )
        } catch (e: Exception) {
            if (apkFile.exists()) {
                apkFile.delete()
            }
            _updateState.value = UpdateState.Error("Download failed: ${e.localizedMessage ?: "Network error"}")
        }
    }

    fun launchPackageInstaller(apkPath: String) {
        val apkFile = File(apkPath)
        if (!apkFile.exists()) {
            _updateState.value = UpdateState.Error("Downloaded APK file not found.")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!context.packageManager.canRequestPackageInstalls()) {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                return
            }
        }

        try {
            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(installIntent)
        } catch (e: Exception) {
            _updateState.value = UpdateState.Error("Unable to launch installer: ${e.localizedMessage}")
        }
    }

    fun resetState() {
        _updateState.value = UpdateState.Idle
    }

    private fun openConnectionWithRedirects(initialUrl: String): HttpURLConnection {
        var url = initialUrl
        var connection: HttpURLConnection
        var redirects = 0
        while (redirects < 5) {
            connection = URL(url).openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = true
            val status = connection.responseCode
            if (status == HttpURLConnection.HTTP_MOVED_TEMP ||
                status == HttpURLConnection.HTTP_MOVED_PERM ||
                status == 307 || status == 308
            ) {
                val newUrl = connection.getHeaderField("Location") ?: break
                connection.disconnect()
                url = newUrl
                redirects++
            } else {
                return connection
            }
        }
        return URL(url).openConnection() as HttpURLConnection
    }

    companion object {
        fun isNewerVersion(remoteTag: String, localVersionName: String): Boolean {
            val remoteClean = remoteTag.removePrefix("v").trim()
            val localClean = localVersionName.removePrefix("v").trim()
            val remoteParts = remoteClean.split(".").mapNotNull { it.toIntOrNull() }
            val localParts = localClean.split(".").mapNotNull { it.toIntOrNull() }
            val maxLen = maxOf(remoteParts.size, localParts.size)
            for (i in 0 until maxLen) {
                val r = remoteParts.getOrElse(i) { 0 }
                val l = localParts.getOrElse(i) { 0 }
                if (r > l) return true
                if (r < l) return false
            }
            return false
        }
    }
}
