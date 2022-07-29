package com.undsf.mcmodmgr.curseforge

import com.fasterxml.jackson.core.type.TypeReference
import com.undsf.mcmodmgr.config.CurseForgeOptions
import com.undsf.mcmodmgr.curseforge.enums.ModLoaderType
import com.undsf.mcmodmgr.curseforge.requests.SearchModsCondition
import com.undsf.mcmodmgr.curseforge.responses.*
import com.undsf.mcmodmgr.curseforge.responses.File as ModFile
import com.undsf.mcmodmgr.util.BaseApiClient
import com.undsf.mcmodmgr.util.JSON
import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File

private val log = KotlinLogging.logger {}

class CurseForgeApiClient constructor(
        httpClient: OkHttpClient,
        options: CurseForgeOptions
) : BaseApiClient(httpClient, options.baseUrl) {
    var options = options

    fun addApiKey(headers: MutableMap<String, String>) {
        headers["x-api-key"] = options.apiKey
    }

    fun <R> sendRequest(req: Request, type: TypeReference<R>) : R? {
        val json = sendRequest(req) ?: return null
        return JSON.parse(json, type)
    }

    fun getMod(modId: Int) : Mod? {
        val uri = "/v1/mods/$modId"
        val headers = LinkedHashMap<String, String>()
        addApiKey(headers)

        val req = buildGetRequest(uri, headers = headers)
        val respMsg = sendRequest(req, object: TypeReference<DataResponse<Mod>>() {})
        if (respMsg != null) {
            return respMsg.data
        }
        return null
    }

    @Deprecated("使用合并搜索searchMods")
    fun searchModsSinglePage(
            conditions: SearchModsCondition,
            index: Int? = null,
            pageSize: Int? = 50) : List<Mod> {
        val uri = "/v1/mods/search"
        val headers = LinkedHashMap<String, String>()
        addApiKey(headers)

        // 构建请求
        val form = FormBody.Builder()
        conditions.buildForm(form)
        if (index != null) form.add("index", index.toString())
        if (pageSize != null) form.add("pageSize", pageSize.toString())

        // 生成参数
        val query = buildQuery(form.build())

        var req = buildGetRequest(uri, query, headers)

        var mods = ArrayList<Mod>()
        val respMsg = sendRequest(req, object: TypeReference<PaginationResponse<Mod>>() {})
        if (respMsg != null) {
            mods.addAll(respMsg.data)
        }

        return mods
    }

    fun searchMods(
            conditions: SearchModsCondition,
            pageLimit: Int = 1,
            pageSize: Int = 50) : List<Mod> {
        val mods = ArrayList<Mod>()

        val uri = "/v1/mods/search"
        val headers = LinkedHashMap<String, String>()
        addApiKey(headers)

        var pageLimit = pageLimit
        if (pageLimit < 0) {
            pageLimit = Int.MAX_VALUE
        }
        for (pageIndex in 1 .. pageLimit) {
            log.info { "正在搜索第${pageIndex}页" }

            val form = FormBody.Builder()
            conditions.buildForm(form)

            val index = (pageIndex - 1) * pageSize
            form.add("index", index.toString())
            form.add("pageSize", pageSize.toString())

            val query = buildQuery(form.build())
            val req = buildGetRequest(uri, query, headers)

            val respMsg = sendRequest(req, object: TypeReference<PaginationResponse<Mod>>() {})
            if (respMsg == null) {
                log.warn { "获取MOD信息第${pageIndex}页出错" }
                return mods
            }

            mods.addAll(respMsg.data)
            if (!respMsg.pagination.hasNextPage()) {
                break
            }
        }

        return mods
    }

    @Deprecated("使用合并搜索getModFiles")
    fun getModFilesSinglePage(
            modId: Int,
            gameVersion: String?,
            modLoaderType: ModLoaderType?,
            gameVersionTypeId: Int?,
            index: Int? = 0,
            pageSize: Int? = 50) {
        throw NotImplementedError("未实现")
    }

    fun getModFiles(
            modId: Int,
            gameVersion: String? = null,
            modLoaderType: ModLoaderType? = null,
            gameVersionTypeId: Int? = null,
            pageLimit: Int = 1) : List<ModFile> {
        val uri = "/v1/mods/${modId}/files"
        val headers = LinkedHashMap<String, String>()
        addApiKey(headers)

        val params = LinkedHashMap<String, String>()

        if (gameVersion != null) {
            params["gameVersion"] = gameVersion
        }

        if (modLoaderType != null) {
            params["modLoaderType"] = modLoaderType.ordinal.toString()
        }

        if (gameVersionTypeId != null) {
            params["gameVersionTypeId"] = gameVersionTypeId.toString()
        }

        val pageSize = DEFAULT_PAGE_SIZE
        val files = ArrayList<ModFile>()
        for (pageIndex in 1 .. pageLimit) {
            log.info { "正在获取MOD(${modId})文件列表第${pageIndex}页" }

            val index = (pageIndex - 1) * pageSize
            params["index"] = index.toString()
            params["pageSize"] = pageSize.toString()
            val query = buildQuery(params)

            var req = buildGetRequest(uri, params = query, headers = headers)
            val respMsg = sendRequest(
                    req,
                    object : TypeReference<PaginationResponse<ModFile>>() {}
            )

            if (respMsg == null) {
                log.warn { "获取MOD文件第${pageSize}页时出错" }
                return files
            }

            files.addAll(respMsg.data)
            if (!respMsg.pagination.hasNextPage()) {
                break
            }
        }

        return files
    }

    fun getModFile(modId: Int, fileId: Int): ModFile? {
        val uri = "/v1/mods/${modId}/files/${fileId}"
        val headers = LinkedHashMap<String, String>()
        addApiKey(headers)

        var req = buildGetRequest(uri, headers = headers)
        val respMsg = sendRequest(req, object: TypeReference<DataResponse<ModFile>>() {}) ?: return null
        return respMsg.data
    }

    fun downloadMod(modId: Int, fileId: Int) : Int {
        val file = getModFile(modId, fileId)
        if (file == null) {
            log.warn { "MOD文件（${modId}/${fileId}）信息获取失败" }
            return 0
        }
        return downloadMod(file)
    }

    fun downloadMod(file: ModFile) : Int {
        if (file.downloadUrl == null || file.downloadUrl!!.trim().isEmpty()) {
            log.warn { "MOD文件下载地址无效" }
            return 0
        }

        // 检查文件是否存在
        val path = Paths.get("${options.modsPath}/${file.modId}/${file.id}/${file.fileName}")

        do {
            if (Files.exists(path)) {
                if (!options.ignoreSizeCheck) {
                    log.info { "正在检查已存在文件${file.fileName}的大小" }
                    val size = Files.size(path)
                    if (file.fileLength != size) {
                        log.warn { "文件大小不同，重新下载" }
                        break
                    }
                    else {
                        log.info { "${file.fileName}的文件大小相同，均为$size" }
                    }
                }
                else {
                    log.debug { "忽略文件大小检查" }
                }

                if (!options.ignoreHashCheck) {
                    log.info { "正在检查已存在文件${file.fileName}的哈希值" }
                    // TODO 检查哈希值
                    // for (fileHash in file.hashes) {
                    // }
                }
                else {
                    log.debug { "忽略文件哈希检查" }
                }

                log.info { "MOD文件已存在" }
                return 0
            }
        }
        while (false)

        val url = URL(file.downloadUrl)
        return download(path, url.toString())
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 50
    }
}