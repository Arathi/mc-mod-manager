package com.undsf.mcmodmgr.curseforge

import com.undsf.mcmodmgr.curseforge.requests.SearchModsCondition
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Paths

@SpringBootTest
class CurseForgeApiClientTests {
    @Autowired
    lateinit var client: CurseForgeApiClient

    @Test
    fun testGetMod() {
        val mod = client.getMod(245755)
        println(mod)
    }

    @Test
    fun testSearchMods() {
        val conditions = SearchModsCondition(
                searchFilter = "rei"
        )
        var mods = client.searchMods(conditions, pageSize = 10)
        println(mods)
    }

    @Test
    fun testDownload() {
        val path = Paths.get("E:\\Tmp\\mmm\\files\\1\\2\\waystones-forge-1.19-11.0.0.jar")
        val url = "https://edge.forgecdn.net/files/3835/119/waystones-forge-1.19-11.0.0.jar"
        client.download(path, url)

    }

    @Test
    fun testDownloadMod() {
        val downloadBytes = client.downloadMod(245755, 3830849)
        println("下载完成${downloadBytes}字节")
    }
}