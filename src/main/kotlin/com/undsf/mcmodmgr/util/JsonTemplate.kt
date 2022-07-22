package com.undsf.mcmodmgr.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

class JsonTemplate(mapper: ObjectMapper) {
    var mapper: ObjectMapper = mapper
    var types = HashMap<String, JavaType>()

    fun stringify(obj: Any) : String? {
        try {
            return mapper.writeValueAsString(obj)
        }
        catch (ex: JsonProcessingException) {
            log.warn("JSON序列化发生异常", ex)
        }
        return null
    }

    fun <T> parsec(json: String, type: Class<T>) : T? {
        try {
            return mapper.readValue(json, type)
        }
        catch (ex: JsonProcessingException) {
            log.warn("JSON反序列化发生异常", ex)
        }
        return null
    }

    fun <T> parser(json: String, type: TypeReference<T>) : T? {
        try {
            return mapper.readValue(json, type)
        }
        catch (ex: JsonProcessingException) {
            log.warn("JSON反序列化发生异常", ex)
        }
        return null
    }

    fun <T> parset(json: String, type: JavaType) : T? {
        try {
            return mapper.readValue(json, type)
        }
        catch (ex: JsonProcessingException) {
            log.warn("JSON反序列化发生异常", ex)
        }
        return null
    }

    fun <S> parsel(json: String, superType: Class<S>, vararg paramTypes: Class<*>) : S? {
        val paramTypeList = ArrayList<Class<*>>()
        for (paramType in paramTypes) {
            paramTypeList.add(paramType)
        }
        return parsev(json, superType, paramTypeList.toTypedArray())
    }

    fun <S> parsev(json: String, superType: Class<S>, paramTypes: Array<Class<*>>) : S? {
        val classNameBuilder = StringBuilder()
        classNameBuilder.append(superType.name)
        classNameBuilder.append("<")
        for ((index, paramType) in paramTypes.withIndex()) {
            if (index > 0) classNameBuilder.append(",")
            classNameBuilder.append(paramType)
        }
        classNameBuilder.append(">")
        val className = classNameBuilder.toString()

        if (!types.containsKey(className)) {
            log.info { "类型${className}不存在，构建JavaType" }
            val typeParams = ArrayList<JavaType>()
            for (paramType in paramTypes) {
                typeParams.add(mapper.constructType(paramType))
            }
            val type = mapper.typeFactory.constructSimpleType(superType, typeParams.toTypedArray())
            types[className] = type
        }

        val type = types[className]
        if (type == null) {
            log.warn { "未找到类型$className" }
            return null
        }
        return parset(json, type)
    }
}
