package com.undsf.mcmodmgr.util

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.undsf.mcmodmgr.curseforge.responses.DataResponse
import com.undsf.mcmodmgr.curseforge.responses.Image
import com.undsf.mcmodmgr.curseforge.responses.Mod
import com.undsf.mcmodmgr.models.ModPack
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
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
        modPack.loadMods(listOf())

        json = mapper.writeValueAsString(modPack)
        log.info { "ModPack转换后的JSON为：${json}" }
    }

    @Test
    fun testParse() {
        val json = """{
  "name": "s224",
  "version": "0.3.0",
  "mcVersion": "1.19",
  "forgeVersion": "41.0.93",
  "dir": "D:\\Temp\\forge-mods",
  "mods": {
    "minecraft": {
      "version": "1.19",
      "modId": "minecraft"
    },
    "forge": {
      "version": "41.0.93",
      "modId": "forge"
    }
  }
}
"""
        val obj: ModPack = mapper.readValue(json)
        assertEquals("s224", obj.name, "转换出来的name与预期不符")
        assertEquals(2, obj.mods.size, "获取到的mod数量与预期不符")
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

    @Test
    fun testAnyGetterSetter() {
        var modJson = """{
    "data": {
        "id": 238222,
        "gameId": 432,
        "name": "Just Enough Items (JEI)",
        "slug": "jei",
        "links": {
            "websiteUrl": "https://www.curseforge.com/minecraft/mc-mods/jei",
            "wikiUrl": "",
            "issuesUrl": "https://github.com/mezz/JustEnoughItems/issues?q=is%3Aissue",
            "sourceUrl": "https://github.com/mezz/JustEnoughItems"
        },
        "summary": "View Items and Recipes",
        "status": 4,
        "downloadCount": 178536045,
        "isFeatured": false,
        "primaryCategoryId": 423,
        "categories": [
            {
                "id": 423,
                "gameId": 432,
                "name": "Map and Information",
                "slug": "map-information",
                "url": "https://www.curseforge.com/minecraft/mc-mods/map-information",
                "iconUrl": "https://media.forgecdn.net/avatars/6/38/635351497437388438.png",
                "dateModified": "2014-05-08T17:42:23.74Z",
                "isClass": false,
                "classId": 6,
                "parentCategoryId": 6
            },
            {
                "id": 421,
                "gameId": 432,
                "name": "API and Library",
                "slug": "library-api",
                "url": "https://www.curseforge.com/minecraft/mc-mods/library-api",
                "iconUrl": "https://media.forgecdn.net/avatars/6/36/635351496947765531.png",
                "dateModified": "2014-05-23T03:21:44.06Z",
                "isClass": false,
                "classId": 6,
                "parentCategoryId": 6
            }
        ],
        "classId": 6,
        "authors": [
            {
                "id": 32358,
                "name": "mezz",
                "url": "https://www.curseforge.com/members/17072262-mezz?username=mezz"
            }
        ],
        "logo": {
            "id": 29069,
            "modId": 238222,
            "title": "635838945588716414.jpeg",
            "description": "",
            "thumbnailUrl": "https://media.forgecdn.net/avatars/thumbnails/29/69/256/256/635838945588716414.jpeg",
            "url": "https://media.forgecdn.net/avatars/29/69/635838945588716414.jpeg"
        },
        "screenshots": [
            {
                "id": 31417,
                "modId": 238222,
                "title": "Recipe Completion",
                "description": "",
                "thumbnailUrl": "https://media.forgecdn.net/attachments/thumbnails/31/417/310/172/thzzdin.png",
                "url": "https://media.forgecdn.net/attachments/31/417/thzzdin.png"
            },
            {
                "id": 31419,
                "modId": 238222,
                "title": "Potions",
                "description": "",
                "thumbnailUrl": "https://media.forgecdn.net/attachments/thumbnails/31/419/310/172/t7f7jh6.png",
                "url": "https://media.forgecdn.net/attachments/31/419/t7f7jh6.png"
            },
            {
                "id": 31420,
                "modId": 238222,
                "title": "Itemlist Edit Mode",
                "description": "",
                "thumbnailUrl": "https://media.forgecdn.net/attachments/thumbnails/31/420/310/172/tgafkma.png",
                "url": "https://media.forgecdn.net/attachments/31/420/tgafkma.png"
            },
            {
                "id": 31418,
                "modId": 238222,
                "title": "Big Screen Support",
                "description": "",
                "thumbnailUrl": "https://media.forgecdn.net/attachments/thumbnails/31/418/310/172/9lngh5f.png",
                "url": "https://media.forgecdn.net/attachments/31/418/9lngh5f.png"
            }
        ],
        "mainFileId": 3847103,
        "latestFiles": [
            {
                "id": 3040523,
                "gameId": 432,
                "modId": 238222,
                "isAvailable": true,
                "displayName": "jei_1.12.2-4.16.1.301.jar",
                "fileName": "jei_1.12.2-4.16.1.301.jar",
                "releaseType": 1,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "3045e8440ea44071d8b83c4e7b3c190348fdc527",
                        "algo": 1
                    },
                    {
                        "value": "1dee4be93d666e2228039c551e927b35",
                        "algo": 2
                    }
                ],
                "fileDate": "2020-08-24T01:01:39.123Z",
                "fileLength": 653211,
                "downloadCount": 11752168,
                "downloadUrl": "https://edge.forgecdn.net/files/3040/523/jei_1.12.2-4.16.1.301.jar",
                "gameVersions": [
                    "1.12.2"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.12.2",
                        "gameVersionPadded": "0000000001.0000000012.0000000002",
                        "gameVersion": "1.12.2",
                        "gameVersionReleaseDate": "2017-09-18T05:00:00Z",
                        "gameVersionTypeId": 628
                    }
                ],
                "dependencies": [],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 3089143260,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 2236405288
                    },
                    {
                        "name": "mezz",
                        "fingerprint": 2222830911
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 1488642189
                    },
                    {
                        "name": "mcmod.info",
                        "fingerprint": 3528499262
                    },
                    {
                        "name": "assets",
                        "fingerprint": 9943101
                    }
                ]
            },
            {
                "id": 3043174,
                "gameId": 432,
                "modId": 238222,
                "isAvailable": true,
                "displayName": "jei_1.12.2-4.16.1.302.jar",
                "fileName": "jei_1.12.2-4.16.1.302.jar",
                "releaseType": 2,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "3e88d2896ca868c3cedb65e117ad3a1b82488fa8",
                        "algo": 1
                    },
                    {
                        "value": "2dc4b6046812e64c514ff4d18bc5cb03",
                        "algo": 2
                    }
                ],
                "fileDate": "2020-08-27T01:47:46.263Z",
                "fileLength": 653210,
                "downloadCount": 6775770,
                "downloadUrl": "https://edge.forgecdn.net/files/3043/174/jei_1.12.2-4.16.1.302.jar",
                "gameVersions": [
                    "1.12.2"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.12.2",
                        "gameVersionPadded": "0000000001.0000000012.0000000002",
                        "gameVersion": "1.12.2",
                        "gameVersionReleaseDate": "2017-09-18T05:00:00Z",
                        "gameVersionTypeId": 628
                    }
                ],
                "dependencies": [],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 1065578237,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 457918307
                    },
                    {
                        "name": "mezz",
                        "fingerprint": 2799571216
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 1488642189
                    },
                    {
                        "name": "mcmod.info",
                        "fingerprint": 3001924313
                    },
                    {
                        "name": "assets",
                        "fingerprint": 9943101
                    }
                ]
            },
            {
                "id": 3272039,
                "gameId": 432,
                "modId": 238222,
                "isAvailable": true,
                "displayName": "jei-1.13.2-5.0.0.31.jar",
                "fileName": "jei-1.13.2-5.0.0.31.jar",
                "releaseType": 3,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "aa15cdea079db8b91d75e3c68216df80a70545d8",
                        "algo": 1
                    },
                    {
                        "value": "1ee1f4fb4c6e199c02c7d15cbd0d2c8a",
                        "algo": 2
                    }
                ],
                "fileDate": "2021-04-11T03:49:47.687Z",
                "fileLength": 690802,
                "downloadCount": 8644,
                "downloadUrl": "https://edge.forgecdn.net/files/3272/39/jei-1.13.2-5.0.0.31.jar",
                "gameVersions": [
                    "1.13.2"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.13.2",
                        "gameVersionPadded": "0000000001.0000000013.0000000002",
                        "gameVersion": "1.13.2",
                        "gameVersionReleaseDate": "2018-10-22T00:00:00Z",
                        "gameVersionTypeId": 55023
                    }
                ],
                "dependencies": [],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 2700304635,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 1102858494
                    },
                    {
                        "name": "mezz",
                        "fingerprint": 2811918946
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 3652707984
                    },
                    {
                        "name": "assets",
                        "fingerprint": 88833534
                    }
                ]
            },
            {
                "id": 3847103,
                "gameId": 432,
                "modId": 238222,
                "isAvailable": true,
                "displayName": "jei-1.18.2-9.7.0.209.jar",
                "fileName": "jei-1.18.2-9.7.0.209.jar",
                "releaseType": 1,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "427cc2cf50f6a654fc2677bafe9988dad377fe86",
                        "algo": 1
                    },
                    {
                        "value": "f3a5f9de7ebd81f414e8efee622202c1",
                        "algo": 2
                    }
                ],
                "fileDate": "2022-06-25T04:20:30.257Z",
                "fileLength": 993424,
                "downloadCount": 28,
                "downloadUrl": "https://edge.forgecdn.net/files/3847/103/jei-1.18.2-9.7.0.209.jar",
                "gameVersions": [
                    "1.18.2",
                    "Forge"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.18.2",
                        "gameVersionPadded": "0000000001.0000000018.0000000002",
                        "gameVersion": "1.18.2",
                        "gameVersionReleaseDate": "2022-02-28T14:23:37.723Z",
                        "gameVersionTypeId": 73250
                    },
                    {
                        "gameVersionName": "Forge",
                        "gameVersionPadded": "0",
                        "gameVersion": "",
                        "gameVersionReleaseDate": "2019-08-01T00:00:00Z",
                        "gameVersionTypeId": 68441
                    }
                ],
                "dependencies": [],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 2451910159,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 2359114732
                    },
                    {
                        "name": "mezz",
                        "fingerprint": 3492014957
                    },
                    {
                        "name": "assets",
                        "fingerprint": 3451630566
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 1550930300
                    }
                ]
            },
            {
                "id": 3885881,
                "gameId": 432,
                "modId": 238222,
                "isAvailable": true,
                "displayName": "jei-1.18.2-fabric-10.1.1.231.jar",
                "fileName": "jei-1.18.2-fabric-10.1.1.231.jar",
                "releaseType": 3,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "fd91782acfe6470470a0f1c967690cb1d2ed7796",
                        "algo": 1
                    },
                    {
                        "value": "8f6993541629e2d17ee423f26d341e9e",
                        "algo": 2
                    }
                ],
                "fileDate": "2022-07-20T04:12:21.72Z",
                "fileLength": 1093310,
                "downloadCount": 0,
                "downloadUrl": "https://edge.forgecdn.net/files/3885/881/jei-1.18.2-fabric-10.1.1.231.jar",
                "gameVersions": [
                    "Fabric",
                    "1.18.2"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "Fabric",
                        "gameVersionPadded": "0",
                        "gameVersion": "",
                        "gameVersionReleaseDate": "2019-08-01T00:00:00Z",
                        "gameVersionTypeId": 68441
                    },
                    {
                        "gameVersionName": "1.18.2",
                        "gameVersionPadded": "0000000001.0000000018.0000000002",
                        "gameVersion": "1.18.2",
                        "gameVersionReleaseDate": "2022-02-28T14:23:37.723Z",
                        "gameVersionTypeId": 73250
                    }
                ],
                "dependencies": [],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 272844195,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 4017640229
                    },
                    {
                        "name": "jei.accesswidener",
                        "fingerprint": 1801728810
                    },
                    {
                        "name": "assets",
                        "fingerprint": 628780493
                    },
                    {
                        "name": "fabric.mod.json",
                        "fingerprint": 3916251286
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 1550930300
                    },
                    {
                        "name": "jei.mixins.json",
                        "fingerprint": 1759688962
                    },
                    {
                        "name": "jei-1.18.2-fabric-refmap.json",
                        "fingerprint": 1412800118
                    },
                    {
                        "name": "mezz",
                        "fingerprint": 254238903
                    }
                ]
            },
            {
                "id": 3885884,
                "gameId": 432,
                "modId": 238222,
                "isAvailable": true,
                "displayName": "jei-1.18.2-forge-10.1.1.231.jar",
                "fileName": "jei-1.18.2-forge-10.1.1.231.jar",
                "releaseType": 3,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "12cae555f4e286c6b98ad8c4dba9fb6ddc579802",
                        "algo": 1
                    },
                    {
                        "value": "3e91c9010d88ee1e6e154dfc35b1d819",
                        "algo": 2
                    }
                ],
                "fileDate": "2022-07-20T04:12:49.12Z",
                "fileLength": 1074665,
                "downloadCount": 0,
                "downloadUrl": "https://edge.forgecdn.net/files/3885/884/jei-1.18.2-forge-10.1.1.231.jar",
                "gameVersions": [
                    "1.18.2",
                    "Forge"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.18.2",
                        "gameVersionPadded": "0000000001.0000000018.0000000002",
                        "gameVersion": "1.18.2",
                        "gameVersionReleaseDate": "2022-02-28T14:23:37.723Z",
                        "gameVersionTypeId": 73250
                    },
                    {
                        "gameVersionName": "Forge",
                        "gameVersionPadded": "0",
                        "gameVersion": "",
                        "gameVersionReleaseDate": "2019-08-01T00:00:00Z",
                        "gameVersionTypeId": 68441
                    }
                ],
                "dependencies": [],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 3926512666,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 3558321083
                    },
                    {
                        "name": "mezz",
                        "fingerprint": 2026175432
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 1550930300
                    },
                    {
                        "name": "assets",
                        "fingerprint": 628780493
                    }
                ]
            },
            {
                "id": 3885885,
                "gameId": 432,
                "modId": 238222,
                "isAvailable": true,
                "displayName": "jei-1.18.2-9.7.1.232.jar",
                "fileName": "jei-1.18.2-9.7.1.232.jar",
                "releaseType": 2,
                "fileStatus": 4,
                "hashes": [
                    {
                        "value": "f6a680da25eab744f0e681e8b33e38175f34c9be",
                        "algo": 1
                    },
                    {
                        "value": "05acfaf71fb0acd66660fa9813b9c7af",
                        "algo": 2
                    }
                ],
                "fileDate": "2022-07-20T04:15:42.63Z",
                "fileLength": 993919,
                "downloadCount": 0,
                "downloadUrl": "https://edge.forgecdn.net/files/3885/885/jei-1.18.2-9.7.1.232.jar",
                "gameVersions": [
                    "1.18.2",
                    "Forge"
                ],
                "sortableGameVersions": [
                    {
                        "gameVersionName": "1.18.2",
                        "gameVersionPadded": "0000000001.0000000018.0000000002",
                        "gameVersion": "1.18.2",
                        "gameVersionReleaseDate": "2022-02-28T14:23:37.723Z",
                        "gameVersionTypeId": 73250
                    },
                    {
                        "gameVersionName": "Forge",
                        "gameVersionPadded": "0",
                        "gameVersion": "",
                        "gameVersionReleaseDate": "2019-08-01T00:00:00Z",
                        "gameVersionTypeId": 68441
                    }
                ],
                "dependencies": [],
                "alternateFileId": 0,
                "isServerPack": false,
                "fileFingerprint": 2452098898,
                "modules": [
                    {
                        "name": "META-INF",
                        "fingerprint": 2035767613
                    },
                    {
                        "name": "mezz",
                        "fingerprint": 2927237128
                    },
                    {
                        "name": "assets",
                        "fingerprint": 3451630566
                    },
                    {
                        "name": "pack.mcmeta",
                        "fingerprint": 1550930300
                    }
                ]
            }
        ],
        "latestFilesIndexes": [
            {
                "gameVersion": "1.18.2",
                "fileId": 3885885,
                "filename": "jei-1.18.2-9.7.1.232.jar",
                "releaseType": 2,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.18.2",
                "fileId": 3885884,
                "filename": "jei-1.18.2-forge-10.1.1.231.jar",
                "releaseType": 3,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.18.2",
                "fileId": 3885881,
                "filename": "jei-1.18.2-fabric-10.1.1.231.jar",
                "releaseType": 3,
                "gameVersionTypeId": 73250,
                "modLoader": 4
            },
            {
                "gameVersion": "1.19",
                "fileId": 3884337,
                "filename": "jei-1.19-forge-11.0.2.230.jar",
                "releaseType": 3,
                "gameVersionTypeId": 73407,
                "modLoader": 1
            },
            {
                "gameVersion": "1.19",
                "fileId": 3884336,
                "filename": "jei-1.19-fabric-11.0.2.230.jar",
                "releaseType": 3,
                "gameVersionTypeId": 73407,
                "modLoader": 4
            },
            {
                "gameVersion": "1.18.2",
                "fileId": 3847103,
                "filename": "jei-1.18.2-9.7.0.209.jar",
                "releaseType": 1,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.18.1",
                "fileId": 3723162,
                "filename": "jei-1.18.1-9.4.1.172.jar",
                "releaseType": 1,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.18.1",
                "fileId": 3712260,
                "filename": "jei-1.18.1-9.4.1.168.jar",
                "releaseType": 2,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.16.5",
                "fileId": 3681294,
                "filename": "jei-1.16.5-7.7.1.152.jar",
                "releaseType": 2,
                "gameVersionTypeId": 70886,
                "modLoader": 1
            },
            {
                "gameVersion": "1.17.1",
                "fileId": 3596034,
                "filename": "jei-1.17.1-8.3.1.62.jar",
                "releaseType": 2,
                "gameVersionTypeId": 73242,
                "modLoader": 1
            },
            {
                "gameVersion": "1.18",
                "fileId": 3550020,
                "filename": "jei-1.18-9.0.0.40.jar",
                "releaseType": 2,
                "gameVersionTypeId": 73250,
                "modLoader": 1
            },
            {
                "gameVersion": "1.13.2",
                "fileId": 3272039,
                "filename": "jei-1.13.2-5.0.0.31.jar",
                "releaseType": 3,
                "gameVersionTypeId": 55023,
                "modLoader": null
            },
            {
                "gameVersion": "1.15.2",
                "fileId": 3272032,
                "filename": "jei-1.15.2-6.0.3.16.jar",
                "releaseType": 3,
                "gameVersionTypeId": 68722,
                "modLoader": null
            },
            {
                "gameVersion": "1.16.4",
                "fileId": 3245003,
                "filename": "jei-1.16.4-7.6.1.74.jar",
                "releaseType": 2,
                "gameVersionTypeId": 70886,
                "modLoader": 1
            },
            {
                "gameVersion": "1.16.3",
                "fileId": 3104018,
                "filename": "jei-1.16.3-7.6.0.51.jar",
                "releaseType": 2,
                "gameVersionTypeId": 70886,
                "modLoader": 1
            },
            {
                "gameVersion": "1.16.3",
                "fileId": 3071356,
                "filename": "jei-1.16.3-7.4.0.40.jar",
                "releaseType": 3,
                "gameVersionTypeId": 70886,
                "modLoader": 1
            },
            {
                "gameVersion": "1.16.2",
                "fileId": 3060935,
                "filename": "jei-1.16.2-7.3.2.28.jar",
                "releaseType": 3,
                "gameVersionTypeId": 70886,
                "modLoader": 1
            },
            {
                "gameVersion": "1.12.2",
                "fileId": 3043174,
                "filename": "jei_1.12.2-4.16.1.302.jar",
                "releaseType": 2,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.12.2",
                "fileId": 3040523,
                "filename": "jei_1.12.2-4.16.1.301.jar",
                "releaseType": 1,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.14.4",
                "fileId": 3039707,
                "filename": "jei-1.14.4-6.0.1.30.jar",
                "releaseType": 3,
                "gameVersionTypeId": 64806,
                "modLoader": null
            },
            {
                "gameVersion": "1.16.1",
                "fileId": 3028697,
                "filename": "jei-1.16.1-7.0.1.10.jar",
                "releaseType": 3,
                "gameVersionTypeId": 70886,
                "modLoader": 1
            },
            {
                "gameVersion": "1.15.1",
                "fileId": 2855456,
                "filename": "jei-1.15.1-6.0.0.1.jar",
                "releaseType": 3,
                "gameVersionTypeId": 68722,
                "modLoader": null
            },
            {
                "gameVersion": "1.14.3",
                "fileId": 2738328,
                "filename": "jei-1.14.3-6.0.0.8.jar",
                "releaseType": 3,
                "gameVersionTypeId": 64806,
                "modLoader": null
            },
            {
                "gameVersion": "1.14.2",
                "fileId": 2733474,
                "filename": "jei-1.14.2-6.0.0.3.jar",
                "releaseType": 3,
                "gameVersionTypeId": 64806,
                "modLoader": null
            },
            {
                "gameVersion": "1.10.2",
                "fileId": 2561516,
                "filename": "jei_1.10.2-3.14.8.422.jar",
                "releaseType": 2,
                "gameVersionTypeId": 572,
                "modLoader": null
            },
            {
                "gameVersion": "1.12",
                "fileId": 2485363,
                "filename": "jei_1.12.2-4.7.11.102.jar",
                "releaseType": 2,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.12.1",
                "fileId": 2485363,
                "filename": "jei_1.12.2-4.7.11.102.jar",
                "releaseType": 2,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.12",
                "fileId": 2478647,
                "filename": "jei_1.12.1-4.7.8.95.jar",
                "releaseType": 1,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.12.1",
                "fileId": 2478647,
                "filename": "jei_1.12.1-4.7.8.95.jar",
                "releaseType": 1,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.11.2",
                "fileId": 2461378,
                "filename": "jei_1.11.2-4.5.1.296.jar",
                "releaseType": 2,
                "gameVersionTypeId": 599,
                "modLoader": null
            },
            {
                "gameVersion": "1.11.2",
                "fileId": 2453428,
                "filename": "jei_1.11.2-4.5.0.294.jar",
                "releaseType": 1,
                "gameVersionTypeId": 599,
                "modLoader": null
            },
            {
                "gameVersion": "1.12",
                "fileId": 2442204,
                "filename": "jei_1.12-4.7.0.68.jar",
                "releaseType": 3,
                "gameVersionTypeId": 628,
                "modLoader": null
            },
            {
                "gameVersion": "1.8.9",
                "fileId": 2431977,
                "filename": "jei_1.8.9-2.28.18.187.jar",
                "releaseType": 1,
                "gameVersionTypeId": 4,
                "modLoader": null
            },
            {
                "gameVersion": "1.10.2",
                "fileId": 2428966,
                "filename": "jei_1.10.2-3.14.7.420.jar",
                "releaseType": 1,
                "gameVersionTypeId": 572,
                "modLoader": null
            },
            {
                "gameVersion": "1.11",
                "fileId": 2360492,
                "filename": "jei_1.11-4.1.1.208.jar",
                "releaseType": 2,
                "gameVersionTypeId": 599,
                "modLoader": null
            },
            {
                "gameVersion": "1.11",
                "fileId": 2350616,
                "filename": "jei_1.11-4.0.4.199.jar",
                "releaseType": 3,
                "gameVersionTypeId": 599,
                "modLoader": null
            },
            {
                "gameVersion": "1.9.4",
                "fileId": 2313650,
                "filename": "jei_1.9.4-3.6.8.225.jar",
                "releaseType": 1,
                "gameVersionTypeId": 552,
                "modLoader": null
            },
            {
                "gameVersion": "1.10",
                "fileId": 2310912,
                "filename": "jei_1.10-3.7.1.219.jar",
                "releaseType": 1,
                "gameVersionTypeId": 572,
                "modLoader": null
            },
            {
                "gameVersion": "1.9.4",
                "fileId": 2306298,
                "filename": "jei_1.9.4-3.6.2.211.jar",
                "releaseType": 2,
                "gameVersionTypeId": 552,
                "modLoader": null
            },
            {
                "gameVersion": "1.9",
                "fileId": 2305823,
                "filename": "jei_1.9.4-3.4.4.208.jar",
                "releaseType": 2,
                "gameVersionTypeId": 552,
                "modLoader": null
            },
            {
                "gameVersion": "1.9",
                "fileId": 2304545,
                "filename": "jei_1.9.4-3.4.3.207.jar",
                "releaseType": 1,
                "gameVersionTypeId": 552,
                "modLoader": null
            },
            {
                "gameVersion": "1.8.9",
                "fileId": 2292565,
                "filename": "jei_1.8.9-2.28.14.182.jar",
                "releaseType": 2,
                "gameVersionTypeId": 4,
                "modLoader": null
            },
            {
                "gameVersion": "1.8.8",
                "fileId": 2275072,
                "filename": "jei_1.8.9-2.16.2.78.jar",
                "releaseType": 1,
                "gameVersionTypeId": 4,
                "modLoader": null
            },
            {
                "gameVersion": "1.8",
                "fileId": 2273901,
                "filename": "jei_1.8-2.14.0.139.jar",
                "releaseType": 1,
                "gameVersionTypeId": 4,
                "modLoader": null
            },
            {
                "gameVersion": "1.8.8",
                "fileId": 2270928,
                "filename": "jei_1.8.8-2.8.3.39.jar",
                "releaseType": 2,
                "gameVersionTypeId": 4,
                "modLoader": null
            },
            {
                "gameVersion": "1.8",
                "fileId": 2270927,
                "filename": "jei_1.8-1.8.3.96.jar",
                "releaseType": 2,
                "gameVersionTypeId": 4,
                "modLoader": null
            }
        ],
        "dateCreated": "2015-11-23T22:55:58.84Z",
        "dateModified": "2022-07-20T04:20:59.683Z",
        "dateReleased": "2022-07-20T04:15:42.63Z",
        "allowModDistribution": true,
        "gamePopularityRank": 1,
        "isAvailable": true,
        "thumbsUpCount": 0
    }
}"""
        val mod = mapper.readValue<DataResponse<Mod>>(modJson)
        println(mod)
    }

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
        var image = mapper.readValue<Image>(json)
        println(image)
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
