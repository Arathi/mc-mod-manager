package com.undsf.mcmodmgr.curseforge.responses

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.undsf.mcmodmgr.curseforge.enums.FileReleaseType
import java.time.LocalDateTime

data class File(
        @JsonProperty
        var id: Int,

        @JsonProperty
        var gameId: Int,

        @JsonProperty
        var modId: Int,

        @JsonProperty
        var isAvailable: Boolean,

        @JsonProperty
        var displayName: String,

        @JsonProperty
        var fileName: String,

        @JsonProperty
        var releaseType: FileReleaseType,

        @JsonProperty
        var fileStatus: Int,

        @JsonProperty
        var hashes: List<FileHash>,

        @JsonProperty
        var fileDate: LocalDateTime,

        @JsonProperty
        var fileLength: Int,

        @JsonProperty
        var downloadCount: Int,

        @JsonProperty
        var downloadUrl: String?,

        @JsonProperty
        var gameVersions: List<String>,

        @JsonProperty
        var sortableGameVersions: List<GameVersion>,

        @JsonProperty
        var dependencies: List<FileDependency>,

        @JsonProperty
        var alternateFileId: Int,

        @JsonProperty
        var isServerPack: Boolean,

        @JsonProperty
        var fileFingerprint: Long,

        @JsonProperty
        var modules: List<FileModule>,
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
        var gameVersion = gameVersions.first()
        return "$id: [${releaseType.description}] $gameVersion $fileName $fileDate"
    }
}