package com.undsf.mcmodmgr.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SemanticVersionTests {
    @Test
    public void testParse() {
        SemanticVersion v1 = new SemanticVersion("1");
        SemanticVersion v1_2 = new SemanticVersion("1.2");
        SemanticVersion v1_2_0 = new SemanticVersion("1.2.0");
        SemanticVersion v1_2_3 = new SemanticVersion("1.2.3");

        assertEquals(1, v1.major, "`1`解析主版本与预期不符");
        assertEquals(0, v1.getMinor(), "`1`解析次版本与预期不符");

        assertEquals(2, v1_2.minor, "`1.2`解析次版本与预期不符");

        assertEquals(3, v1_2_3.patch, "`1.2.3`解析修订版本与预期不符");

        assertEquals(v1_2, v1_2_0, "`1.2`应与`1.2.0`相等");
        int c = v1_2_3.compareTo(v1_2);
        assertTrue(c > 0, "`1.2.3应大于1.2`");
    }
}
