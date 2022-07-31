package com.undsf.mcmodmgr.util

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jayway.jsonpath.TypeRef
import com.undsf.mcmodmgr.curseforge.enums.FileReleaseType
import com.undsf.mcmodmgr.curseforge.enums.ModLoaderType
import com.undsf.mcmodmgr.curseforge.responses.DataResponse
import com.undsf.mcmodmgr.curseforge.responses.ModAsset
import com.undsf.mcmodmgr.curseforge.responses.Mod
import com.undsf.mcmodmgr.models.ModPack
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

private val log = KotlinLogging.logger {}

@SpringBootTest
class JsonConvertTests {
    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var tpl: JsonTemplate

    @Test
    fun testStringify() {
        val obj = mapOf(
                "code" to 1,
                "message" to "失败",
                "data" to listOf(1, 2, 3)
        )
        var json = mapper.writeValueAsString(obj)
        log.info { "转换后的JSON为：${json}" }

        var modPack = ModPack(
                "s224",
                "0.3.0",
                "1.19",
                "41.0.93",
                "D:\\Temp\\forge-mods"
        )

        json = JSON.stringify(modPack)
        log.info { "ModPack转换后的JSON为：${json}" }
    }

    @Test
    fun testParse() {
        val json = """{
  "name": "s224",
  "version": "0.3.0",
  "mcVersion": "1.19",
  "forgeVersion": "41.0.93",
  "dir": "D:\\Temp\\forge-mods"
}
"""
        val obj = JSON.parse(json, ModPack::class.java)

        assertNotNull(obj, "解析出的对象不应为空")
        assertEquals("s224", obj?.name, "转换出来的name与预期不符")
    }

    @Test
    fun testDateConvert() {
        // 序列化
        var t0 = TestStruct(0, "成功", Date(), LocalDateTime.now())
        var json = mapper.writeValueAsString(t0)
        log.info { "时间结构序列化：${json}" }

        // 反序列化
        json = """{"code":0,"message":"成功","createTime":"2022-07-21 08:52:02","executeTime":"2022-07-21T16:52:02.3637045"}"""
        var t1: TestStruct = mapper.readValue(json)
        assertEquals(363704500, t1.executeTime?.nano)
    }

