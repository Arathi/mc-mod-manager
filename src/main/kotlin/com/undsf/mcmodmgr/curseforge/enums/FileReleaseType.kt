package com.undsf.mcmodmgr.curseforge.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class FileReleaseType(var description: String) {
    Unknown("未知"),
    Release("正式"),
    Beta("公测"),
    Alpha("内测");

    @JsonValue
    fun toOrdinal(): Int {
        return this.ordinal
    }
}