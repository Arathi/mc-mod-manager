package com.undsf.mcmodmgr.curseforge.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnyResponse extends DataResponse<Map<String, Object>> {
    public AnyResponse() {
    }

    public void put(String key, Object value) {
        if (data == null) {
            data = new LinkedHashMap<>();
        }
        data.put(key, value);
    }

    public Object get(String key, Object defaultValue) {
        if (data == null) {
            data = new LinkedHashMap<>();
        }
        return data.getOrDefault(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        Object value = data.getOrDefault(key, defaultValue);
        return (String) value;
    }

    public Integer getInteger(String key, int defaultValue) {
        Object value = data.getOrDefault(key, defaultValue);
        return (Integer) value;
    }
}
