package com.undsf.mcmodmgr.curseforge.responses

data class Author(
        var id: Int,
        var name: String,
        var url: String
) {
    override fun toString(): String {
        return "$id: $name ($url)"
    }
}