    // region 大型JSON解析
    @Test
    fun testParseDataResponse() {
        var json = """{
    "data": {
        "id": 245755,
        "gameId": 432,
        "name": "Waystones",
        "slug": "waystones",
        "links": {
            "websiteUrl": "https://www.curseforge.com/minecraft/mc-mods/waystones",
            "wikiUrl": "https://blay09.net/mods/waystones",
            "issuesUrl": "https://github.com/ModdingForBlockheads/Waystones/issues",
            "sourceUrl": "https://github.com/ModdingForBlockheads/Waystones"
        },
        "summary": "Teleport back to activated waystones. For Survival, Adventure or Servers.",
        "status": 4,
        "downloadCount": 67654234,
        "isFeatured": false,
        "primaryCategoryId": 414,
        "categories": [
            {
                "id": 419,
                "gameId": 432,
                "name": "Magic",
                "slug": "magic",
                "url": "https://www.curseforge.com/minecraft/mc-mods/magic",
                "iconUrl": "https://media.forgecdn.net/avatars/6/34/635351496247862494.png",
                "dateModified": "2014-05-08T17:40:24.787Z",
                "isClass": false,
                "classId": 6,
                "parentCategoryId": 6
            },
            {
                "id": 422,
                "gameId": 432,
                "name": "Adventure and RPG",
                "slug": "adventure-rpg",
                "url": "https://www.curseforge.com/minecraft/mc-mods/adventure-rpg",
                "iconUrl": "https://media.forgecdn.net/avatars/6/37/635351497295252123.png",
                "dateModified": "2014-05-08T17:42:09.54Z",
                "isClass": false,
                "classId": 6,
                "parentCategoryId": 6
            },
            {
                "id": 435,
                "gameId": 432,
                "name": "Server Utility",
                "slug": "server-utility",
                "url": "https://www.curseforge.com/minecraft/mc-mods/server-utility",
                "iconUrl": "https://media.forgecdn.net/avatars/6/48/635351498950580836.png",
                "dateModified": "2014-05-08T17:44:55.057Z",
                "isClass": false,
                "classId": 6,
                "parentCategoryId": 6
            },
            {
                "id": 414,
                "gameId": 432,
                "name": "Player Transport",
                "slug": "technology-player-transport",
                "url": "https://www.curseforge.com/minecraft/mc-mods/technology/technology-player-transport",
                "iconUrl": "https://media.forgecdn.net/avatars/6/29/635351495383551178.png",
                "dateModified": "2014-05-08T17:38:58.357Z",
                "isClass": false,
                "classId": 6,
                "parentCategoryId": 412
            }
        ],
        "classId": 6,
        "authors": [
            {
                "id": 40678,
                "name": "BlayTheNinth",
                "url": "https://www.curseforge.com/members/12099681-blaytheninth?username=blaytheninth"
            }
        ],
        "logo": {
            "id": 42418,
            "modId": 245755,
            "title": "636008504954894502.png",
            "description": "",
            "thumbnailUrl": "https://media.forgecdn.net/avatars/thumbnails/42/418/256/256/636008504954894502.png",
            "url": "https://media.forgecdn.net/avatars/42/418/636008504954894502.png"
        },
        "screenshots": [
            {
                "id": 37127,
                "modId": 245755,
                "title": "Optional Teleport Button",
                "description": "",
                "thumbnailUrl": "https://media.forgecdn.net/attachments/thumbnails/37/127/310/172/2016-06-07_10.png",
                "url": "https://media.forgecdn.net/attachments/37/127/2016-06-07_10.png"
            },
            {
                "id": 37126,
                "modId": 245755,
                "title": "Waystone Menu",
                "description": "",
                "thumbnailUrl": "https://media.forgecdn.net/attachments/thumbnails/37/126/310/172/2016-06-07_06.png",
                "url": "https://media.forgecdn.net/attachments/37/126/2016-06-07_06.png"
            },
            {
                "id": 37125,
                "modId": 245755,
                "title": "Waystones",
                "description": "",
                "thumbnailUrl": "https://media.forgecdn.net/attachments/thumbnails/37/125/310/172/screenshotwaystones.png",
                "url": "https://media.forgecdn.net/attachments/37/125/screenshotwaystones.png"
            }
        ],
        "mainFileId": 3835119,
        "latestFiles": [
            {
                "id": 3051110,
                "gameId": 432,
                "modId": 245755,
                "isAvailable": true,
                "displayName": "Waystones 1.16.2-7.2.0 NO WORLDGEN",
                "fileName": "Waystones_1.16.2-7.2.0.jar",
                "releaseType": 2,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "5ef14f7b47a6c0dd1e87d7c580332daa3a4d9927",
                        "algo": 1
                    },
                    {
                        "value": "26d34f3ad00889cc43b7f43321298cbd",
                        "algo": 2
                    }
                ],
                "fileDate": "2020-09-06T18:50:05.887Z",
                "fileLength": 200278,
                "downloadCount": 47092,
                "downloadUrl": "https://edge.forgecdn.net/files/3051/110/Waystones_1.16.2-7.2.0.jar",
                "gameVersions": [
                    "1.16.2"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.16.2",
                        "gameVersionPadded": "0000000001.0000000016.0000000002",
                        "gameVersion": "1.16.2",
                        "gameVersionReleaseDate": "2020-08-11T16:42:21.863Z",
                        "gameVersionTypeId": 70886
                    }
                ],
                "dependencies": [],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 2018409322,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 1882107330
                    },
                    {
                        "name": "net",
                        "fingerprint": 2610296415
                    },
                    {
                        "name": "assets",
                        "fingerprint": 4257522397
                    },
                    {
                        "name": "data",
                        "fingerprint": 479223525
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 1362067209
                    }
                ]
            },
            {
                "id": 3478251,
                "gameId": 432,
                "modId": 245755,
                "isAvailable": true,
                "displayName": "waystones-8.1.3+0.jar",
                "fileName": "waystones-8.1.3+0.jar",
                "releaseType": 2,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "6bb6bb8e4114dd8800596962714da0dd325f353d",
                        "algo": 1
                    },
                    {
                        "value": "b1fdc55c0121efee8fb6caf440ee11f5",
                        "algo": 2
                    }
                ],
                "fileDate": "2021-10-01T19:04:27.627Z",
                "fileLength": 365345,
                "downloadCount": 100353,
                "downloadUrl": "https://edge.forgecdn.net/files/3478/251/waystones-8.1.3+0.jar",
                "gameVersions": [
                    "1.17.1",
                    "Forge"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.17.1",
                        "gameVersionPadded": "0000000001.0000000017.0000000001",
                        "gameVersion": "1.17.1",
                        "gameVersionReleaseDate": "2021-07-06T14:16:03.97Z",
                        "gameVersionTypeId": 73242
                    },
                    {
                        "gameVersionName": "Forge",
                        "gameVersionPadded": "0",
                        "gameVersion": "",
                        "gameVersionReleaseDate": "2019-08-01T00:00:00Z",
                        "gameVersionTypeId": 68441
                    }
                ],
                "dependencies": [
                    {
                        "modId": 531761,
                        "relationType": 3
                    }
                ],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 2764826214,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 2567078589
                    },
                    {
                        "name": "net",
                        "fingerprint": 2328213798
                    },
                    {
                        "name": "assets",
                        "fingerprint": 957087351
                    },
                    {
                        "name": "data",
                        "fingerprint": 3072314050
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 2769305621
                    },
                    {
                        "name": "waystones-icon.png",
                        "fingerprint": 3649261042
                    },
                    {
                        "name": "waystones.mixins.json",
                        "fingerprint": 4080752435
                    },
                    {
                        "name": "waystones.refmap.json",
                        "fingerprint": 3720142189
                    }
                ]
            },
            {
                "id": 3493690,
                "gameId": 432,
                "modId": 245755,
                "isAvailable": true,
                "displayName": "waystones-8.1.4+0.jar",
                "fileName": "waystones-8.1.4+0.jar",
                "releaseType": 1,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "c05a6d4fffc1eb5b92113871e387b82e0c46df44",
                        "algo": 1
                    },
                    {
                        "value": "f0763865463868f7d028344b7cc409a4",
                        "algo": 2
                    }
                ],
                "fileDate": "2021-10-17T08:10:44.47Z",
                "fileLength": 365345,
                "downloadCount": 119953,
                "downloadUrl": "https://edge.forgecdn.net/files/3493/690/waystones-8.1.4+0.jar",
                "gameVersions": [
                    "1.17.1"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.17.1",
                        "gameVersionPadded": "0000000001.0000000017.0000000001",
                        "gameVersion": "1.17.1",
                        "gameVersionReleaseDate": "2021-07-06T14:16:03.97Z",
                        "gameVersionTypeId": 73242
                    }
                ],
                "dependencies": [
                    {
                        "modId": 531761,
                        "relationType": 3
                    }
                ],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 4096900498,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 1953396316
                    },
                    {
                        "name": "net",
                        "fingerprint": 2328213798
                    },
                    {
                        "name": "assets",
                        "fingerprint": 957087351
                    },
                    {
                        "name": "data",
                        "fingerprint": 3072314050
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 2769305621
                    },
                    {
                        "name": "waystones-icon.png",
                        "fingerprint": 3649261042
                    },
                    {
                        "name": "waystones.mixins.json",
                        "fingerprint": 4080752435
                    },
                    {
                        "name": "waystones.refmap.json",
                        "fingerprint": 3720142189
                    }
                ]
            },
            {
                "id": 3835119,
                "gameId": 432,
                "modId": 245755,
                "isAvailable": true,
                "displayName": "waystones-forge-1.19-11.0.0.jar",
                "fileName": "waystones-forge-1.19-11.0.0.jar",
                "releaseType": 1,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "679b3c87f9145400ffb7fa81b2f918443cba72df",
                        "algo": 1
                    },
                    {
                        "value": "591352d7c666d2607ad4c473bb48ad10",
                        "algo": 2
                    }
                ],
                "fileDate": "2022-06-16T20:25:04.053Z",
                "fileLength": 359351,
                "downloadCount": 0,
                "downloadUrl": "https://edge.forgecdn.net/files/3835/119/waystones-forge-1.19-11.0.0.jar",
                "gameVersions": [
                    "Forge",
                    "1.19"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "Forge",
                        "gameVersionPadded": "0",
                        "gameVersion": "",
                        "gameVersionReleaseDate": "2019-08-01T00:00:00Z",
                        "gameVersionTypeId": 68441
                    },
                    {
                        "gameVersionName": "1.19",
                        "gameVersionPadded": "0000000001.0000000019",
                        "gameVersion": "1.19",
                        "gameVersionReleaseDate": "2022-06-07T15:38:07.377Z",
                        "gameVersionTypeId": 73407
                    }
                ],
                "dependencies": [
                    {
                        "modId": 531761,
                        "relationType": 3
                    }
                ],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 2580803773,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 2731717902
                    },
                    {
                        "name": "net",
                        "fingerprint": 2705334492
                    },
                    {
                        "name": "assets",
                        "fingerprint": 1478786455
                    },
                    {
                        "name": "data",
                        "fingerprint": 1311553820
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 2905512523
                    },
                    {
                        "name": "waystones-icon.png",
                        "fingerprint": 3621685521
                    },
                    {
                        "name": "waystones.mixins.json",
                        "fingerprint": 2087209328
                    },
                    {
                        "name": "waystones.refmap.json",
                        "fingerprint": 2378598686
                    }
                ]
            }
        ],
        "latestFilesIndexes": [
            {
                "gameVersion": "1.19",
                "fileId": 3835119,
                "filename": "waystones-forge-1.19-11.0.0.jar",
                "releaseType": 1,
                "gameVersionTypeId": 73407,
                "modLoader": 1
            },
            {
                "gameVersion": "1.18.2",
                "fileId": 3830849,
                "filename": "waystones-forge-1.18.2-10.1.0.jar",
                "releaseType": 1,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.18.1",
                "fileId": 3658046,
                "filename": "waystones-forge-1.18.1-9.0.4.jar",
                "releaseType": 1,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.18",
                "fileId": 3548808,
                "filename": "waystones-forge-1.18-9.0.0.jar",
                "releaseType": 1,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.17.1",
                "fileId": 3515761,
                "filename": "waystones-8.2.0+0.jar",
                "releaseType": 1,
                "gameVersionTypeId": 73242,
                "modLoader": 1
            },
            {
                "gameVersion": "1.16.5",
                "fileId": 3515707,
                "filename": "Waystones_1.16.5-7.6.4.jar",
                "releaseType": 1,
                "gameVersionTypeId": 70886,
                "modLoader": 1
            },
            {
                "gameVersion": "1.17.1",
                "fileId": 3493690,
                "filename": "waystones-8.1.4+0.jar",
                "releaseType": 1,
                "gameVersionTypeId": 73242,
                "modLoader": null
            },
            {
                "gameVersion": "1.17.1",
                "fileId": 3478251,
                "filename": "waystones-8.1.3+0.jar",
                "releaseType": 2,
                "gameVersionTypeId": 73242,
                "modLoader": 1
            },
            {
                "gameVersion": "1.16.5",
                "fileId": 3332276,
                "filename": "Waystones_1.16.5-7.6.2.jar",
                "releaseType": 1,
                "gameVersionTypeId": 70886,
                "modLoader": null
            },
            {
                "gameVersion": "1.16.3",
                "fileId": 3222129,
                "filename": "Waystones_1.16.5-7.4.0.jar",
                "releaseType": 1,
                "gameVersionTypeId": 70886,
                "modLoader": null
            },
            {
                "gameVersion": "1.16.4",
                "fileId": 3222129,
                "filename": "Waystones_1.16.5-7.4.0.jar",
                "releaseType": 1,
                "gameVersionTypeId": 70886,
                "modLoader": null
            },
            {
                "gameVersion": "1.16.2",
                "fileId": 3051110,
                "filename": "Waystones_1.16.2-7.2.0.jar",
                "releaseType": 2,
                "gameVersionTypeId": 70886,
                "modLoader": null
            },
            {
                "gameVersion": "1.16.1",
                "fileId": 3035119,
                "filename": "Waystones_1.16.1-7.1.1.jar",
                "releaseType": 2,
                "gameVersionTypeId": 70886,
                "modLoader": null
            },
            {
                "gameVersion": "1.15.2",
                "fileId": 2991235,
                "filename": "Waystones_1.15.2-6.0.2.jar",
                "releaseType": 1,
                "gameVersionTypeId": 68722,
                "modLoader": null
            },
            {
                "gameVersion": "1.15.2",
                "fileId": 2903834,
                "filename": "Waystones_1.15.2-6.0.1.jar",
                "releaseType": 2,
                "gameVersionTypeId": 68722,
                "modLoader": null
            },
            {
                "gameVersion": "1.14.4",
                "fileId": 2872947,
                "filename": "Waystones_1.14.4-5.1.1.jar",
                "releaseType": 2,
                "gameVersionTypeId": 64806,
                "modLoader": null
            },
            {
                "gameVersion": "1.12.2",
                "fileId": 2859589,
                "filename": "Waystones_1.12.2-4.1.0.jar",
                "releaseType": 1,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.7.10",
                "fileId": 2559125,
                "filename": "Waystones-mc1.7.10-1.0.12.jar",
                "releaseType": 1,
                "gameVersionTypeId": 5,
                "modLoader": null
            },
            {
                "gameVersion": "1.12",
                "fileId": 2489635,
                "filename": "Waystones_1.12.1-4.0.17.jar",
                "releaseType": 1,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.12.1",
                "fileId": 2464463,
                "filename": "Waystones_1.12.1-4.0.15.jar",
                "releaseType": 1,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.11.2",
                "fileId": 2457030,
                "filename": "Waystones_1.11.2-3.0.17.jar",
                "releaseType": 1,
                "gameVersionTypeId": 599,
                "modLoader": null
            },
            {
                "gameVersion": "1.10.2",
                "fileId": 2457029,
                "filename": "Waystones_1.10.2-2.0.13.jar",
                "releaseType": 1,
                "gameVersionTypeId": 572,
                "modLoader": null
            },
            {
                "gameVersion": "1.11",
                "fileId": 2382350,
                "filename": "Waystones_1.11.2-3.0.10.jar",
                "releaseType": 1,
                "gameVersionTypeId": 599,
                "modLoader": null
            },
            {
                "gameVersion": "1.10",
                "fileId": 2326369,
                "filename": "Waystones_1.10.2-2.0.4.jar",
                "releaseType": 1,
                "gameVersionTypeId": 572,
                "modLoader": null
            },
            {
                "gameVersion": "1.10.1",
                "fileId": 2326330,
                "filename": "Waystones_1.10.2-2.0.3.jar",
                "releaseType": 1,
                "gameVersionTypeId": 572,
                "modLoader": null
            }
        ],
        "dateCreated": "2016-06-02T23:55:20.133Z",
        "dateModified": "2022-06-16T20:28:06.873Z",
        "dateReleased": "2022-06-16T20:25:04.053Z",
        "allowModDistribution": true,
        "gamePopularityRank": 9,
        "isAvailable": true,
        "thumbsUpCount": 0
    }
}"""
        // val resp = mapper.readValue<DataResponse<Mod>>(json)
        // val resp = tpl.parsel(json, DataResponse::class.java, Mod::class.java)
        val resp = tpl.parser(json, object: TypeReference<DataResponse<Mod>>() {})
        println(resp)
    }
    // endregion

