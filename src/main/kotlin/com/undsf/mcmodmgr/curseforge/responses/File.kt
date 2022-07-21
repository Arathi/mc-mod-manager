package com.undsf.mcmodmgr.curseforge.responses

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter

data class File(
        var id: Int
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
}