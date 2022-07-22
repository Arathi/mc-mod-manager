package com.undsf.mcmodmgr.curseforge.responses

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Mod(
        @JsonProperty
        var id: Int,

        @JsonProperty
        var gameId: Int,

        @JsonProperty
        var name: String,

        @JsonProperty
        var slug: String,

        @JsonProperty
        var links: MutableMap<String, String>,

        @JsonProperty
        var summary: String,

        @JsonProperty
        var status: Int,

        @JsonProperty
        var downloadCount: Long,

        @JsonProperty
        var isFeatured: Boolean,

        @JsonProperty
        var primaryCategoryId: Int,

        @JsonProperty
        var categories: List<Category>,

        @JsonProperty
        var classId: Int,

        @JsonProperty
        var authors: List<Author>,

        @JsonProperty
        var logo: ModAsset,

        @JsonProperty
        var screenshots: List<ModAsset>,

        @JsonProperty
        var mainFileId: Int,

        @JsonProperty
        var latestFiles: List<File>,

        @JsonProperty
        var latestFilesIndexes: List<FileIndex>,

        @JsonProperty
        var dateCreated: LocalDateTime,

        @JsonProperty
        var dateModified: LocalDateTime,

        @JsonProperty
        var dateReleased: LocalDateTime,

        @JsonProperty
        var allowModDistribution: Boolean,

        @JsonProperty
        var gamePopularityRank: Int,

        @JsonProperty
        var isAvailable: Boolean,

        @JsonProperty
        var thumbsUpCount: Int
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
        return "$id: $name "
    }
}