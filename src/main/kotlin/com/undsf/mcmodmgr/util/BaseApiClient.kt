package com.undsf.mcmodmgr.util

import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories

private val log = KotlinLogging.logger {}

abstract class BaseApiClient constructor(
        httpClient: OkHttpClient,
        baseUrl: String?) {
    var httpClient: OkHttpClient = httpClient
    val baseUrl: String? = baseUrl

    fun buildGetRequest(uri: String, params: String? = null, headers: Map<String, String>? = null) : Request {
        val builder = Request.Builder()

        var url = ""
        // 传入完整URL
        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            url = uri
        }
        else {
            if (baseUrl != null) url += baseUrl
            url += uri
        }

        if (params != null && params.isNotEmpty()) {
            if (!url.contains('?')) {
                url += "?"
            }
            url += params
        }

        builder.get().url(url)

        if (headers != null && headers.isNotEmpty()) {
            for (header in headers) {
                builder.addHeader(header.key, header.value)
            }
        }

        return builder.build()
    }

    fun buildPostRequest(
            uri: String,
            body: RequestBody,
            headers: Map<String, String>? = null) : Request {
        val builder = Request.Builder()

        var url = ""
        // 传入完整URL
        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            url = uri
        }
        else {
            if (baseUrl != null) url += baseUrl
            url += uri
        }

        builder.post(body)
                .url(url)

        if (headers != null && headers.isNotEmpty()) {
            for (header in headers) {
                builder.addHeader(header.key, header.value)
            }
        }

        return builder.build()
    }

    fun buildFormBody(params: Map<String, String>?,
                      encoded: Boolean = false) : FormBody {
        var builder = FormBody.Builder()

        if (params != null) {
            for (entry in params.entries) {
                if (encoded) {
                    builder.addEncoded(entry.key, entry.value)
                }
                else {
                    builder.add(entry.key, entry.value)
                }
            }
        }

        return builder.build()
    }

    fun buildQuery(form: FormBody) : String {
        val query = StringBuilder()

        for (index in 1 .. form.size) {
            if (index > 1) {
                query.append("&")
            }
            query.append(form.encodedName(index - 1))
            query.append("=")
            query.append(form.encodedValue(index - 1))
        }

        return query.toString()
    }

    fun buildQuery(params: Map<String, String>?,
                   encoded: Boolean = false) : String {
        val form = buildFormBody(params, encoded)
        return buildQuery(form)
    }

    fun sendRequest(req: Request) : String? {
        val resp = httpClient.newCall(req).execute()
        var body = resp.body
        if (body != null) {
            return body.string()
        }
        log.warn { "请求报文（${req.method} ${req.url}）发送失败" }
        return null
    }

    fun download(path: Path, url: String): Int {
        // 获取文件内容
        val req = buildGetRequest(url)
        val resp = httpClient.newCall(req).execute()
        if (resp.body == null) {
            return 0
        }
        val bytes = resp.body!!.bytes()

        // 生成目录
        path.parent.createDirectories()

        // 写入文件
        val saved = Files.write(path, bytes, StandardOpenOption.CREATE)
        log.info { "下载${url}到${saved.absolutePathString()}，大小${bytes.size}字节" }
        return bytes.size
    }
}