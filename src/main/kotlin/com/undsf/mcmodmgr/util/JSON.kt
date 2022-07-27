package com.undsf.mcmodmgr.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class JSON {
    @Autowired
    fun setJsonTemplate(jsonTemplate: JsonTemplate) {
        JSON.jsonTemplate = jsonTemplate
    }

    companion object {
        private lateinit var jsonTemplate: JsonTemplate

        fun stringify(obj: Any) : String? {
            return jsonTemplate.stringify(obj)
        }

        fun parse(json: String) : JsonNode? {
            return jsonTemplate.parse(json)
        }

        fun <T> parse(json: String, type: Class<T>) : T? {
            return jsonTemplate.parsec(json, type)
        }

        fun <T> parse(json: String, type: TypeReference<T>) : T? {
            return jsonTemplate.parser(json, type)
        }

        fun <T> parse(json: String, superType: Class<T>, vararg paramTypes: Class<*>) : T? {
            val paramTypeList = ArrayList<Class<*>>()
            for (paramType in paramTypes) {
                paramTypeList.add(paramType)
            }
            return jsonTemplate.parsev(json, superType, paramTypeList.toTypedArray())
        }
    }
}