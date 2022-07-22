package com.undsf.mcmodmgr.commons

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

/**
 * 语义化版本
 */
class SemanticVersion constructor(
        major: Int,
        minor: Int,
        patch: Int,
        preRelease: String,
        build: String
) : Comparable<SemanticVersion> {
    var major: Int
    var minor: Int
    var patch: Int
    var preRelease: String
    var build: String

    init {
        this.major = major
        this.minor = minor
        this.patch = patch
        this.preRelease = preRelease
        this.build = build
    }

    companion object {
        val VERSION_REGEX = Regex(pattern = "^(0|[1-9]\\d*)(\\.(0|[1-9]\\d*))?(\\.(0|[1-9]\\d*))?(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$")

        val MIN = SemanticVersion(0, 0, 0, "", "")
        val MAX = SemanticVersion(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, "", "")

        fun parse(verStr: String): SemanticVersion? {
            val result = VERSION_REGEX.find(verStr)
            if (result == null) {
                log.warn { "无法解析版本号：${verStr}" }
                return null
            }

            var major = if (result.groupValues[1].isNotEmpty()) result.groupValues[1].toInt() else 0
            var minor = if (result.groupValues[3].isNotEmpty()) result.groupValues[3].toInt() else 0
            var patch = if (result.groupValues[5].isNotEmpty()) result.groupValues[5].toInt() else 0
            var preRelease: String = result.groupValues[6]
            var build: String = result.groupValues[7]
            return SemanticVersion(major, minor, patch, preRelease, build)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SemanticVersion) {
            return false
        }
        return compareTo(other) == 0
    }

    override fun compareTo(other: SemanticVersion): Int {
        var delta = major - other.major
        if (delta != 0) return delta

        delta = minor - other.minor
        if (delta != 0) return delta

        delta = patch - other.patch
        if (delta != 0) return delta

        delta = preRelease.compareTo(other.preRelease)
        if (delta != 0) return delta

        return build.compareTo(other.build)
    }

    override fun toString() : String {
        var version = "$major.$minor.$patch"
        if (preRelease != null && preRelease!!.isNotEmpty()) {
            version += "-$preRelease"
        }
        if (build != null && build!!.isNotEmpty()) {
            version += "-$build"
        }
        return version
    }
}