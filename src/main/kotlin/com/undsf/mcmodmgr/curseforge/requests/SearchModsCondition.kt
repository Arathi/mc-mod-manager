package com.undsf.mcmodmgr.curseforge.requests

import com.undsf.mcmodmgr.curseforge.enums.ModLoaderType
import com.undsf.mcmodmgr.curseforge.enums.ModSearchSortField
import okhttp3.FormBody

data class SearchModsCondition(
        /**
         * 大类
         */
        val classId: Int? = null,

        /**
         * 小类
         */
        val categoryId: Int? = null,

        /**
         * MC版本
         *
         * 1.7.10
         * 1.12.2
         * 1.16.5
         * 1.19
         */
        val gameVersion: String? = null,

        /**
         * MOD名称，作者名字等
         */
        val searchFilter: String? = null,

        /**
         * 排序字段
         */
        val sortField: ModSearchSortField? = null,

        /**
         * 顺序
         *
         * asc = 正序
         * desc = 倒序
         */
        val sortOrder: String? = null,

        /**
         * MOD加载器类型
         */
        val modLoaderType: ModLoaderType? = null,

        /**
         * 游戏版本类型
         */
        val gameVersionTypeId: Int? = null,

        /**
         * 根据简写搜索
         */
        val slug: String? = null
) {
    private final val gameId: Int = 432

    fun buildForm(form: FormBody.Builder) {
        // 必填字段，固定值为MC游戏编号432
        form.add("gameId", gameId.toString())

        // 选填字段
        if (classId != null) form.add("classId", "$classId")
        if (categoryId != null) form.add("categoryId", "$categoryId")
        if (gameVersion != null) form.add("gameVersion", gameVersion)
        if (searchFilter != null) form.add("searchFilter", searchFilter)
        if (sortField != null) form.add("sortField", sortField.ordinal.toString())
        if (sortOrder != null) form.add("sortOrder", sortOrder)
        if (modLoaderType != null) form.add("sortOrder", modLoaderType.ordinal.toString())
        if (gameVersionTypeId != null) form.add("gameVersionTypeId", "$gameVersionTypeId")
        if (slug != null) form.add("slug", slug)
    }
}