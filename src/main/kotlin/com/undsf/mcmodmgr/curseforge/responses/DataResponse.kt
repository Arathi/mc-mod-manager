package com.undsf.mcmodmgr.curseforge.responses

import com.fasterxml.jackson.annotation.JsonProperty

open class DataResponse<D>(
        @JsonProperty
        var data: D){
}