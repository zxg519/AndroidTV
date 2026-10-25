package com.example.streamhub.ui

/**
 * 主界面“管理”行里的功能入口卡片（添加来源 / 加载索引 / 状态提示）。
 */
data class ActionItem(
    val key: String,
    val title: String,
    val desc: String = ""
)
