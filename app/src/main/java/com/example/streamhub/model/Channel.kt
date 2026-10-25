package com.example.streamhub.model

/**
 * 一个直播频道。
 *
 * @param id        唯一 id（解析时生成）
 * @param name      频道名称（来自 #EXTINF 逗号后的标题）
 * @param logoUrl   台标地址（来自 tvg-logo，可能为空）
 * @param group     分组（来自 group-title，可能为空）
 * @param url       实际播放地址（.m3u8 / .mpd / 直接流）
 * @param sourceName 来源名称，便于在卡片副标题区分
 */
data class Channel(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val group: String?,
    val url: String,
    val sourceName: String
)
