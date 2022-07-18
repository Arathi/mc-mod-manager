package com.undsf.mcmodmgr.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("json-util")
@Slf4j
public class JsonUtil {
    public static ObjectMapper mapper;

    public void setMapper(ObjectMapper mapper) {
        JsonUtil.mapper = mapper;
    }

    public static String stringify(Object o) {
        try {
            return mapper.writeValueAsString(o);
        }
        catch (JsonProcessingException ex) {
            log.warn("序列化JSON发生异常", ex);
        }
        return null;
    }

    public static <T> T parse(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        }
        catch (JsonProcessingException ex) {
            log.warn("解析JSON发生异常", ex);
        }
        return null;
    }

    public static <T> T parse(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        }
        catch (JsonProcessingException ex) {
            log.warn("解析JSON发生异常", ex);
        }
        return null;
    }
}
