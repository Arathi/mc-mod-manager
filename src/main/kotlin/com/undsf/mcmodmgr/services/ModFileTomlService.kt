package com.undsf.mcmodmgr.services

import com.undsf.mcmodmgr.models.Mod
import com.undsf.mcmodmgr.models.ModDependency
import com.undsf.mcmodmgr.models.ModFileToml
import mu.KotlinLogging
import org.apache.tuweni.toml.Toml
import org.apache.tuweni.toml.TomlTable
import org.springframework.stereotype.Service
import java.io.File
import java.util.jar.JarFile

private val log = KotlinLogging.logger {}

@Service("mod-file-toml-svc")
class ModFileTomlService {
    final val JAR_FILE_NAME_REGEX = Regex(pattern = ".*\\.jar\$")
    final val MODS_TOML_PATH = "META-INF/mods.toml"

    fun scanModFiles(dir: File): List<ModFileToml> {
        val tomls = ArrayList<ModFileToml>()
        var files = dir.listFiles { d, n -> JAR_FILE_NAME_REGEX.find(n) != null }
        if (files != null) {
            for (file in files) {
                val jar = JarFile(file)
                val toml = parse(jar)
                if (toml != null) {
                    tomls.add(toml)
                }
            }
        }
        return tomls
    }

    fun parse(jar: JarFile) : ModFileToml? {
        val manifest = jar.manifest

        val modsTomlEntry = jar.getJarEntry(MODS_TOML_PATH)
        if (modsTomlEntry == null) {
            log.warn { "在${jar.name}中未找到mods.toml" }
            return null
        }

        log.info("开始读取{}的mods.toml", jar.name)
        val modsToml = Toml.parse(jar.getInputStream(modsTomlEntry))
        val fml = modsToml.getString("modLoader")
        val ver = modsToml.getString("loaderVersion")

        if (fml == null) {
            log.warn { "在mods.toml中未找到modLoader" }
            return null
        }

        if (ver == null) {
            log.warn { "在mods.toml中未找到loaderVersion" }
            return null
        }

        val mfl = ModFileToml(jar.name, fml, ver)

        // 获取所有[[mods]]
        var modNodes = modsToml.getArray("mods")
        if (modNodes == null) {
            log.warn { "${jar.name}中未找到mods节点" }
            return null
        }

        for (modIndex in 1 .. modNodes.size()) {
            var modNode = modNodes[modIndex-1]
            if (modNode == null) {
                log.warn{ "获取${jar.name}的第${modIndex}个[[mods]]" }
                continue
            }

            if (modNode !is TomlTable) {
                log.warn { "第${modIndex}个[[mods]]不是table结构" }
                continue
            }

            val modId = modNode.getString("modId")
            var version = modNode.getString("version")
            if (modId == null || version == null) {
                log.warn { "第${modIndex}个MOD的信息不全" }
                continue
            }
            if (version == "\${file.jarVersion}") {
                val manifestVersion = manifest.mainAttributes.getValue("Implementation-Version")
                if (manifestVersion != null) {
                    version = manifestVersion
                }
            }

            val mod = Mod(modId, version)

            // 检查[[dependencies.*]]
            val depNodesKey = "dependencies.$modId"
            val depNodes = modsToml.getArray(depNodesKey)
            if (depNodes != null) {
                log.info { "${modId}有${depNodes.size()}个依赖项" }
                for (depIndex in 1 .. depNodes.size()) {
                    val depNode = depNodes[depIndex - 1]
                    if (depNode == null) {
                        log.warn { "第${depIndex}个依赖项获取失败" }
                        continue
                    }

                    if (depNode !is TomlTable) {
                        log.warn { "第${depIndex}个依赖项不是table结构" }
                        continue
                    }

                    val depId = depNode.getString("modId")
                    val mandatory = depNode.getBoolean("mandatory")
                    val versionRange = depNode.getString("versionRange")
                    val ordering = depNode.getString("ordering")
                    val side = depNode.getString("side")
                    val dep = ModDependency(
                            depId ?: "",
                            mandatory ?: true,
                            versionRange ?: "",
                            ordering ?: "NONE",
                            side ?: "BOTH"
                    )

                    mod.addDependency(dep)
                }
            }
            else {
                log.info { "${modId}没有依赖项" }
            }

            mfl.addMod(mod)
        }

        return mfl
    }
}