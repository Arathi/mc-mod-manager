package com.undsf.mcmodmgr.curseforge

import com.undsf.mcmodmgr.curseforge.responses.Mod
import com.undsf.mcmodmgr.util.BaseApiClient

class CurseForgeApiClient constructor(
        apiKey: String,
        baseUrl: String = "https://api.curseforge.com"
) : BaseApiClient(baseUrl) {
    var apiKey: String = apiKey

    fun getMod(modId: Int) : Mod? {
        return null
    }
}