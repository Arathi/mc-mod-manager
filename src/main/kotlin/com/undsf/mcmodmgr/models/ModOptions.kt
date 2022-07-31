package com.undsf.mcmodmgr.models

import com.fasterxml.jackson.annotation.JsonProperty

data class ModOptions(
        /**
         * CurseForge的modId
         */
        var curseForgeId: Int? = null,

        /**
         * CurseForge的slug
         */
        var slug: String? = null,

        /**
         * 指定版本号
         */
        @JsonProperty
        var version: String? = null,

        /**
         * 指定文件ID
         */
        @JsonProperty
        var fileId: Int? = null,

        /**
         * 别名
         */
        @JsonProperty
        var alias: String? = null
)
