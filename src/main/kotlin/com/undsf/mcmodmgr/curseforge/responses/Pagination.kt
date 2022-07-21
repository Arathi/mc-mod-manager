package com.undsf.mcmodmgr.curseforge.responses

class Pagination(
        var index: Int,
        var pageSize: Int,
        var resultCount: Int,
        var totalCount: Int) {
}