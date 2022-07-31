package com.undsf.mcmodmgr.models

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
        var mods: MutableMap<String, ModOptions?> = mutableMapOf()
) {
    // fun loadMods(mfts: List<ModFileToml>) {
    //     dependencies["minecraft"] = ModPackDependency("minecraft", "[$mcVersion,$mcVersion]")
    //     dependencies["forge"] = ModPackDependency("forge", "[$forgeVersion,$forgeVersion]")
    //     log.info { "获取到${mfts.size}个MOD文件" }
    //     for (mft in mfts) {
    //         log.info { "正在加载${mft.name}" }
    //         for (mod in mft.mods.values) {
    //             if (dependencies.containsKey(mod.modId)) {
    //                 log.warn { "id为${mod.modId}的MOD已经存在" }
    //             }
    //             dependencies[mod.modId] = ModPackDependency(mod.modId, "[${mod.version},$mod.version]")
    //         }
    //     }
    // }

    fun addDependency(key: String, opt: ModOptions) {
        mods[key] = opt
    }
}