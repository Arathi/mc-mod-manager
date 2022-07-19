package com.undsf.mcmodmgr.models;

public class Dependency {
    /**
     * mod编号
     */
    public String modId;

    /**
     * 强制依赖
     *
     * 一般不强制依赖都不写的
     */
    public Boolean mandatory;

    /**
     * 依赖版本范围
     */
    public String versionRange;

    /**
     * 顺序
     */
    public String ordering;

    /**
     * SIDE
     *
     * 仅服务端 SERVER
     * 仅客户端 CLIENT
     * 都要安装 BOTH
     */
    public String side;

    @Override
    public String toString() {
        return String.format(
                "%s %s %s",
                modId,
                versionRange,
                side
        );
    }
}
