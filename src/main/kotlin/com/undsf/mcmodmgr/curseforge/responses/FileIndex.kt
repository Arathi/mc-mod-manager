package com.undsf.mcmodmgr.curseforge.responses

data class FileIndex(
        var gameVersion: String,
        var fileId: Int,
        var filename: String,
        var releaseType: Int,
        var gameVersionTypeId: Int,
        var modLoader: Int
) {
    override fun toString(): String {
        var releaseTypeName = "U"
        when (releaseType) {
            1 -> releaseTypeName = "R" // Release
            2 -> releaseTypeName = "B" // Beta
            3 -> releaseTypeName = "A" // Alpha
        }

        var modLoaderName = "Unknown"
        when (modLoader) {
            0 -> modLoaderName = "Any"
            1 -> modLoaderName = "Forge"
            2 -> modLoaderName = "Cauldron"
            3 -> modLoaderName = "LiteLoader"
            4 -> modLoaderName = "Fabric"
            5 -> modLoaderName = "Quilt"
        }

        return "$fileId: [$releaseTypeName] ${gameVersion}-${modLoaderName} $filename"
    }
}