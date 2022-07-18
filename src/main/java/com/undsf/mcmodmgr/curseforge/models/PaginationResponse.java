package com.undsf.mcmodmgr.curseforge.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PaginationResponse<D> {
    @JsonProperty
    public List<D> data;

    @JsonProperty
    public Pagination pagination;
}
