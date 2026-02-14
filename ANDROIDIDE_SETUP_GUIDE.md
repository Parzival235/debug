# 📖 Audio Quick Play - Hướng Dẫn Chạy Trên AndroidIDE

## 📌 Tóm Tắt Nhanh

**Audio Quick Play** là ứng dụng Android hiện đại để phát nhạc với quick settings tile integration.  
Tài liệu này hướng dẫn cách chạy app trên **AndroidIDE** (IDE Android native).

---

## 📚 Tài Liệu Chính

| File | Mục Đích |
|------|----------|
| **QUICKSTART_ANDROIDIDE.md** | 🚀 Bắt đầu nhanh (5 phút) |
| **ANDROIDIDE_GUIDE.md** | 📖 Hướng dẫn chi tiết (setup → debug) |
| **README.md** | 📋 Tài liệu chính (features, setup, usage) |
| **ADVANCED_FEATURES.md** | ✨ Tính năng nâng cao (v1.2) |
| **CHANGELOG.md** | 📝 Lịch sử thay đổi |

---

## 🎯 Bắt Đầu Nhanh

```bash
# Step 1: Clone project
$ git clone https://github.com/Parzival235/debug.git audio-quick-play
$ cd audio-quick-play

# Step 2: Open in AndroidIDE
# File → Open Project → Select audio-quick-play folder

# Step 3: Build & Run
$ bash build.sh run
# Hoặc via UI: Build → Build Project → Run → Run
```

---

## 🛠️ Kiến Trúc Project

```
audio-quick-play/
├── Source Code (Java)
│   ├── LoginActivity.java          # Login UI
│   ├── MainActivity.java           # Playback UI + repeat/seekbar
│   ├── AudioPlayerService.java     # Foreground service
│   ├── AudioTileService.java       # Quick Settings
│   ├── PreferencesManager.java     # Encrypted preferences
│   ├── AudioPlayer.java            # MediaPlayer wrapper
│   └── PlayerUtils.java            # Utilities
│
├── Resources (XML)
│   ├── activity_login.xml          # Login layout
│   ├── activity_main.xml           # Playback (portrait)
│   ├── activity_main-land.xml      # Playback (landscape)
│   ├── themes.xml                  # Material3 theme
│   ├── colors.xml                  # Color palette
│   └── strings.xml                 # Text strings
│
├── Configuration
│   ├── AndroidManifest.xml
│   ├── build.gradle
│   ├── settings.gradle
│   ├── gradle.properties
│   └── proguard-rules.pro
│
├── Build Helper
│   ├── build.sh                    # Build script
│   └── local.properties.template
│
└── Documentation
    ├── README.md                   # Main docs
    ├── QUICKSTART_ANDROIDIDE.md    # Quick start
    ├── ANDROIDIDE_GUIDE.md         # Detailed guide
    ├── ADVANCED_FEATURES.md        # Features v1.2
    └── CHANGELOG.md                # Release notes
```

---

## ✨ Core Features

### v1.0
- Login/Authentication
- Audio file picker
- Basic playback (play/pause)
- Quick Settings tile

### v1.1
- Improved validation
- Logout button
- Play/pause toggle
- Material3 UI

### v1.2 (Latest)
- ✅ **Encrypted Preferences** (AES-256-GCM)
- ✅ **Repeat Mode** (OFF/ONE/ALL)
- ✅ **Seekbar + Time Display** (MM:SS format)
- ✅ **Landscape Layout** (responsive)
- ✅ **AudioPlayer Wrapper** (better state management)
- ✅ **PlayerUtils** (time formatting, validation)
- ✅ **Improved Error Handling** (callbacks + logging)

---

## 🚀 How to Run

### Via AndroidIDE GUI
1. **File** → **Open Project** → Select `audio-quick-play`
2. Wait for Gradle sync (first time: 30-60s)
3. **Build** → **Build Project**
4. **Run** → **Run**
5. Choose device/emulator → App launches

### Via Terminal/Script
```bash
cd audio-quick-play
bash build.sh run     # One-command build + install + launch
```

### Via Gradle Directly
```bash
# Clean
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install via ADB
adb install -r build/outputs/apk/debug/app-debug.apk

# Launch
adb shell am start -n com.example.audioquickplay/.LoginActivity
```

---

## 📋 System Requirements

| Component | Requirement |
|-----------|-------------|
| **Device OS** | Android 8.0+ (API 26+) |
| **RAM** | ≥ 3GB (for build + runtime) |
| **Storage** | ≥ 5GB free (SDK + Gradle cache) |
| **Java** | 11+ |
| **AndroidIDE** | v2.5.0+ |
| **SDK API** | 26 (min) + 34 (target) |

---

## 🧪 Testing Checklist

- [ ] App launches without crashing
- [ ] Login accepts valid credentials
- [ ] File picker shows audio files
- [ ] Playback starts/pauses
- [ ] Seekbar moves cursor
- [ ] Time display updates (MM:SS)
- [ ] Repeat mode toggles (OFF → ONE → ALL)
- [ ] Logout returns to login screen
- [ ] Landscape mode works
- [ ] Notification appears on playback
- [ ] Quick Settings tile functional

---

## 🐛 Common Issues & Fixes

| Issue | Solution |
|-------|----------|
| **Build fails - "Could not find tools.jar"** | Check Java 11+: `java -version` |
| **Project won't open** | Verify `build.gradle` exists in root |
| **Build timeout** | Increase Gradle timeout in Settings |
| **"Could not resolve dependencies"** | Check internet → Clean → Rebuild |
| **App crashes on launch** | View Logcat (top red errors) |
| **Permissions denied** | Grant in device Settings → Apps |

---

## 🔗 Useful Links

- **GitHub Repo**: https://github.com/Parzival235/debug
- **AndroidIDE**: https://github.com/Mohammed-Baqer-null/AndroidIDE-Rv2
- **Android Docs**: https://developer.android.com
- **Material3**: https://m3.material.io

---

## 📞 Support & Debugging

**Enable Logcat** (in AndroidIDE):
```
View → Logcat
Filter: "com.example.audioquickplay"
```

**Watch for errors**:
- Red lines = compile errors
- Orange = warnings
- Look for stack traces if app crashes

**Key commands**:
```bash
# Build clean
./gradlew clean

# Build with verbose output
./gradlew assembleDebug --info

# List connected devices
adb devices

# View app logs
adb logcat | grep audioquickplay
```

---

## 📅 Version History

| Version | Date | Highlights |
|---------|------|-----------|
| 1.0 | Initial | Basic audio player |
| 1.1 | Jan 2026 | Login refactor, Material3 |
| 1.2 | Feb 2026 | Encrypted prefs, repeat mode, seekbar |

---

## ✅ Project Status

- ✅ Fully functional audio player app
- ✅ Production-ready code (v1.2)
- ✅ Comprehensive documentation
- ✅ Material3 design + responsive layouts
- ✅ Security best practices (encrypted storage)
- ✅ Error handling & logging

**Ready to build & deploy!** 🎉

---

**For detailed instructions, see**:
- Quick start: `QUICKSTART_ANDROIDIDE.md`
- Full setup: `ANDROIDIDE_GUIDE.md`
- Features: `ADVANCED_FEATURES.md`
