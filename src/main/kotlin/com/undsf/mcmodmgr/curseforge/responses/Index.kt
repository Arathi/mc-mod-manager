package com.undsf.mcmodmgr.curseforge.responses

data class Index(
        var gameVersion: String,
        var fileId: Int,
        var filename: String,
        var releaseType: Int,
        var gameVersionTypeId: Int,
        var modLoader: Int
) {}