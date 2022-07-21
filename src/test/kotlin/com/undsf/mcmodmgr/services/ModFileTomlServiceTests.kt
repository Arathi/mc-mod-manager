package com.undsf.mcmodmgr.services

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.util.jar.JarFile

private val log = KotlinLogging.logger {}

@SpringBootTest
class ModFileTomlServiceTests {
    @Autowired
    lateinit var modFileTomlSvc: ModFileTomlService

    @Test
    fun testScanModFiles() {
        val dir = File("D:\\Temp\\forge-mods")
        val mfts = modFileTomlSvc.scanModFiles(dir)
        for (mft in mfts) {
            print(mft.toTree())
        }
    }

    @Test
    fun testParse() {
        val jar = JarFile(File("D:\\Temp\\forge-mods\\Jade-1.19-forge-7.5.0.jar"))
        val mft = modFileTomlSvc.parse(jar)
        if (mft != null) {
            print(mft.toTree())
        }
        else {
            log.warn("{}解析失败", jar.name)
        }
    }
}