package com.undsf.mcmodmgr.services;

import com.undsf.mcmodmgr.models.ModFileInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

@SpringBootTest
public class ModInfoServiceTests {
    public static final String MODS_DIR = "D:\\Temp\\forge-mods";

    @Autowired
    public ModInfoService modInfoSvc;

    @Test
    public void testParseModsToml() throws IOException {
        String modFilePath = MODS_DIR + File.separator + "TravelersBackpack-1.19-8.0.3.jar";
        JarFile jar = new JarFile(new File(modFilePath));
        ModFileInfo mfi = modInfoSvc.parseModsToml(jar);
        System.out.println(mfi.toTree());
    }
}
