package com.undsf.mcmodmgr.models

class AnyResponse(code: Int, message: String?) : DataResponse<MutableMap<String, Any?>>(code, message) {
    init {
        data = LinkedHashMap()
    }

    fun addParam(key: String, value: Any?) {
        data?.put(key, value)
    }
}