package com.undsf.mcmodmgr.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

open class DataResponse<D> constructor(
        code: Int = 0,
        message: String? = "成功",
        data: D? = null) {
    @JsonProperty
    var code: Int = code

    @JsonProperty
    var message: String? = message

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var data: D? = data
}