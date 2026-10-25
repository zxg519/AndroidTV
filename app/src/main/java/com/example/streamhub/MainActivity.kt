package com.example.streamhub

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.streamhub.databinding.ActivityMainBinding

/**
 * 启动 Activity：承载 Leanback 的 [com.example.streamhub.ui.MainFragment]。
 * 使用 FragmentActivity + Leanback 主题，符合 Android TV 规范。
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, MainFragment())
                .commitNow()
        }
    }
}
