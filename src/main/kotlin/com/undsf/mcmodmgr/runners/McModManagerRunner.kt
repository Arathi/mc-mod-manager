package com.undsf.mcmodmgr.runners

import com.undsf.mcmodmgr.curseforge.CurseForgeApiClient
import com.undsf.mcmodmgr.curseforge.enums.ModLoaderType
import com.undsf.mcmodmgr.curseforge.enums.RelationType
import com.undsf.mcmodmgr.curseforge.requests.SearchModsCondition
import com.undsf.mcmodmgr.curseforge.responses.File
import com.undsf.mcmodmgr.models.ModDependency
import com.undsf.mcmodmgr.models.Mod as TomlMod
import com.undsf.mcmodmgr.curseforge.responses.Mod as CurseForgeMod
import com.undsf.mcmodmgr.models.ModPack
import com.undsf.mcmodmgr.util.JSON
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.file.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val log = KotlinLogging.logger {}

@Component
@Order(0)
class McModManagerRunner : ApplicationRunner {
    private var modPack: ModPack? = null

    @Autowired
    lateinit var client: CurseForgeApiClient

    /**
     * 使用硬连接
     */
    @Value("\${mmm.install.use-hard-link:false}")
    var useHardLink: Boolean = false

    /**
     * 使用软连接
     */
    @Value("\${mmm.install.use-symbolic-link:false}")
    var useSymbolicLink: Boolean = false

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
            "search" -> search(args)
            "download" -> download(args)
            "install" -> install(args)
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
""".trim())
    }

    // region 公共
    private fun loadModPack() : ModPack? {
        if (modPack != null) {
            return modPack
        }

        val userDir = System.getProperty("user.dir")
        val modPackPath = Paths.get("${userDir}/modpack.json")
        if (!Files.exists(modPackPath)) {
            log.warn { "ModFile文件不存在！" }
            return null
        }

        val json = Files.readString(modPackPath)
        modPack = JSON.parse(json, ModPack::class.java)
        if (modPack == null) {
            log.warn { "ModPack配置文件加载失败" }
            return null
        }

        return modPack
    }
    // endregion

    // region init 初始化
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

        modPack = ModPack(name, version, mcVersion, forgeVersion, dir)
        if (modPack == null) {
            log.warn { "ModPack生成失败" }
            return
        }

        val path = Paths.get("${userDir}/modpack.json")
        val json = JSON.stringify(modPack!!, true)
        Files.writeString(path, json, StandardOpenOption.CREATE)
        log.info { "ModPack配置文件生成完成" }
    }
    // endregion

    // region search 搜索
    private fun searchForgeMods(
            searchFilter: String?,
            slug: String?,
            pageLimit: Int = 1) : List<CurseForgeMod> {
        val conditions = SearchModsCondition()
        conditions.gameVersion = modPack?.mcVersion
        conditions.searchFilter = searchFilter
        conditions.modLoaderType = ModLoaderType.Forge
        conditions.slug = slug

        return client.searchMods(conditions, pageLimit)
    }

    private fun search(args: ApplicationArguments) {
        loadModPack()
        if (modPack == null) {
            log.warn { "无法读取MC及Mod加载器版本信息" }
            return
        }

        if (args.nonOptionArgs.size < 2) {
            log.warn { "请输入Mod关键字" }
            return
        }

        val keyWord = args.nonOptionArgs[1]
        var searchFilter: String? = null
        var slug: String? = null

        if (args.containsOption("--name")) {
            searchFilter = keyWord
        }
        else {
            slug = keyWord
        }

        val searchResults = searchForgeMods(searchFilter, slug, 10)

        println("搜索到MOD共${searchResults.size}个")
        for ((index, result) in searchResults.withIndex()) {
            println("$index: $result")
        }
    }
    // endregion

    // region download 下载
    private fun download(
            modId: Int?,
            slug: String?,
            version: String?,
            downloadDependency: Boolean = false) : List<Path> {
        val paths = arrayListOf<Path>()
        var mod: CurseForgeMod? = null

        if (modId == null) {
            // 通过slug获取mod信息
            if (slug == null || slug.isEmpty()) {
                log.warn { "未指定modId，也未设置有效的slug" }
                return paths
            }

            val mods = searchForgeMods(null, slug, 1)
            if (mods.size != 1) {
                if (mods.isEmpty()) {
                    log.warn { "未找到slug为${slug}的mod" }
                }
                log.warn { "搜索到${mods.size}个${slug}" }
                for ((index, m) in mods.withIndex()) {
                    println("${index}: $m")
                }
                return paths
            }

            mod = mods.first()
        }
        else {
            // 根据modId获取mod信息
            mod = client.getMod(modId)
        }

        if (mod == null) {
            log.warn { "未找到指定mod" }
            return paths
        }

        // 根据modId获取file
        val searchResults = client.getModFiles(
                mod.id,
                modPack?.mcVersion,
                ModLoaderType.Forge,
                null,
                1
        )

        // 过滤版本号
        val filteredFiles = arrayListOf<File>()
        if (version != null) {
            log.info { "根据版本号${version}筛选mod文件" }
            for (f in searchResults) {
                if (f.fileName.contains(version)) {
                    filteredFiles.add(f)
                }
            }
        }
        else {
            filteredFiles.addAll(searchResults)
        }

        var file: File? = null
        if (filteredFiles.size > 0) {
            file = filteredFiles.first()
            if (filteredFiles.size > 1) {
                log.info { "${mod.slug}搜索到${filteredFiles.size}个文件" }
                for ((index, f) in filteredFiles.withIndex()) {
                    println("${index+1}: $f")
                }
            }
        }

        if (file != null) {
            val downloaded = client.downloadMod(file)
            if (downloaded != null) {
                paths.add(downloaded)
            }
            else {
                log.info { "${file.fileName}下载失败" }
            }

            // 递归下载
            if (downloadDependency) {
                if (file.dependencies.isNotEmpty()) {
                    log.info { "${file.fileName}有${file.dependencies.size}个依赖项" }
                    for (dep in file.dependencies) {
                        if (dep.relationType == RelationType.RequiredDependency) {
                            log.info { "开始下载${file.fileName}的依赖项${dep.modId}" }
                            val downloaded = download(
                                    dep.modId,
                                    null,
                                    null,
                                    true
                            )
                            paths.addAll(downloaded)
                        }
                    }
                }
                else {
                    log.info { "${file.fileName}没有依赖项" }
                }
            }
            else {
                log.info { "不下载${file.fileName}的依赖项" }
            }
        }

        return paths
    }

    private fun download(args: ApplicationArguments) : List<Path> {
        val paths = arrayListOf<Path>()

        loadModPack()
        if (modPack == null) {
            log.warn { "无法读取MC及Mod加载器版本信息" }
            return paths
        }

        if (args.nonOptionArgs.size < 2) {
            log.warn { "请输入Mod关键字" }
            return paths
        }

        val slug = args.nonOptionArgs[1]
        var version: String? = null
        if (args.containsOption("version")) {
            version = args.getOptionValues("version").first()
        }
        var recursion = true
        if (args.containsOption("recursion")) {
            recursion = args.getOptionValues("recursion").first().toBoolean()
        }

        paths.addAll(download(null, slug, version, recursion))
        return paths
    }
    // endregion

    // region install 安装
    private fun install(args: ApplicationArguments) : Boolean {
        loadModPack()
        if (modPack == null) {
            log.warn { "无法读取MC及Mod加载器版本信息" }
            return false
        }

        if (args.nonOptionArgs.size < 2) {
            log.warn { "请输入Mod关键字" }
            return false
        }

        var prefix = ""
        if (args.containsOption("prefix")) {
            prefix = "[" + args.getOptionValues("prefix").first() + "]"
        }

        val slug = args.nonOptionArgs[1]
        val downloaded = download(null, slug, null, true)

        log.info { "开始安装${downloaded.size}个MOD" }
        for ((index, existing) in downloaded.withIndex()) {
            log.info { "第${index+1}个：${existing.fileName}" }
            var linkPath: StringBuilder = StringBuilder()
            linkPath.append("${modPack?.dir}/")
            if (index == 0) {
                linkPath.append(prefix)
            }
            else {
                linkPath.append("[依赖]")
            }
            linkPath.append(existing.fileName)
            val link = Paths.get(linkPath.toString())
            var created: Path? = null

            if (useHardLink) {
                // 硬连接
                try {
                    log.info { "正在创建硬连接${existing} =H=> $link" }
                    created = Files.createLink(link, existing)
                } catch (ex: IOException) {
                    log.warn("无法创建${existing}的硬连接，尝试创建软连接", ex)
                }
            }

            if (useSymbolicLink) {
                // 软连接
                try {
                    if (created == null) {
                        log.info { "正在创建软连接${existing} =S=> $link" }
                        created = Files.createSymbolicLink(link, existing)
                    }
                }
                catch (ex: IOException) {
                    log.warn ("无法创建${existing}的软连接，尝试复制文件", ex)
                }
            }

            // 复制文件
            try {
                if (created == null) {
                    log.info { "正在复制文件${existing} =C=> $link" }
                    created = Files.copy(existing, link, StandardCopyOption.REPLACE_EXISTING)
                }
            }
            catch (ex: IOException) {
                log.warn ("无法复制文件${existing}到${link}，安装失败", ex)
                return false
            }
        }

        log.info { "${slug}安装完成" }
        return true
    }
    // endregion
}