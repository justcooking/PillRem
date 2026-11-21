package com.pillrem.app

import com.google.gson.Gson
import com.pillrem.app.data.update.GithubRelease
import com.pillrem.app.data.update.UpdateManager
import org.junit.Assert.*
import org.junit.Test

class UpdateManagerTest {

    @Test
    fun testVersionComparison() {
        // Newer patch version
        assertTrue(UpdateManager.isNewerVersion("v1.1.1", "1.1.0"))
        assertTrue(UpdateManager.isNewerVersion("1.1.1", "1.1.0"))

        // Newer minor version
        assertTrue(UpdateManager.isNewerVersion("v1.2.0", "1.1.0"))
        assertTrue(UpdateManager.isNewerVersion("1.2", "1.1.0"))

        // Newer major version
        assertTrue(UpdateManager.isNewerVersion("v2.0.0", "1.1.0"))

        // Same version
        assertFalse(UpdateManager.isNewerVersion("v1.1.0", "1.1.0"))
        assertFalse(UpdateManager.isNewerVersion("1.1.0", "1.1.0"))

        // Older version
        assertFalse(UpdateManager.isNewerVersion("v1.0.9", "1.1.0"))
        assertFalse(UpdateManager.isNewerVersion("0.9.0", "1.1.0"))

        // Multi-level version
        assertTrue(UpdateManager.isNewerVersion("1.1.0.1", "1.1.0"))
        assertFalse(UpdateManager.isNewerVersion("1.1.0", "1.1.0.1"))
    }

    @Test
    fun testGithubReleaseJsonParsing() {
        val sampleJson = """
            {
              "tag_name": "v1.2.0",
              "name": "PillRem v1.2.0 - Critical Clinical Update",
              "body": "### Changes\n* Added stock refill alerts\n* Improved alarm reliability",
              "html_url": "https://github.com/justcooking/PillRem/releases/tag/v1.2.0",
              "published_at": "2026-11-21T12:00:00Z",
              "assets": [
                {
                  "name": "PillRem-v1.2.0.apk",
                  "size": 11223344,
                  "browser_download_url": "https://github.com/justcooking/PillRem/releases/download/v1.2.0/PillRem-v1.2.0.apk"
                }
              ]
            }
        """.trimIndent()

        val gson = Gson()
        val release = gson.fromJson(sampleJson, GithubRelease::class.java)

        assertNotNull(release)
        assertEquals("v1.2.0", release.tagName)
        assertEquals("PillRem v1.2.0 - Critical Clinical Update", release.name)
        assertEquals(1, release.assets.size)
        assertEquals("PillRem-v1.2.0.apk", release.assets[0].name)
        assertEquals(11223344L, release.assets[0].size)
        assertEquals(
            "https://github.com/justcooking/PillRem/releases/download/v1.2.0/PillRem-v1.2.0.apk",
            release.assets[0].downloadUrl
        )
    }
}
