package com.example.streamhub

/**
 * 全局常量与公开播放列表入口。
 *
 * 合规说明：
 * - 本 App 不内置任何受版权保护的电视频道。
 * - [SAMPLE_M3U] 仅包含公认的开源 / 公开测试流（Big Buck Bunny、Apple BipBop），
 *   用于验证播放器是否可用，请按需替换为你自己有权使用的源。
 * - [INDEX_CN] / [INDEX_ALL] 指向 GitHub 上 iptv-org 维护的“公开索引”项目，
 *   其聚合的是社区提交的公开流，频道版权状况由使用者自行判断，App 不背书。
 */
object Constants {
    const val PREFS_NAME = "streamhub_prefs"
    const val KEY_SOURCES = "user_sources"

    /** 存放已加载的公开索引 M3U 文本的缓存文件名（位于 cacheDir） */
    const val INDEX_CACHE = "index_cache.m3u"

    /** 标记为“内置示例源”的特殊 url（不打网络，直接解析 SAMPLE_M3U） */
    const val SAMPLE_URL = "__sample__"

    /** iptv-org 公开索引：按国家（中国） */
    const val INDEX_CN = "https://iptv-org.github.io/iptv/countries/cn.m3u"

    /** iptv-org 公开索引：全部（数量很大，加载较慢） */
    const val INDEX_ALL = "https://iptv-org.github.io/iptv/index.m3u"

    /** 内置合法公开测试流，开箱即可验证播放器 */
    val SAMPLE_M3U = """
        #EXTM3U
        #EXTINF:-1 tvg-logo="",测试流 · Big Buck Bunny
        https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8
        #EXTINF:-1 tvg-logo="",测试流 · Apple BipBop
        https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8
    """.trimIndent()
}
