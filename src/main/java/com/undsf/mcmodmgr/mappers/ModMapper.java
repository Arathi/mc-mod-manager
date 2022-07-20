package com.undsf.mcmodmgr.mappers;

import com.undsf.mcmodmgr.curseforge.models.Mod;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ModMapper {
    /**
     * 根据id获取MOD
     * @param id
     * @return
     */
    Mod getById(long id);

    /**
     * 根据modId获取MOD
     * @param modId
     * @return
     */
    List<Mod> getByModId(String modId);

    /**
     * 保存MOD
     * @param mod
     * @return
     */
    Mod save(Mod mod);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    boolean delete(long id);
}
