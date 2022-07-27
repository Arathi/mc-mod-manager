package com.undsf.mcmodmgr.curseforge.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class ModLoaderType {
    Any,
    Forge,
    Cauldron,
    LiteLoader,
    Fabric,
    Quilt;

    @JsonValue
    fun toOrdinal() : Int {
        return ordinal
    }
}