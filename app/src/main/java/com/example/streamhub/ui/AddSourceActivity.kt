package com.example.streamhub.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.streamhub.data.PrefsStore
import com.example.streamhub.databinding.ActivityAddSourceBinding

/**
 * 添加自定义 M3U 播放列表来源。
 * 在电视上用遥控器聚焦输入框后，可用已连接的键盘 / 手机输入法 / 语音输入填写。
 */
class AddSourceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddSourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val url = binding.urlInput.text.toString().trim()
            if (url.isEmpty()) {
                Toast.makeText(this, "请输入 M3U 链接", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            PrefsStore(this).addSource(name.ifEmpty { url }, url)
            Toast.makeText(this, "已保存，返回主页将自动刷新", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.cancelButton.setOnClickListener { finish() }
    }
}
