package com.undsf.mcmodmgr.models;

/**
 * 版本区间
 */
public class VersionInterval {
    public boolean containStart;
    public SemanticVersion startVersion;

    public boolean containEnd;
    public SemanticVersion endVersion;
}
