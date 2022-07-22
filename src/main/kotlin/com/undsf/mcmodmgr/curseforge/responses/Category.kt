package com.undsf.mcmodmgr.curseforge.responses

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import java.time.LocalDateTime

data class Category(
        var id: Int,
        var gameId: Int,
        var name: String,
        var slug: String?,
        var url: String,
        var iconUrl: String,
        var dateModified: LocalDateTime,
        var isClass: Boolean,
        var classId: Int,
        var parentCategoryId: Int
) {
    var additionParameters: MutableMap<String, Any?> = LinkedHashMap()

    @JsonAnyGetter
    fun jsonAnyGetter() : MutableMap<String, Any?> {
        return additionParameters
    }

    @JsonAnySetter
    fun jsonAnySetter(key: String, value: Any?) {
        additionParameters[key] = value
    }

    override fun toString(): String {
        return "$id: $name"
    }
}