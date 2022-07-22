package com.undsf.mcmodmgr.util

import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

private val log = KotlinLogging.logger {}

abstract open class BaseApiClient constructor(
        httpClient: OkHttpClient,
        baseUrl: String?) {
    var httpClient: OkHttpClient = httpClient
    val baseUrl: String? = baseUrl

    fun buildGetRequest(uri: String, params: String? = null, headers: Map<String, String>? = null) : Request {
        val builder = Request.Builder()

        var url = ""
        if (baseUrl != null) url += baseUrl
        url += uri

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
        if (baseUrl != null) url += baseUrl
        url += uri

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
        log.info { "请求报文（${req.method} ${req.url}）发送失败" }
        return null
    }
}