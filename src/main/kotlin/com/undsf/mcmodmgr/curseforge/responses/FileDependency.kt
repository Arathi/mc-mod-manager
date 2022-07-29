package com.undsf.mcmodmgr.curseforge.responses

import com.undsf.mcmodmgr.curseforge.enums.RelationType

data class FileDependency(
        var modId: Int,
        var relationType: RelationType
) {
    override fun toString(): String {
        return "$modId: ${relationType.name}"
    }
}