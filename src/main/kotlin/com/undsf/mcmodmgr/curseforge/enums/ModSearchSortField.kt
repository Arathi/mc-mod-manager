package com.undsf.mcmodmgr.curseforge.enums

enum class ModSearchSortField(
        var id: Int,
        var description: String) {
    None(0, "无"),
    Featured(1, "特性"),
    Popularity(2, "热度"),
    LastUpdated(3, "最后更新"),
    Name(4, "名称"),
    Author(5, "作者"),
    TotalDownloads(6, "下载数量"),
    Category(7, "分类"),
    GameVersion(8, "MC版本")
}