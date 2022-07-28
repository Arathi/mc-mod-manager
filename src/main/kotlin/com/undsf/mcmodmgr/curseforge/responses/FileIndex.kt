package com.undsf.mcmodmgr.curseforge.responses

import com.undsf.mcmodmgr.curseforge.enums.FileReleaseType
import com.undsf.mcmodmgr.curseforge.enums.ModLoaderType

data class FileIndex(
        var gameVersion: String,
        var fileId: Int,
        var filename: String,
        var releaseType: FileReleaseType,
        var gameVersionTypeId: Int,
        var modLoader: ModLoaderType
) {
    override fun toString(): String {
        return "$fileId: [${releaseType.description}] ${gameVersion}-${modLoader.name} $filename"
    }
}