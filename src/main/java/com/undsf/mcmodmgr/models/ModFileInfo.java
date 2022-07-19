package com.undsf.mcmodmgr.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModFileInfo {
    /**
     * mod文件名
     */
    public String fileName;

    /**
     * FML类型
     *
     * javafml
     * kotlinfml（此时需要依赖kotlin for forge）
     */
    public String modLoader;

    /**
     * FML版本范围
     */
    public String loaderVersion;

    /**
     * 内含MOD
     *
     * 一般只有一个，但也有一个文件中有很多MOD的情况
     */
    public Map<String, ModInfo> mods;

    public void addMod(ModInfo mod) {
        if (mods == null) {
            mods = new LinkedHashMap<>();
        }
        mods.put(mod.modId, mod);
    }

    @Override
    public String toString() {
        int modAmount = 0;
        if (mods != null) {
            modAmount = mods.size();
        }
        return String.format("%s %s %s %d个MOD", fileName, modLoader, loaderVersion, modAmount);
    }
}
