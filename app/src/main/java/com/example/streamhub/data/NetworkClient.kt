package com.example.streamhub.data

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream

/**
 * 用系统自带的 HttpURLConnection 拉取 M3U 文本。
 * 不依赖 OkHttp，从而兼容到 Android 4.4（API 19），覆盖更老款的飞利浦电视。
 * 超时设置偏宽松，因为部分公开流服务器响应较慢。
 */
object NetworkClient {

    fun fetchText(
        url: String,
        connectTimeoutMs: Int = 15_000,
        readTimeoutMs: Int = 60_000
    ): String {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("User-Agent", "StreamHub/1.0")
            setRequestProperty("Accept", "*/*")
            connectTimeout = connectTimeoutMs
            readTimeout = readTimeoutMs
            instanceFollowRedirects = true
        }
        try {
            val code = connection.responseCode
            if (code !in 200..299) {
                throw IllegalStateException("HTTP $code ${connection.responseMessage}")
            }
            val encoding = connection.contentEncoding?.lowercase()
            val raw = if (encoding == "gzip") {
                GZIPInputStream(connection.inputStream)
            } else {
                connection.inputStream
            }
            val charset = parseCharset(connection.contentType)
            BufferedReader(InputStreamReader(raw, charset)).use { reader ->
                val sb = StringBuilder()
                val buf = CharArray(8192)
                var n: Int
                while (reader.read(buf).also { n = it } != -1) {
                    sb.append(buf, 0, n)
                }
                return sb.toString()
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun parseCharset(contentType: String?): Charset {
        if (contentType == null) return Charsets.UTF_8
        val idx = contentType.lowercase().indexOf("charset=")
        if (idx >= 0) {
            val name = contentType.substring(idx + "charset=".length)
                .trim().split(";")[0].trim()
            return try {
                Charset.forName(name)
            } catch (_: Exception) {
                Charsets.UTF_8
            }
        }
        return Charsets.UTF_8
    }
}
