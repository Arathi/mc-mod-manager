package com.undsf.mcmodmgr.curseforge.responses

data class FileDependency(
        var modId: Int,
        var relationType: Int
) {
    override fun toString(): String {
        var relationTypeName = "Unknown"
        when (relationType) {
            1 -> relationTypeName= "EmbeddedLibrary"
            2 -> relationTypeName= "OptionalDependency"
            3 -> relationTypeName = "RequiredDependency"
            4 -> relationTypeName = "Tool"
            5 -> relationTypeName = "Incompatible"
            6 -> relationTypeName = "Include"
        }
        return "$modId: $relationTypeName"
    }
}