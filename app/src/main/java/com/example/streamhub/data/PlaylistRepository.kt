package com.example.streamhub.data

import com.example.streamhub.Constants
import com.example.streamhub.model.Channel
import com.example.streamhub.model.PlaylistSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 加载单个播放列表来源，返回解析后的频道列表。
 * 特殊处理：SAMPLE_URL 表示内置示例，直接解析内置文本、不发起网络请求。
 */
object PlaylistRepository {

    suspend fun loadSource(source: PlaylistSource): List<Channel> = withContext(Dispatchers.IO) {
        val text = if (source.url == Constants.SAMPLE_URL) {
            Constants.SAMPLE_M3U
        } else {
            NetworkClient.fetchText(source.url)
        }
        M3uParser.parse(text, source.name)
    }
}
