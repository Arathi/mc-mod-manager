package com.undsf.mcmodmgr.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class ModPack(
        @JsonProperty
        var name: String,

        @JsonProperty
        var version: String,

        @JsonProperty
        var mcVersion: String,

        @JsonProperty
        var forgeVersion: String,

        @JsonProperty
        var dir: String,

        @JsonProperty
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var mods: MutableMap<String, Mod> = mutableMapOf()
) {
    fun loadMods(mfts: List<ModFileToml>) {
        mods["minecraft"] = Mod("minecraft", mcVersion)
        mods["forge"] = Mod("forge", forgeVersion)

        log.info { "获取到${mfts.size}个MOD文件" }
        for (mft in mfts) {
            log.info { "正在加载${mft.name}" }
            for (mod in mft.mods.values) {
                if (mods.containsKey(mod.modId)) {
                    log.warn { "id为${mod.modId}的MOD已经存在" }
                }
                mods[mod.modId] = mod
            }
        }
    }
}