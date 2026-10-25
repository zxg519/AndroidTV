package com.example.streamhub.ui

import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.example.streamhub.R

/**
 * “管理”行 / 状态提示卡片的展示器。
 */
class ActionCardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val card = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setBackgroundColor(parent.context.getColor(R.color.primary))
        }
        return ViewHolder(card)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val action = item as ActionItem
        val card = viewHolder.view as ImageCardView
        card.setTitleText(action.title)
        card.setContentText(action.desc)
        card.setMainImageDimensions(176, 176)
        card.mainImageView.setImageResource(R.drawable.default_channel)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val card = viewHolder.view as ImageCardView
        card.mainImage = null
    }
}
