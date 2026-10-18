# ShowFox - Android TV Browser

A custom Android TV browser based on Mozilla's GeckoView engine with built-in uBlock Origin support.

## Features

- **GeckoView Engine** — Full Firefox rendering engine
- **uBlock Origin** — Built-in ad blocking (hybrid: ContentBlocking API + uBO extension)
- **D-pad Navigation** — Full TV remote support
- **Quick Launch** — Press `i` (Info) button or Back with no history to toggle address bar
- **Media Player** — ExoPlayer for clear streams, GeckoView fallback for DRM content
- **Bookmarks & History** — Local Room database
- **Custom Cursor** — KDE Breeze style cursor on web pages
- **DRM Support** — Widevine for Netflix, Prime Video, etc.
- **Voice Search** — Google SpeechRecognizer integration

## Building

### Local Build (requires Android SDK)

```bash
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

### CI/CD (GitHub Actions)

Every push to `main` builds a debug APK automatically.
Push a tag `v*` to create a signed release on GitHub Releases.

## Project Structure

```
app/src/main/java/com/showfox/browser/
├── TvBrowserApp.kt          # Application class, GeckoRuntime init
├── engine/                  # GeckoView session management, content blocking
├── extensions/              # uBlock Origin installation and management
├── ui/
│   ├── MainActivity.kt      # Single Activity host
│   ├── browser/             # BrowserFragment, TopBar, UrlBar, MediaPlayer
│   ├── newtab/              # New tab page (main UI)
│   ├── tabs/                # Tab switcher
│   ├── bookmarks/           # Bookmark grid
│   ├── history/             # History list
│   ├── downloads/           # Download manager
│   ├── settings/            # Settings
│   ├── adblock/             # Ad blocking settings
│   └── video/               # ExoPlayer media player
├── data/                    # Room database, models, repositories
├── voice/                   # Voice search
└── util/                    # D-pad navigation, media detection
```

## Configuration

| Setting | Location | Default |
|---------|----------|---------|
| DRM (Widevine) | Settings > Privacy | Enabled |
| Private Browsing | Settings > Privacy | Disabled |
| Custom Cursor | Settings > Appearance | Enabled |
| uBlock Origin | Ad Blocking settings | Enabled |

## Requirements

- Android TV (API 21+, recommended API 34 / Android 14)
- ARM64 processor
- 1.5 GB RAM recommended

## License

MPL 2.0 — See [LICENSE](LICENSE)
