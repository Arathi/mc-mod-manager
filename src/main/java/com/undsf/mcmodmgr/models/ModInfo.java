package com.undsf.mcmodmgr.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModInfo {
    /**
     * 编号
     */
    public String modId;

    /**
     * 版本
     */
    public String version;

    /**
     * 显示名称
     */
    public String displayName;

    /**
     * 依赖
     */
    public Map<String, Dependency> dependencies;

    public void addDependency(Dependency dep) {
        if (dependencies == null) {
            dependencies = new LinkedHashMap<>();
        }
        dependencies.put(dep.modId, dep);
    }

    @Override
    public String toString() {
        int depAmount = 0;
        if (dependencies != null) {
            depAmount = dependencies.size();
        }
        return String.format("%s %s %d个依赖", modId, version, depAmount);
    }
}
