package com.undsf.mcmodmgr.models

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class VersionInterval constructor(
        containMin: Boolean,
        versionMin: SemanticVersion?,
        versionMax: SemanticVersion?,
        containMax: Boolean
) {
    var containMin: Boolean
    var versionMin: SemanticVersion
    var versionMax: SemanticVersion
    var containMax: Boolean

    init {
        this.containMin = containMin
        this.versionMin = versionMin ?: SemanticVersion.MIN
        this.versionMax = versionMax ?: SemanticVersion.MAX
        this.containMax = containMax
    }

    fun containVersion(version: SemanticVersion) : Boolean {
        val checkMin = if (containMin) version >= versionMin else version > versionMin
        val checkMax = if (containMax) version <= versionMax else version < versionMax
        return checkMin && checkMax
    }

    companion object{
        final val PATTERN = Regex(pattern = "[\\[\\(](.*?)?,(.*?)?[\\]\\)]")

        fun parse(viStr: String) : VersionInterval? {
            val result = PATTERN.find(viStr)
            if (result == null) {
                log.warn { "无效的版本号区间：$viStr" }
                return null
            }

            return VersionInterval(
                    viStr.first() == '[',
                    SemanticVersion.parse(result.groupValues[1]),
                    SemanticVersion.parse(result.groupValues[2]),
                    viStr.last() == ']'
            )
        }
    }
}