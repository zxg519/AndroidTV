package com.example.streamhub.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.streamhub.databinding.ActivityPlaybackBinding

/**
 * 全屏播放 Activity，承载 [PlaybackFragment]。
 */
class PlaybackActivity : FragmentActivity() {

    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_NAME = "extra_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.playback_frame, PlaybackFragment())
                .commitNow()
        }
    }
}
