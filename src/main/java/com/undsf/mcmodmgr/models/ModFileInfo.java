package com.undsf.mcmodmgr.models;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
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

    public String toTree() {
        StringBuilder builder = new StringBuilder();

        String fileName = this.fileName;
        int lastSeparatorIndex = fileName.lastIndexOf(File.separator);
        if (lastSeparatorIndex >= 0) {
            fileName = fileName.substring(lastSeparatorIndex + 1);
        }

        builder.append(fileName);
        builder.append(" -> ");
        builder.append(modLoader);
        builder.append(" ");
        builder.append(loaderVersion);
        builder.append(System.lineSeparator());

        if (mods != null && mods.size() > 0) {
            List<String> modIds = mods.keySet().stream().toList();
            for (int modIndex = 0; modIndex < modIds.size(); modIndex++) {
                String modId = modIds.get(modIndex);
                ModInfo mod = mods.get(modId);
                builder.append("+--- ");
                builder.append(mod.modId);
                builder.append(" -> ");
                builder.append(mod.version);
                builder.append(System.lineSeparator());
                boolean lastMod = modIndex == modIds.size() - 1;

                if (mod.dependencies != null && mod.dependencies.size() > 0) {
                    List<String> depIds = mod.dependencies.keySet().stream().toList();
                    for (int depIndex = 0; depIndex < depIds.size(); depIndex++) {
                        String depId = depIds.get(depIndex);
                        Dependency dep = mod.dependencies.get(depId);
                        boolean lastDep = depIndex == depIds.size() - 1;
                        builder.append(lastMod ? " " : "|");
                        builder.append("    ");
                        builder.append(lastDep ? "\\" : "+");
                        builder.append("--- ");
                        builder.append(depId);
                        builder.append(" ");
                        builder.append(dep.versionRange);
                        builder.append(" ");
                        builder.append(dep.side);

                        if (dep.mandatory != null && dep.mandatory) {
                            builder.append(" (*)");
                        }

                        builder.append(System.lineSeparator());
                    }
                }
            }
        }

        return builder.toString();
    }
}
