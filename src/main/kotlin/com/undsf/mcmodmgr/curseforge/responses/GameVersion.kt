package com.undsf.mcmodmgr.curseforge.responses

import java.time.LocalDateTime

data class GameVersion(
        var gameVersionName: String,
        var gameVersionPadded: String,
        var gameVersion: String,
        var gameVersionReleaseDate: LocalDateTime,
        var gameVersionTypeId: Int
)