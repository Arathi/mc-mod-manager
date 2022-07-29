package com.undsf.mcmodmgr.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import java.io.File

private val log = KotlinLogging.logger {}

class ModFileToml(
        jarName: String,
        fml: String,
        version: String
) {
    @JsonProperty
    var name: String

    @JsonProperty
    var modLoader: String = fml

    @JsonProperty
    var loaderVersion: String = version

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var mods: MutableMap<String, Mod> = mutableMapOf()

    init {
        name = jarName
        val lastSeparatorIndex = name.lastIndexOf(File.separatorChar)
        if (lastSeparatorIndex >= 0) {
            name = name.substring(lastSeparatorIndex+1)
        }
    }

    fun addMod(mod: Mod) {
        mods[mod.modId] = mod
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(name)
        builder.append(" ")
        builder.append(modLoader)
        builder.append(" ")
        builder.append(loaderVersion)
        return builder.toString()
    }

    fun toTree(): String {
        val builder = StringBuilder()
        builder.append(toString())
        builder.append(System.lineSeparator())
        val modIdList = mods.keys.toList()
        for ((modIndex, modId) in modIdList.withIndex()) {
            val mod = mods[modId]
            if (mod == null) {
                log.warn("MOD({})不存在", modId)
                continue
            }

            val lastMod = modIndex == mods.size - 1
            builder.append(if (lastMod) "\\" else "+")
            builder.append("--- ")
            builder.append(mod)
            builder.append(System.lineSeparator())
            val depIdList = mod.dependencies.keys.toList()
            for ((depIndex, depId) in depIdList.withIndex()) {
                val dep = mod.dependencies[depId]
                if (dep == null) {
                    log.warn("依赖项({})不存在", depId)
                    continue
                }

                val lastDep = depIndex == mod.dependencies.size - 1
                builder.append(if (lastMod) " " else "|")
                builder.append("    ")
                builder.append(if (lastDep) "\\" else "+")
                builder.append("--- ")
                builder.append(dep)
                builder.append(System.lineSeparator())
            }
        }
        return builder.toString()
    }
}