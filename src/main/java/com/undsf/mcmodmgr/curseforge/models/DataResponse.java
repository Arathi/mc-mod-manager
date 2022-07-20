package com.undsf.mcmodmgr.curseforge.models;

public class DataResponse<D> {
    public D data;

    public DataResponse() {}

    public DataResponse(D data) {
        this.data = data;
    }
}
