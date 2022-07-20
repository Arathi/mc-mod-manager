package com.undsf.mcmodmgr.models;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 语义化版本解析
 */
@Slf4j
public class SemanticVersion implements Comparable<SemanticVersion> {
    public static final Pattern SEMVER_PATTERN = Pattern.compile("(\\d+)(\\.\\d+)?(\\.\\d+)?(-[0-9A-Za-z]+)?");

    public Integer major;

    public Integer minor;

    public Integer patch;

    public String suffix;

    public SemanticVersion(String version) {
        parse(version);
    }

    public SemanticVersion(Integer major, Integer minor, Integer patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public SemanticVersion(Integer major, Integer minor, Integer patch, String suffix) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int getMajor() {
        return major != null ? major : 0;
    }

    public int getMinor() {
        return minor != null ? minor : 0;
    }

    public int getPatch() {
        return patch != null ? patch : 0;
    }

    public String getSuffix() {
        return suffix != null ? suffix : "";
    }

    public boolean parse(String version) {
        Matcher matcher = SEMVER_PATTERN.matcher(version);
        if (matcher.find()) {
            String majorStr = matcher.group(1);
            String minorStr = matcher.group(2);
            String patchStr = matcher.group(3);

            try {
                major = Integer.parseInt(majorStr);

                if (minorStr != null && minorStr.length() > 1) {
                    minor = Integer.parseInt(minorStr.substring(1));
                }

                if (patchStr != null && patchStr.length() > 1) {
                    patch = Integer.parseInt(patchStr.substring(1));
                }
            }
            catch (NumberFormatException ex) {
                log.warn("版本号`{}`解析失败！", version, ex);
                return false;
            }
        }
        else {
            log.warn("不匹配");
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof SemanticVersion o) {
            if (getMajor() == o.getMajor() &&
                    getMinor() == o.getMinor() &&
                    getPatch() == o.getPatch() &&
                    getSuffix().equals(o.getSuffix())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int compareTo(@NotNull SemanticVersion o) {
        int deltaMajor = getMajor() - o.getMajor();
        if (deltaMajor != 0) return deltaMajor;

        int deltaMinor = getMinor() - o.getMinor();
        if (deltaMinor != 0) return deltaMinor;

        int deltaPatch = getPatch() - o.getPatch();
        if (deltaPatch != 0) return deltaPatch;

        return getSuffix().compareTo(o.getSuffix());
    }

    @Override
    public String toString() {
        StringBuilder version = new StringBuilder();

        if (major != null) {
            version.append(major);
        }

        if (minor != null) {
            version.append(".");
            version.append(minor);
        }

        if (patch != null) {
            version.append(".");
            version.append(patch);
        }

        if (suffix != null && !suffix.isEmpty()) {
            version.append("-");
            version.append(suffix);
        }

        return version.toString();
    }
}
