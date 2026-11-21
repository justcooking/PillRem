package com.pillrem.app.data.update

import com.google.gson.annotations.SerializedName

data class GithubRelease(
    @SerializedName("tag_name") val tagName: String,
    @SerializedName("name") val name: String?,
    @SerializedName("body") val body: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("assets") val assets: List<GithubAsset> = emptyList()
)

data class GithubAsset(
    @SerializedName("name") val name: String,
    @SerializedName("size") val size: Long,
    @SerializedName("browser_download_url") val downloadUrl: String
)

data class ReleaseInfo(
    val tagName: String,
    val title: String,
    val changelog: String,
    val downloadUrl: String,
    val apkSize: Long,
    val htmlUrl: String
)

sealed class UpdateState {
    data object Idle : UpdateState()
    data object Checking : UpdateState()
    data class UpdateAvailable(val releaseInfo: ReleaseInfo) : UpdateState()
    data class UpToDate(val currentVersion: String) : UpdateState()
    data class Downloading(val progressPercent: Int, val bytesDownloaded: Long, val totalBytes: Long) : UpdateState()
    data class ReadyToInstall(val releaseInfo: ReleaseInfo, val apkPath: String) : UpdateState()
    data class Error(val message: String) : UpdateState()
}
