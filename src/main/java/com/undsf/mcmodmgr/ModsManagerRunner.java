package com.undsf.mcmodmgr;

import com.undsf.mcmodmgr.curseforge.models.Mod;
import com.undsf.mcmodmgr.models.ModFileInfo;
import com.undsf.mcmodmgr.services.ModInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

@Component
@Order(0)
@Slf4j
public class ModsManagerRunner implements ApplicationRunner {
    public static final String MOD_LOADER_FORGE = "forge";
    public static final String MOD_LOADER_FABRIC = "fabric";

    @Autowired
    public ModInfoService modInfoSvc;

    public void printHelp() {
        PrintStream ps = System.out;
        ps.println("Arathi's Minecraft Mods Manager");
        ps.println();
        ps.println("usage: mmm []");
        ps.println();
        ps.println("mmm init --mod-loader=forge --version=1.19");
        ps.println("mmm init --forge --version=1.19");
        ps.println("mmm init --fabric --version=1.19");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("无值参数如下：");
        for (var optionArgName : args.getNonOptionArgs()) {
            log.info("{}", optionArgName);
        }

        log.info("有值参数如下：");
        for (var optionArgName : args.getOptionNames()) {
            List<String> values = args.getOptionValues(optionArgName);
            log.info("{} = {}", optionArgName, String.join(",", values));
        }

        var nonOptionArgs = args.getNonOptionArgs();
        if (nonOptionArgs.size() <= 0) {
            printHelp();
            return;
        }

        var mode = nonOptionArgs.get(0);
        switch (mode) {
            case "init":
                runInit(args);
                return;
            case "search":
                runSearch(args);
                return;
            case "install":
                runInstall(args);
                return;
            case "list":
                runList(args);
                return;
            case "check":
                runCheck(args);
                return;
            case "help":
            default:
                printHelp();
        }
    }

    public void runInit(ApplicationArguments args) {
        // region 获取mod-loader
        String modLoader = null;
        if (args.getOptionNames().contains("mod-loader")) {
            List<String> modLoaderNames = args.getOptionValues("mod-loader");
            for (String modLoaderName : modLoaderNames) {
                switch (modLoaderName.toLowerCase()) {
                    case MOD_LOADER_FORGE:
                        modLoader = MOD_LOADER_FORGE;
                        break;
                    case MOD_LOADER_FABRIC:
                        modLoader = MOD_LOADER_FABRIC;
                        break;
                    default:
                        log.warn("{}不是有效的模组加载器名称", modLoaderName);
                        break;
                }
                if (modLoader != null) {
                    break;
                }
            }
        }

        if (modLoader == null) {
            if (args.getOptionNames().contains(MOD_LOADER_FORGE)) {
                modLoader = MOD_LOADER_FORGE;
            }
            else if (args.getOptionNames().contains(MOD_LOADER_FABRIC)) {
                modLoader = MOD_LOADER_FABRIC;
            }
        }

        if (modLoader == null) {
            log.warn("未指定模组加载器");
            return;
        }

        log.info("指定模组加载器为{}", modLoader);
        // endregion

        // region 获取版本号
        String version = null;
        if (args.getOptionNames().contains("version")) {
            List<String> versions = args.getOptionValues("version");
            if (versions.size() == 1) {
                version = versions.get(0);
            }
            else {
                log.warn("无效的版本号：{}", String.join(",", versions));
            }
        }

        if (version == null) {
            log.warn("未指定版本号");
            return;
        }

        log.info("指定版本号为{}", version);
        // endregion
    }

    public void runSearch(ApplicationArguments args) {
        var nonOptionArgs = args.getNonOptionArgs();
        if (nonOptionArgs.size() <= 1) {
            log.info("search命令参数不足");
            return;
        }

        String filter = nonOptionArgs.get(1);
        log.info("搜索关键字为：{}", filter);
    }

    public void runInstall(ApplicationArguments args) {

    }

    public void runList(ApplicationArguments args) {

    }

    public void runCheck(ApplicationArguments args) {
        String workDir = System.getProperty("user.dir");
        log.info("当前工作目录：{}", workDir);
        File modsDir = new File(workDir);

        List<ModFileInfo> modFileInfos = modInfoSvc.getModInfoListFromDir(modsDir);
        for (ModFileInfo mfi : modFileInfos) {
            System.out.println(mfi.toTree());
        }
    }
}
