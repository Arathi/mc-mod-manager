package com.undsf.mcmodmgr;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class McModManagerApplicationTests {

    @Test
    void contextLoads() {
        var a1 = List.of(100, 100);
        var a2 = List.of(100, 100);
        System.out.println(a1 == a2);

        Integer i1 = 127;
        Integer i2 = 127;
        System.out.println("i1 == i2 => " + (i1 == i2));
        System.out.println("i1.equals(i2) => " + i1.equals(i2));

        Integer i3 = 128;
        Integer i4 = 128;
        System.out.println("i3 == i4 => " + (i3 == i4));
        System.out.println("i3.equals(i4) => " + i3.equals(i4));
    }

}
