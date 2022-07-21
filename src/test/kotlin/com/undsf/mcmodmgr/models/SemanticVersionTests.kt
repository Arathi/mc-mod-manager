package com.undsf.mcmodmgr.models

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

import org.junit.jupiter.api.Assertions.*

@SpringBootTest
class SemanticVersionTests {
    @Test
    fun testParse() {
        val v1 = SemanticVersion.parse("1")
        val v1_2 = SemanticVersion.parse("1.2")
        val v1_2_0 = SemanticVersion.parse("1.2.0")
        val v1_2_3 = SemanticVersion.parse("1.2.3")
        var v2_1_5_beta = SemanticVersion.parse("2.1.5-beta")
        var v2_2_2_build3 = SemanticVersion.parse("2.2.2+build3")
        var v2_3_5_pre3_build5 = SemanticVersion.parse("2.3.5-pre3+build5")

        assertEquals("1.0.0", v1.toString(), "版本号自动补充出现问题")
        assertTrue(v1_2 == v1_2_0, "版本号相等检查出现问题")

        assertNotNull(v1)
        assertNotNull(v1_2_3)
        if (v1 != null && v1_2_3 != null) {
            assertTrue(v1_2_3 > v1, "版本号大小比较出现问题")
        }

        assertEquals("", v1_2?.preRelease, "获取空预发布版本号出现问题")
        assertEquals("beta", v2_1_5_beta?.preRelease, "获取非空预发布版本号出现问题")
        assertEquals("", v1_2_3?.build, "获取空构建版本号出现问题")
        assertEquals("build3", v2_2_2_build3?.build, "获取空构建版本号出现问题")

        println(v1)
        println(v1_2)
        println(v1_2_0)
        println(v1_2_3)
        println(v2_1_5_beta)
        println(v2_2_2_build3)
        println(v2_3_5_pre3_build5)
    }
}