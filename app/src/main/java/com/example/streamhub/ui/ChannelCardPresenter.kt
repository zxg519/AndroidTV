package com.example.streamhub.ui

import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.streamhub.R
import com.example.streamhub.model.Channel

/**
 * 频道卡片：显示台标 + 频道名 + 分组/来源。
 * 遥控器上下左右移动焦点，OK 由 MainFragment 统一处理进入播放。
 */
class ChannelCardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val card = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setBackgroundColor(parent.context.getColor(R.color.card_bg))
        }
        return ViewHolder(card)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val channel = item as Channel
        val card = viewHolder.view as ImageCardView
        card.setTitleText(channel.name)
        card.setContentText(channel.group ?: channel.sourceName)
        card.setMainImageDimensions(176, 176)

        if (!channel.logoUrl.isNullOrEmpty()) {
            Glide.with(card.context)
                .load(channel.logoUrl)
                .placeholder(R.drawable.default_channel)
                .error(R.drawable.default_channel)
                .into(card.mainImageView)
        } else {
            card.mainImageView.setImageResource(R.drawable.default_channel)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val card = viewHolder.view as ImageCardView
        card.mainImage = null
    }
}
