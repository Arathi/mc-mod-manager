package com.undsf.mcmodmgr.curseforge.responses

import com.undsf.mcmodmgr.curseforge.enums.FileReleaseType
import com.undsf.mcmodmgr.curseforge.enums.ModLoaderType

data class FileIndex(
        var gameVersion: String,
        var fileId: Int,
        var filename: String,
        var releaseType: FileReleaseType,
        var gameVersionTypeId: Int,
        var modLoader: ModLoaderType?
) {
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("$fileId: ")
        builder.append("[${releaseType.name.first()}] ")
        builder.append(gameVersion)
        if (modLoader != null) {
            builder.append("-${modLoader?.name?.lowercase()}")
        }
        builder.append(" $filename")
        return builder.toString()
    }
}