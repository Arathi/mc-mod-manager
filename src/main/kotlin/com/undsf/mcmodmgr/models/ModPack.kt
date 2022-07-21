package com.undsf.mcmodmgr.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class ModPack(name: String,
              version: String,
              mcVersion: String,
              forgeVersion: String,
              dir: String,
              mods: MutableMap<String, Mod> = LinkedHashMap()) {
    @JsonProperty
    var name: String = name

    @JsonProperty
    var version: String = version

    @JsonProperty
    var mcVersion: String = mcVersion

    @JsonProperty
    var forgeVersion: String = forgeVersion

    @JsonProperty
    var dir: String = dir

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var mods: MutableMap<String, Mod> = mods

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