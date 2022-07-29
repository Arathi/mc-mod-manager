package com.undsf.mcmodmgr.curseforge.responses

class Pagination(
        var index: Int,
        var pageSize: Int,
        var resultCount: Int,
        var totalCount: Int) {
    fun endIndex() : Int {
        return index + resultCount
    }

    fun hasNextPage() : Boolean {
        return endIndex() < totalCount
    }
}