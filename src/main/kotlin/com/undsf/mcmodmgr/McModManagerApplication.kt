package com.undsf.mcmodmgr

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan("com.undsf.mcmodmgr.mappers")
class McModManagerApplication

fun main(args: Array<String>) {
    runApplication<McModManagerApplication>(*args)
}
