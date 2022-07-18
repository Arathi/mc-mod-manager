package com.undsf.mcmodmgr.services;

import com.undsf.mcmodmgr.curseforge.CurseForgeApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mod-repo-svc-curseforge")
public class ModRepositoryService {
    @Autowired
    public CurseForgeApi api;
}
