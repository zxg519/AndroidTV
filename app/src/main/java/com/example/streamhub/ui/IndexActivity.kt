package com.example.streamhub.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.streamhub.Constants
import com.example.streamhub.data.M3uParser
import com.example.streamhub.data.NetworkClient
import com.example.streamhub.databinding.ActivityIndexBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 一键加载 iptv-org 公开索引（按国家 / 全部），下载后缓存到 cacheDir，
 * 主页“公开 IPTV”行会读取该缓存展示。
 *
 * 注意：iptv-org 索引为社区提交的公开流聚合，频道版权状况由使用者自行判断，
 * 本 App 仅做技术上的拉取与解析，不对内容合法性背书。
 */
class IndexActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChina.setOnClickListener { load(Constants.INDEX_CN, "中国频道") }
        binding.btnAll.setOnClickListener { load(Constants.INDEX_ALL, "全部频道") }
        binding.btnCancel.setOnClickListener { finish() }
    }

    private fun load(url: String, label: String) {
        Toast.makeText(this, "正在加载 $label，请稍候…", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            try {
                val text = withContext(Dispatchers.IO) { NetworkClient.fetchText(url) }
                val file = File(cacheDir, Constants.INDEX_CACHE)
                file.writeText(text)
                val count = M3uParser.parse(text, "公开索引").size
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@IndexActivity,
                        "已加载 $count 个频道，返回主页查看",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@IndexActivity,
                        "加载失败：${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
