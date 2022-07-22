package com.undsf.mcmodmgr.curseforge.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class FileHash(
        @JsonProperty
        var value: String,

        /**
         * 哈希算法
         *
         * 1 = SHA1，2 = MD5
         */
        @JsonProperty
        var algo: Int
) {
    override fun toString(): String {
        var algoName = "Unknown"
        when (algo) {
            1 -> algoName = "SHA1"
            2 -> algoName = "MD5"
        }
        return "$algoName $value"
    }
}