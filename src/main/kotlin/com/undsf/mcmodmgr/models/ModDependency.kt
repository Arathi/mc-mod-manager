package com.undsf.mcmodmgr.models

class ModDependency(
        modId: String,
        mandatory: Boolean,
        versionRange: String,
        ordering: String,
        side: String
) {
    var modId: String = modId
    var mandatory: Boolean = mandatory
    var versionRange: String = versionRange
    var ordering: String = ordering
    var side: String = side

    override fun toString(): String {
        var builder = StringBuilder()
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