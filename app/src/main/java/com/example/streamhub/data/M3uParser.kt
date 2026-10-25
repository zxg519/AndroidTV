package com.example.streamhub.data

import com.example.streamhub.model.Channel
import java.util.UUID

/**
 * 极简 M3U / M3U8 解析器，覆盖常见字段：
 *   #EXTINF:-1 tvg-id=".." tvg-logo=".." group-title="..",频道名
 *   <url>
 */
object M3uParser {

    // 匹配 #EXTINF:-1 后的属性段与逗号后的标题
    private val EXTINF = Regex("""#EXTINF:-1(?:\s+([^,]*))?,(.*)""")
    private val ATTR = Regex("""(\w[\w-]*)="([^"]*)"""")

    fun parse(text: String, sourceName: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        var pendingName: String? = null
        var pendingLogo: String? = null
        var pendingGroup: String? = null

        for (raw in text.lineSequence()) {
            val line = raw.trim()
            if (line.isEmpty()) continue

            when {
                line.startsWith("#EXTINF") -> {
                    val m = EXTINF.find(line)
                    if (m != null) {
                        val attrPart = m.groupValues[1]
                        val title = m.groupValues[2].trim()
                        val attrs = ATTR.findAll(attrPart)
                            .associate { it.groupValues[1].lowercase() to it.groupValues[2] }
                        pendingName = title
                        pendingLogo = attrs["tvg-logo"]?.takeIf { it.isNotBlank() }
                        pendingGroup = attrs["group-title"]?.takeIf { it.isNotBlank() }
                    }
                }
                line.startsWith("#") -> {
                    // 其它指令（#EXTGRP、#PLAYLIST 等）忽略
                }
                else -> {
                    val url = line.trim()
                    if (pendingName != null && url.isNotEmpty()) {
                        channels.add(
                            Channel(
                                id = UUID.randomUUID().toString(),
                                name = pendingName!!,
                                logoUrl = pendingLogo,
                                group = pendingGroup,
                                url = url,
                                sourceName = sourceName
                            )
                        )
                    }
                    pendingName = null
                    pendingLogo = null
                    pendingGroup = null
                }
            }
        }
        return channels
    }
}
