package com.example.streamhub.ui

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.lifecycle.lifecycleScope
import com.example.streamhub.Constants
import com.example.streamhub.data.M3uParser
import com.example.streamhub.data.PlaylistRepository
import com.example.streamhub.data.PrefsStore
import com.example.streamhub.model.Channel
import com.example.streamhub.model.PlaylistSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 主界面：左侧为分类（管理 / 我的频道 / 公开 IPTV），右侧为频道卡片行。
 * 遥控器：上下键在卡片间移动、左右键在分类间切换、OK 进入播放或触发功能。
 */
class MainFragment : BrowseSupportFragment() {

    private lateinit var prefs: PrefsStore
    private lateinit var manageAdapter: ArrayObjectAdapter
    private lateinit var myAdapter: ArrayObjectAdapter
    private lateinit var indexAdapter: ArrayObjectAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prefs = PrefsStore(requireContext())

        title = getString(R.string.app_name)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(R.color.primary)

        val selector = ClassPresenterSelector().apply {
            addClassPresenter(Channel::class.java, ChannelCardPresenter())
            addClassPresenter(ActionItem::class.java, ActionCardPresenter())
        }

        manageAdapter = ArrayObjectAdapter(selector).apply {
            add(ActionItem("add", "➕ 添加 M3U 来源", "粘贴你自己的播放列表链接"))
            add(ActionItem("index", "🌐 加载公开 IPTV 索引", "加载 iptv-org 社区公开频道"))
        }
        myAdapter = ArrayObjectAdapter(selector)
        indexAdapter = ArrayObjectAdapter(selector)

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter()).apply {
            add(ListRow(HeaderItem("管理"), manageAdapter))
            add(ListRow(HeaderItem("我的频道"), myAdapter))
            add(ListRow(HeaderItem("公开 IPTV"), indexAdapter))
        }
        adapter = rowsAdapter

        setOnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is Channel -> {
                    val intent = Intent(activity, PlaybackActivity::class.java).apply {
                        putExtra(PlaybackActivity.EXTRA_URL, item.url)
                        putExtra(PlaybackActivity.EXTRA_NAME, item.name)
                    }
                    startActivity(intent)
                }
                is ActionItem -> handleAction(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadMyChannels()
        loadCachedIndex()
    }

    /** 用户无自定义源时，回退到内置合法公开测试流，保证开箱可看。 */
    private fun sourcesToLoad(): List<PlaylistSource> {
        val user = prefs.getSources()
        return if (user.isEmpty()) {
            listOf(PlaylistSource("sample", "内置示例 · 公开测试流", Constants.SAMPLE_URL))
        } else user
    }

    private fun loadMyChannels() {
        val sources = sourcesToLoad()
        myAdapter.clear()
        myAdapter.add(ActionItem("loading", "加载中…", ""))
        lifecycleScope.launch {
            val all = mutableListOf<Channel>()
            for (s in sources) {
                try {
                    all.addAll(PlaylistRepository.loadSource(s))
                } catch (e: Exception) {
                    // 单个源失败不影响其它源
                }
            }
            withContext(Dispatchers.Main) {
                myAdapter.clear()
                if (all.isEmpty()) {
                    myAdapter.add(ActionItem("err", "未解析到频道", "检查链接或网络后重试"))
                } else {
                    all.forEach { myAdapter.add(it) }
                }
            }
        }
    }

    private fun loadCachedIndex() {
        val file = File(requireContext().cacheDir, Constants.INDEX_CACHE)
        indexAdapter.clear()
        if (file.exists()) {
            val channels = M3uParser.parse(file.readText(), "公开索引")
            if (channels.isEmpty()) {
                indexAdapter.add(ActionItem("noindex", "暂无公开索引", "从『管理』加载"))
            } else {
                channels.forEach { indexAdapter.add(it) }
            }
        } else {
            indexAdapter.add(ActionItem("noindex", "暂无公开索引", "从『管理』加载"))
        }
    }

    private fun handleAction(item: ActionItem) {
        when (item.key) {
            "add" -> startActivity(Intent(activity, AddSourceActivity::class.java))
            "index" -> startActivity(Intent(activity, IndexActivity::class.java))
        }
    }
}
