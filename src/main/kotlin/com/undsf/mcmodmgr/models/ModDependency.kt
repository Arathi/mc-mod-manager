package com.undsf.mcmodmgr.models

data class ModDependency(
        var modId: String,
        var mandatory: Boolean,
        var versionRange: String,
        var ordering: String,
        var side: String
) {
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(modId)
        builder.append(" ")
        builder.append(versionRange)
        builder.append(" ")
        builder.append(ordering)
        builder.append(" ")
        builder.append(side)
        if (mandatory) {
            builder.append(" (*)")
        }

        return builder.toString()
    }
}