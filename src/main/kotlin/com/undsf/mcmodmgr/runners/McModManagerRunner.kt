package com.undsf.mcmodmgr.runners

import com.undsf.mcmodmgr.models.ModPack
import com.undsf.mcmodmgr.util.JSON
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val log = KotlinLogging.logger {}

@Component
@Order(0)
class McModManagerRunner : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        if (args == null) {
            printHelp()
            return
        }

        val noArgs = args.nonOptionArgs
        log.debug { "参数如下：" }
        for (arg in noArgs) {
            log.debug { arg }
        }

        val options = args.optionNames
        log.debug { "选项如下：" }
        for (option in options) {
            val values = args.getOptionValues(option)
            log.debug { "$option = ${values.joinToString(",")}" }
        }

        if (noArgs.size < 1) {
            printHelp()
            return
        }

        val command = noArgs[0]

        when(command) {
            "init" -> init(args)
        }
    }

    private fun printHelp() {
        println("""
Minecraft Mods Manager v0.0.1
Usage: mmm command [options...]
    # 初始化当前目录，MC版本1.19，Forge版本41.1.0
    mmm init --name=S224 --minecraft-version=1.19 --forge-version=41.1.0
    # 从CurseForge上搜索jei
    mmm search jei
    # 从CurseForge上下载jei（最新版）
    mmm download jei
    # 从CurseForge上下载jei 11.1.1.239
    mmm download jei --version=11.1.1.239
    # 安装jei，文件名与版本号默认
    mmm install jei
    # 安装jei，命名为"[物品管理]JEI"，版本号为11.1.1.239
    mmm install jei --name="[物品管理]JEI" --version=11.1.1.239
    # 列出所有mod
    mmm list
    # 列出已安装mod
    mmm list --installed
""")
    }

    private fun init(args: ApplicationArguments) {
        val now = LocalDateTime.now()
        var name = "modpack-${now.format(DateTimeFormatter.ISO_DATE_TIME)}"  // 生成名称
        if (args.containsOption("name")) {
            name = args.getOptionValues("name").first()
        }

        var version = "0.1.0"  // 生成版本号
        if (args.containsOption("version")) {
            version = args.getOptionValues("version").first()
        }

        if (!args.containsOption("minecraft-version")) {
            println("未指定Minecraft版本")
            return
        }
        var mcVersion = args.getOptionValues("minecraft-version").first()

        if (!args.containsOption("forge-version")) {
            println("未指定Forge版本")
            return
        }
        var forgeVersion = args.getOptionValues("forge-version").first()

        var userDir = System.getProperty("user.dir")
        var dir = userDir
        if (args.containsOption("dir")) {
            dir = args.getOptionValues("dir").first()
        }

        var modPack = ModPack(name, version, mcVersion, forgeVersion, dir)
        val path = Paths.get("${userDir}/modpack.json")
        val json = JSON.stringify(modPack, true)
        Files.writeString(path, json, StandardOpenOption.CREATE)
    }
}