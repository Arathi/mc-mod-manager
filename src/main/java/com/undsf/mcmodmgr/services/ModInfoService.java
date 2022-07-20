package com.undsf.mcmodmgr.services;

import com.undsf.mcmodmgr.models.Dependency;
import com.undsf.mcmodmgr.models.ModFileInfo;
import com.undsf.mcmodmgr.models.ModInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tuweni.toml.Toml;
import org.apache.tuweni.toml.TomlParseError;
import org.apache.tuweni.toml.TomlParseResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("mod-file-svc")
@Slf4j
public class ModInfoService {
    public static final Pattern MOD_FILE_NAME_PATTERN = Pattern.compile(".*\\.jar$");

    public static final String MODS_TOML_PATH = "META-INF/mods.toml";

    public List<ModFileInfo> getModInfoListFromDir(File dir) {
        // 列出jar包
        File[] jars = dir.listFiles((d, n) -> {
            Matcher m = MOD_FILE_NAME_PATTERN.matcher(n);
            return m.find();
        });
        if (jars == null || jars.length == 0) {
            log.info("扫描mod目录{}，未发现有效jar包", dir.getAbsolutePath());
            return null;
        }
        log.info("扫描mod目录{}，发现jar包{}个", dir.getAbsolutePath(), jars.length);

        // 检查Forge MOD
        List<ModFileInfo> mfis = new ArrayList<>();
        for (File f : jars) {
            try {
                JarFile jar = new JarFile(f);
                ModFileInfo mfi = parseModsToml(jar);
                if (mfi != null) {
                    mfis.add(mfi);
                }
                else {
                    log.warn("解析{}，获得null", f.getName());
                }
            }
            catch (IOException ex) {
                log.warn("读取{}时发生IO异常", f.getName(), ex);
            }
        }

        return mfis;
    }

    public ModFileInfo parseModsToml(JarFile jar) {
        try {
            JarEntry modsTomlEntry = jar.getJarEntry(MODS_TOML_PATH);
            if (modsTomlEntry == null) {
                log.warn("在{}中未找到{}", jar.getName(), MODS_TOML_PATH);
                return null;
            }

            TomlParseResult modsToml = Toml.parse(jar.getInputStream(modsTomlEntry));
            modsToml.getString("mods.modId");

            // 获取fml
            ModFileInfo modFileInfo = new ModFileInfo();
            modFileInfo.fileName = jar.getName();
            modFileInfo.modLoader = modsToml.getString("modLoader");
            modFileInfo.loaderVersion = modsToml.getString("loaderVersion");

            // 检查mods
            var mods = modsToml.getArray("mods");
            if (mods == null || mods.size() <= 0) {
                log.info("{}中没有mod信息", jar.getName());
                return modFileInfo;
            }

            // 扫描所有
            for (var modIndex = 0; modIndex < mods.size(); modIndex++) {
                ModInfo modInfo = new ModInfo();
                var mod = mods.getTable(modIndex);

                modInfo.modId = mod.getString("modId");
                modInfo.version = mod.getString("version");
                modInfo.displayName = mod.getString("displayName");

                String key = "dependencies." + modInfo.modId;
                var deps = modsToml.getArrayOrEmpty(key);
                if (deps.size() <= 0) {
                    log.info("{}没有依赖", modInfo.modId);
                    continue;
                }

                for (var depIndex = 0; depIndex < deps.size(); depIndex++) {
                    Dependency dependency = new Dependency();
                    var dep = deps.getTable(depIndex);
                    if (dep == null) {
                        log.info("未获取到{}的第{}个依赖项", modInfo.modId, depIndex+1);
                        continue;
                    }
                    dependency.modId = dep.getString("modId");
                    dependency.mandatory = dep.getBoolean("mandatory");
                    dependency.versionRange = dep.getString("versionRange");
                    dependency.ordering = dep.getString("ordering");
                    dependency.side = dep.getString("side");
                    modInfo.addDependency(dependency);
                }

                modFileInfo.addMod(modInfo);
            }

            log.info("{}的mods.toml解析完成：{}", jar.getName(), modFileInfo);
            return modFileInfo;
        }
        catch (IOException ex) {
            log.warn("读取jar包{}发生IO异常", jar.getName(), ex);
        }
        catch (TomlParseError tpe) {
            log.warn("TOML解析失败", tpe);
        }
        return null;
    }
}
