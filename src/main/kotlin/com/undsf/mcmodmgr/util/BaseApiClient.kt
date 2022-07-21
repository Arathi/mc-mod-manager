package com.undsf.mcmodmgr.util

import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody

abstract class BaseApiClient constructor(baseUrl: String?) {
    val baseUrl: String? = baseUrl

    fun buildGetRequest(uri: String, params: String? = null) : Request {
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

        return builder.build()
    }

    fun buildPostRequest(uri: String, body: RequestBody) : Request {
        val builder = Request.Builder()

        var url = ""
        if (baseUrl != null) url += baseUrl
        url += uri

        builder.post(body)
                .url(url)

        return builder.build()
    }

    protected fun buildFormBody(params: Map<String, String>?,
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

    protected fun buildQuery(params: Map<String, String>?,
                   encoded: Boolean = false) : String {
        val form = buildFormBody(params, encoded)
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
}