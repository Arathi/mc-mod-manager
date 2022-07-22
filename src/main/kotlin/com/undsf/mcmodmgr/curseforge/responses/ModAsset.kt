package com.undsf.mcmodmgr.curseforge.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class ModAsset(
        @JsonProperty
        val id: Int?,

        @JsonProperty
        val modId: Int?,

        @JsonProperty
        val title: String?,

        @JsonProperty
        val description: String?,

        @JsonProperty
        val thumbnailUrl: String?,

        @JsonProperty
        val url: String
) {
    override fun toString(): String {
        return "$modId/$id: $url"
    }
}