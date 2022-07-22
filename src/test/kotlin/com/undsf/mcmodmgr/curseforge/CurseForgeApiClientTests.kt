package com.undsf.mcmodmgr.curseforge

import com.undsf.mcmodmgr.curseforge.requests.SearchModsCondition
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

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
}