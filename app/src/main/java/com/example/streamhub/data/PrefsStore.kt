package com.example.streamhub.data

import android.content.Context
import android.content.SharedPreferences
import com.example.streamhub.Constants
import com.example.streamhub.model.PlaylistSource
import org.json.JSONArray
import org.json.JSONObject

/**
 * 用 SharedPreferences 持久化“用户添加的 M3U 来源”。
 * 采用简单 JSON 数组存储，便于阅读与手动修改。
 */
class PrefsStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun getSources(): List<PlaylistSource> {
        val json = prefs.getString(Constants.KEY_SOURCES, "[]") ?: "[]"
        return try {
            val arr = JSONArray(json)
            val list = mutableListOf<PlaylistSource>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                list.add(
                    PlaylistSource(
                        id = o.getString("id"),
                        name = o.getString("name"),
                        url = o.getString("url")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addSource(name: String, url: String) {
        val list = getSources().toMutableList()
        list.add(
            PlaylistSource(
                id = java.util.UUID.randomUUID().toString(),
                name = name,
                url = url
            )
        )
        save(list)
    }

    fun removeSource(id: String) {
        save(getSources().filter { it.id != id })
    }

    private fun save(list: List<PlaylistSource>) {
        val arr = JSONArray()
        list.forEach {
            arr.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("name", it.name)
                    put("url", it.url)
                }
            )
        }
        prefs.edit().putString(Constants.KEY_SOURCES, arr.toString()).apply()
    }
}