    @Test
    fun testParseImage() {
        var json = """{
      "id": 29069,
      "modId": 238222,
      "title": "635838945588716414.jpeg",
      "description": "",
      "thumbnailUrl": "https://media.forgecdn.net/avatars/thumbnails/29/69/256/256/635838945588716414.jpeg",
      "url": "https://media.forgecdn.net/avatars/29/69/635838945588716414.jpeg"
    }"""
        val assrt = mapper.readValue<ModAsset>(json)
        println(assrt)
    }

    @Test
    fun testEnumConvert() {
        var json = """{
    "gameVersion": "1.19",
    "fileId": 3835119,
    "filename": "waystones-forge-1.19-11.0.0.jar",
    "releaseType": 1,
    "gameVersionTypeId": 73407,
    "modLoader": 1
}"""
        var fileIndex = JSON.parse(json, FileIndexNg::class.java)
        assertNotNull(fileIndex, "转换出的FileIndex不能为null")

        if (fileIndex != null) {
            println(fileIndex)
            println(JSON.stringify(fileIndex))
        }
    }
}

data class TestStruct(
        @JsonProperty
        var code: Int,

        @JsonProperty
        var message: String?,

        // 传统时间
        @JsonProperty
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        var createTime: Date?,

        // JSR310时间
        @JsonProperty
        var executeTime: LocalDateTime?
)

data class FileIndexNg(
        var gameVersion: String,
        var fileId: Int,
        var filename: String,
        var releaseType: FileReleaseType,
        var gameVersionTypeId: Int,
        var modLoader: ModLoaderType
) {
    override fun toString(): String {
        val releaseTypeName = releaseType.description
        val modLoaderName = modLoader.name
        return "$fileId: [$releaseTypeName] ${gameVersion}-${modLoaderName} $filename"
    }
}
