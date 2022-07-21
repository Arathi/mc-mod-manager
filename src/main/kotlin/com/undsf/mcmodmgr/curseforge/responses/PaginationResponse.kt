package com.undsf.mcmodmgr.curseforge.responses

class PaginationResponse<D>(
        data: List<D>,
        var pagination: Pagination) : DataResponse<List<D>>(data) {
}