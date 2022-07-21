package com.undsf.mcmodmgr.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Mod(
        modId: String,
        version: String,
) {
    @JsonProperty
    val modId: String = modId

    @JsonProperty
    val version: String = version

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val dependencies: MutableMap<String, ModDependency> = LinkedHashMap()

    fun addDependency(dep: ModDependency) {
        dependencies[dep.modId] = dep
    }

    override fun toString(): String {
        return "$modId $version"
    }
}