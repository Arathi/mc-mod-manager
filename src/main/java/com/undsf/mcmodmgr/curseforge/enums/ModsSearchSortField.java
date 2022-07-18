package com.undsf.mcmodmgr.curseforge.enums;

public enum ModsSearchSortField {
    None(0, "无"),
    Featured(1, "特性"),
    Popularity(2, "流行"),
    LastUpdated(2, "最后更新"),
    Name(3, "名称"),
    Author(4, "作者"),
    TotalDownloads(5, "总下载量"),
    Category(6, "分类"),
    GameVersion(7, "游戏版本");

    public int value;

    public String name;

    ModsSearchSortField(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "(" + value + ")";
    }
}
