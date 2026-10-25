# StreamHub 电视流 —— 飞利浦 Android TV 直播聚合播放器

一个面向 **Android TV（含飞利浦 Android TV）** 的开源 IPTV 播放器。
基于 Google 官方 **Leanback** 框架做遥控器导航，使用 **Media3 / ExoPlayer**
播放 HLS(.m3u8) / DASH(.mpd) / 渐进式直播流。用户用遥控器**上下键选台、OK 播放**。

> ⚠️ 合规声明
> 本仓库**不内置任何受版权保护的电视频道**。
> - 应用默认只带 2 个**公认的开源 / 公开测试流**（Big Buck Bunny、Apple BipBop），
>   用于验证播放器可用，请按需替换为**你自己有权使用**的源。
> - “加载公开 IPTV 索引”功能拉取的是 [iptv-org](https://github.com/iptv-org/iptv)
>   社区维护的**公开索引**，频道版权状况由使用者自行判断，App 不做任何背书。
> - 请勿用本工具聚合、传播你无权使用的付费/版权内容。

---

## 功能

- 遥控器 **上下键**在频道卡片间移动、**左右键**在分类间切换、**OK** 进入播放。
- 主界面三个分类：
  - **管理**：添加 M3U 来源、加载公开 IPTV 索引。
  - **我的频道**：你添加的 M3U 播放列表（未添加时展示内置示例测试流）。
  - **公开 IPTV**：一键加载的 iptv-org 索引频道。
- 支持 **HLS / DASH / 直接流**，自动按后缀识别。
- 显示频道名、缓冲/错误状态，播放失败有 Toast 提示。
- 频道台标（tvg-logo）自动加载（Glide）。

## 目录结构

```
androidTV/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/streamhub/
│       │   ├── Constants.kt
│       │   ├── MainActivity.kt
│       │   ├── model/        Channel.kt, PlaylistSource.kt
│       │   ├── data/         M3uParser.kt, NetworkClient.kt, PrefsStore.kt, PlaylistRepository.kt
│       │   └── ui/           MainFragment.kt, ChannelCardPresenter.kt, ActionCardPresenter.kt,
│       │                       PlaybackActivity.kt, PlaybackFragment.kt,
│       │                       AddSourceActivity.kt, IndexActivity.kt, ActionItem.kt
│       └── res/              values/, drawable/, layout/
└── README.md
```

## 环境要求

- **JDK 17**（Android Gradle Plugin 8.x 需要）
- **Android SDK**（API 33 平台 + build-tools）
- **Android Studio**（推荐，会自动补齐 Gradle Wrapper 并下载依赖）
- 目标设备：**飞利浦 Android TV**（Android 9~11 均可，minSdk 21）

## 构建 APK

### 方式 A：Android Studio（最简单）
1. 用 Android Studio 打开本项目根目录（`androidTV/`）。
2. 首次打开会提示生成 Gradle Wrapper 并同步依赖，等待完成。
3. 菜单 **Build → Build Bundle(s) / APK(s) → Build APK(s)**。
4. 产物在 `app/build/outputs/apk/release/app-release-unsigned.apk`
   （调试可直接点工具栏 ▶ Run 装到已连接的电视/模拟器）。

### 方式 B：命令行（需本机已装 Gradle 8.2）
```bash
# 若仓库缺少 gradle-wrapper.jar，先生成：
gradle wrapper --gradle-version 8.2
# 生成 release APK（未签名，安装时按提示允许未知来源即可）：
./gradlew assembleRelease
```

## 安装到飞利浦 Android TV

1. **开启开发者选项**：设置 → 关于 → 连续点击“版本号” 7 次。
2. **开启 USB 调试**：设置 → 设备偏好 → 开发者选项 → 打开“USB 调试”。
3. **连接电脑**（二选一）：
   - 有线：USB 线连接电视与电脑（部分电视需在连接模式里选“媒体设备/文件传输”）。
   - 无线（推荐，免去线材）：在电视“开发者选项”里打开“网络调试/无线调试”，
     记下电视 IP，电脑执行：
     ```bash
     adb connect <电视IP>:5555
     ```
4. **安装**：
   ```bash
   adb install app/build/outputs/apk/release/app-release-unsigned.apk
   ```
5. 电视主屏 → 应用 → 找到 **StreamHub 电视流** 打开即可。

> 提示：若提示“安装被阻止”，在电视上允许“未知来源应用”安装（对应 adb/文件管理器）。

## 使用

1. 打开应用，默认在“我的频道”里能看到内置示例测试流，**用遥控器上下键选台、OK 播放**，先验证能播。
2. **添加你自己的源**：主界面选“管理 → ➕ 添加 M3U 来源”，填入名称与 `.m3u` 链接，
   保存后返回主页自动刷新（遥控器聚焦输入框后可用已连接的键盘 / 手机输入法 / 语音输入）。
3. **加载公开索引（可选）**：选“管理 → 🌐 加载公开 IPTV 索引”，
   先试“中国频道”，加载完成后“公开 IPTV”行出现大量频道。
4. 频道太多时，用遥控器在卡片间快速移动即可；返回键退出播放回到列表。

## 自定义

- **替换内置示例流**：编辑 `app/src/main/java/com/example/streamhub/Constants.kt`
  里的 `SAMPLE_M3U`（或把 `sourcesToLoad()` 改成默认返回空，让“我的频道”初始为空）。
- **默认索引地址**：同文件 `INDEX_CN` / `INDEX_ALL` 可改为你信任的其它公开索引。
- **解析字段**：`data/M3uParser.kt` 已支持 `tvg-logo`、`group-title`、`tvg-id`，
  如需更多字段（如 `tvg-shift`）可在此扩展。

## 已知限制 / 可扩展点

- 加载用户源为每次进入前台重新拉取，未做本地缓存（公开索引有缓存）；可加 Room 缓存提升速度。
- 暂无“收藏 / 最近播放 / 分组筛选 / EPG 节目单”。
- 暂无频道搜索（可按 `name` 在 MainFragment 加 `SearchSupportFragment`）。
- 未做数字键直跳频道（可在 PlaybackFragment 监听数字键切到下一/上一频道）。
- 未做多音轨 / 字幕选择 UI（ExoPlayer 已支持，可在 PlayerView 控制条开启）。

## 第三方依赖

- `androidx.leanback` —— Android TV 官方 UI 框架
- `androidx.media3` —— ExoPlayer 播放内核
- `com.squareup.okhttp3` —— 网络拉取 M3U
- `com.bumptech.glide` —— 台标图片加载
- `org.jetbrains.kotlinx:kotlinx-coroutines-android` —— 协程
