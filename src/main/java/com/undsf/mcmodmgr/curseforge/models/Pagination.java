package com.undsf.mcmodmgr.curseforge.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pagination {
    @JsonProperty
    public int index;

    @JsonProperty
    public int pageSize;

    @JsonProperty
    public int resultCount;

    @JsonProperty
    public int totalCount;
}
