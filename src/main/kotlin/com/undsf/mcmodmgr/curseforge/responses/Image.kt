package com.undsf.mcmodmgr.curseforge.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class Image(
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
        val url: String?
) {}