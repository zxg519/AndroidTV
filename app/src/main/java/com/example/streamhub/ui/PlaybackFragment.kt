package com.example.streamhub.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.streamhub.databinding.FragmentPlaybackBinding

/**
 * 用 Media3 / ExoPlayer 播放直播流。
 * 自动识别 HLS(.m3u8) / DASH(.mpd) / 渐进式流。
 * 遥控器：OK 显示/隐藏控制条并暂停-继续，返回键退出。
 */
class PlaybackFragment : Fragment() {

    private var _binding: FragmentPlaybackBinding? = null
    private val binding get() = _binding!!
    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaybackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = activity?.intent?.getStringExtra(PlaybackActivity.EXTRA_URL).orEmpty()
        val name = activity?.intent?.getStringExtra(PlaybackActivity.EXTRA_NAME) ?: "频道"

        binding.channelName.text = name
        binding.statusText.text = "正在连接…"
        binding.statusText.visibility = View.VISIBLE

        if (url.isEmpty()) {
            binding.statusText.text = "无效的播放地址"
            return
        }

        player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> binding.statusText.visibility = View.GONE
                        Player.STATE_BUFFERING -> {
                            binding.statusText.text = "缓冲中…"
                            binding.statusText.visibility = View.VISIBLE
                        }
                        Player.STATE_ENDED -> {
                            binding.statusText.text = "播放结束"
                            binding.statusText.visibility = View.VISIBLE
                        }
                        Player.STATE_IDLE -> { /* 初始态，忽略 */ }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    binding.statusText.text = "播放错误：${error.message}"
                    binding.statusText.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "播放失败：${error.message}", Toast.LENGTH_LONG).show()
                }
            })

            exoPlayer.setMediaSource(buildMediaSource(url))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    private fun buildMediaSource(url: String): MediaSource {
        val uri = Uri.parse(url)
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("StreamHubTV/1.0")
            .setAllowCrossProtocolRedirects(true)

        return when {
            url.contains(".m3u8", ignoreCase = true) ->
                HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))

            url.contains(".mpd", ignoreCase = true) ->
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(
                        MediaItem.Builder()
                            .setUri(uri)
                            .setMimeType(MimeTypes.APPLICATION_MPD)
                            .build()
                    )

            else ->
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onDestroyView() {
        player?.release()
        player = null
        _binding = null
        super.onDestroyView()
    }
}
