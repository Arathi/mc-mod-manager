package com.undsf.mcmodmgr.curseforge.responses

class PaginationResponse<D>(data: List<D>, var pagination: Pagination) : DataResponse<List<D>>(data) {
    override fun toString(): String {
        return "${pagination.index} ~ ${pagination.index + pagination.resultCount} of ${pagination.totalCount}"
    }
}