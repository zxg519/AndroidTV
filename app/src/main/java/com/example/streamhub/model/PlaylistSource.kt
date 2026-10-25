package com.example.streamhub.model

/**
 * 一个播放列表来源（用户添加的 M3U 链接，或内置示例 / 公开索引入口）。
 *
 * @param id   唯一 id
 * @param name 展示名称
 * @param url  M3U 链接；特殊值 [com.example.streamhub.Constants.SAMPLE_URL] 表示内置示例
 */
data class PlaylistSource(
    val id: String,
    val name: String,
    val url: String
)
