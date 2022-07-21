package com.undsf.mcmodmgr.curseforge

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CurseForgeApiClientTests {
    @Autowired
    lateinit var client: CurseForgeApiClient

    @Test
    fun testGet() {

    }
}