package com.undsf.mcmodmgr.curseforge

import com.fasterxml.jackson.core.type.TypeReference
import com.undsf.mcmodmgr.config.CurseForgeOptions
import com.undsf.mcmodmgr.curseforge.requests.SearchModsCondition
import com.undsf.mcmodmgr.curseforge.responses.DataResponse
import com.undsf.mcmodmgr.curseforge.responses.File
import com.undsf.mcmodmgr.curseforge.responses.Mod
import com.undsf.mcmodmgr.curseforge.responses.PaginationResponse
import com.undsf.mcmodmgr.util.BaseApiClient
import com.undsf.mcmodmgr.util.JSON
import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import java.nio.file.Paths

private val log = KotlinLogging.logger {}

class CurseForgeApiClient constructor(
        httpClient: OkHttpClient,
        options: CurseForgeOptions
) : BaseApiClient(httpClient, options.baseUrl) {
    var apiKey: String = options.apiKey
    var indexesPath: String = options.indexesPath
    var modsPath: String = options.modsPath

    fun addApiKey(headers: MutableMap<String, String>) {
        headers["x-api-key"] = apiKey
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

    fun searchMods(conditions: SearchModsCondition, index: Int? = null, pageSize: Int? = 50) : List<Mod> {
        val uri = "/v1/mods/search"
        val headers = LinkedHashMap<String, String>()
        addApiKey(headers)

        // 构建请求
        val form = FormBody.Builder()
        val params = conditions.buildForm(form)
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

    fun getModFile(modId: Int, fileId: Int): File? {
        val uri = "/v1/mods/${modId}/files/${fileId}"
        val headers = LinkedHashMap<String, String>()
        addApiKey(headers)

        var req = buildGetRequest(uri, headers = headers)
        val respMsg = sendRequest(req, object: TypeReference<DataResponse<File>>() {}) ?: return null
        return respMsg.data
    }

    fun downloadMod(modId: Int, fileId: Int) : Int {
        val file = getModFile(modId, fileId)
        if (file == null) {
            log.warn { "MOD文件（${modId}/${fileId}）信息获取失败" }
            return 0
        }

        if (file.downloadUrl == null) {
            log.warn { "MOD文件下载地址无效" }
            return 0
        }

        val url = URL(file.downloadUrl)
        val path = Paths.get("${modsPath}/${modId}/${fileId}/${file.fileName}")
        return download(path, url.toString())
    }
}