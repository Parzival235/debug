# 📱 Hướng Dẫn Chạy Audio Quick Play trong AndroidIDE

**AndroidIDE** là IDE Android native chạy trực tiếp trên thiết bị Android. Hướng dẫn này giúp bạn import, build và chạy Audio Quick Play trên AndroidIDE.

---

## 📋 Yêu Cầu

### Hardware
- **Thiết bị Android**: API 26+ (Android 8.0+) khuyến nghị
- **RAM**: ≥ 3GB (để AndroidIDE + Gradle build)
- **Storage**: ≥ 5GB free (cho SDK, NDK, Gradle cache)
- **CPU**: Multi-core processor

### AndroidIDE Installation
1. Cài đặt **AndroidIDE** từ:
   - GitHub Releases: https://github.com/Mohammed-Baqer-null/AndroidIDE-Rv2
   - Hoặc từ F-Droid/Play Store (nếu có)

2. **Minimum AndroidIDE Build**: v2.5.0+
   - Check: Settings → About → Version

3. **SDK Setup**:
   - AndroidIDE đi kèm với SDK Manager
   - Cần install: SDK API 34 (Target) + API 26 (Min Target)

---

## 🚀 Bước 1: Import Project vào AndroidIDE

### Via Git Clone
```bash
# Mở Terminal trong AndroidIDE hoặc adb shell
$ cd ~/AndroidIDEProjects/  # hoặc thư mục projects mặc định
$ git clone https://github.com/Parzival235/debug.git audio-quick-play
$ cd audio-quick-play
```

### Via File Manager
1. **Copy project folder** vào `~/AndroidIDEProjects/`
   - Cấu trúc phải là:
   ```
   ~/AndroidIDEProjects/
   └── audio-quick-play/
       ├── src/
       ├── build.gradle
       ├── settings.gradle
       ├── AndroidManifest.xml
       └── ... (các files khác)
   ```

### Mở trong AndroidIDE
1. Launch **AndroidIDE**
2. Tap **File** → **Open Project**
3. Navigate to `audio-quick-play` folder
4. **Select** project root folder (nơi có `settings.gradle`)
5. Tap **Open**

---

## ⚙️ Bước 2: Configure Project Settings

Sau khi import, AndroidIDE sẽ tự động detect `settings.gradle` và `build.gradle`.

### Check Configuration
1. **Project Panel** (Left sidebar)
   - Verify: Bạn thấy `audio-quick-play` với cây file
   
2. **Build Configuration** (nếu cần)
   - File → **Project Settings**
   - Kiểm tra:
     - **Gradle Distribution**: Latest recommended
     - **Java Version**: 11 hoặc cao hơn
     - **Variant**: `debug` (để develop nhanh)

3. **SDK Configuration**
   - **Settings** → **SDK Manager**
   - **Install** nếu chưa có:
     - [ ] Android SDK 34 (Target)
     - [ ] Android SDK 26 (Min)
     - [ ] Build Tools 34.0.0+
     - [ ] Source for Android 34 (optional)

---

## 🔨 Bước 3: Build Project

### Full Build
1. Open Project
2. **Build** menu → **Build Project** (hoặc Ctrl+B)
   - First build sẽ lâu (download Gradle + dependencies)
   - Sau lần đầu sẽ cache → nhanh hơn

### Build Output
- **Console**: Xem build logs (bottom panel)
- **Build Progress**: Top right corner
- **Status**: "BUILD SUCCESSFUL" hoặc error

### Handle Common Build Errors

**Error: "Could not determine the path to the Java compiler"**
- **Fix**: Settings → SDK Manager → Verify Java compiler path
  - Phải trỏ đến Android SDK bundled JDK

**Error: "Could not resolve androidx.security:security-crypto"**
- **Cause**: Internet kết nối hoặc Gradle cache
- **Fix**: 
  ```
  Build → Clean Project → Build Project (lại)
  ```

**Error: Gradle syncing slow/timeout**
- **Workaround**:
  - Ensure WiFi connected (không mobile data)
  - Increase Gradle timeout: Project Settings → Gradle Timeout (e.g., 60s)

---

## 📦 Bước 4: Run on Device/Emulator

### Run on Connected Device

**Prerequisites**:
- USB cable kết nối
- USB Debugging enabled trên device (Settings → Developer Options)
- ADB recognized by AndroidIDE

**Steps**:
1. **Run** menu → **Run** (hoặc Shift+F10)
   - Hoặc Project panel → right-click → **Run**

2. **Select Device**:
   - Choose your connected device from list
   - Nếu chỉ có 1 device, sẽ auto-select

3. **Build & Install**:
   - AndroidIDE build APK
   - Push APK to device
   - Auto-launch app

4. **Verify**:
   - App icon "Audio Quick Play" xuất hiện
   - Tap để mở → Login screen

### Run on AndroidIDE's Built-in Emulator

**Setup**:
1. **Settings** → **Emulator Manager**
2. Create new emulator (nếu chưa có)
   - Choose API 26+ image
   - Allocate RAM (e.g., 1GB)
3. **Start** emulator
4. **Run** → Select emulator from device list

---

## 🧪 Bước 5: Test & Debug

### Basic Testing
1. **Login Screen**
   - Enter username (≥3 chars)
   - Enter password (≥4 chars)
   - Tap "Đăng nhập" (Login)

2. **Pick Audio**
   - Tap "Chọn file từ bộ nhớ"
   - Select audio file (MP3/WAV/OGG)
   - Verify filename shows

3. **Playback**
   - Tap "Phát ngay" (Play)
   - Should hear audio or see notification
   - Tap again to pause

4. **Repeat Mode**
   - Tap "🔁 OFF" button to toggle
   - Verify text changes to "🔂 ONE" → "🔁 ALL" → "🔁 OFF"

5. **Seek**
   - Tap seekbar to jump position
   - Time display should update

6. **Logout**
   - Tap "Đăng xuất"
   - Return to login screen
   - Session cleared

### View Logs (Debugging)

1. **Logcat** (AndroidIDE's built-in)
   - **View** → **Logcat**
   - Filter by app name: `com.example.audioquickplay`

2. **Common Log Patterns**:
   ```
   I/AudioPlayerService: Starting playback
   D/PreferencesManager: Saving encrypted preferences
   W/AudioPlayer: Seek requested but no MediaPlayer
   E/AudioTileService: Failed to get QS Tile
   ```

3. **Debug Mode**:
   - Set breakpoints in Java Editor
   - Run with debug (if supported by AndroidIDE version)

---

## 🐛 Troubleshooting

### Issue: "Cannot read AndroidManifest.xml"
**Cause**: Project structure messed up  
**Fix**: Ensure files are in correct location:
```
audio-quick-play/
├── AndroidManifest.xml
├── build.gradle
├── settings.gradle
├── src/
│   ├── com/example/audioquickplay/
│   │   ├── LoginActivity.java
│   │   ├── MainActivity.java
│   │   ├── AudioPlayerService.java
│   │   └── ... (others)
│   └── res/
│       ├── layout/
│       ├── values/
│       └── ... (resources)
└── ... (config files)
```

### Issue: Build fails - "aapt2 error"
**Cause**: Resource naming or declaration error  
**Fix**:
1. Check `strings.xml` / `colors.xml` for typos
2. Check layout XML IDs match code (e.g., `@+id/btn_play`)
3. Clean & rebuild: `Build → Clean Project`

### Issue: "Permission denied" when installing APK
**Fix**:
```
adb shell pm install -r /path/to/apk
# Or in AndroidIDE:
Settings → Permissions → Grant app installation permission
```

### Issue: App crashes on startup
**Steps to debug**:
1. Open **Logcat** filter to `crash` or `exception`
2. Look for stack trace starting with your app package
3. Common causes:
   - Missing resource ID (check R.java)
   - NullPointerException in onCreate()
   - Missing permission in manifest

**Quick fix**:
```
1. Clean Project (Build → Clean)
2. Invalidate Caches (Settings → Clear cache)
3. Rebuild (Build → Build Project)
```

---

## ⚡ Tips for AndroidIDE Development

### 1. **Optimize Build Speed**
- Use **Build Variant** = `Debug` (faster than Release)
- Enable **Offline Mode** if no internet (Project Settings)
- Increase **Gradle Daemon Memory**: Settings → Gradle Options
  - Add: `-Xmx1500m` to JVM args

### 2. **Code Editing**
- **Quick Fix**: Alt+Enter on errors
- **Jump to Declaration**: Ctrl+Click on class/method
- **Refactor**: Right-click → Refactor
- **Format Code**: Ctrl+Alt+L

### 3. **Resource Management**
- Keep **Emulator closed** when not testing
- Monitor storage: free up >10GB for smooth builds
- **Clear Gradle cache** if build issues:
  ```
  Settings → Cache Manager → Clear Gradle Cache
  ```

### 4. **AndroidIDE Hotkeys**
| Shortcut | Action |
|----------|--------|
| Ctrl+B | Build |
| Shift+F10 | Run |
| Ctrl+Alt+L | Format Code |
| Ctrl+/ | Toggle Comment |
| Ctrl+D | Duplicate Line |
| Alt+Up/Down | Move Line |

---

## 📝 Project Structure Reference

```
audio-quick-play/
│
├── AndroidManifest.xml          # App manifest + permissions
├── build.gradle                 # Gradle build config
├── settings.gradle              # Gradle project settings
├── gradle.properties            # Version + compile constants
├── proguard-rules.pro           # R8 obfuscation rules
│
├── LoginActivity.java           # Login screen
├── MainActivity.java            # Playback UI
├── AudioPlayerService.java      # Foreground service
├── AudioTileService.java        # Quick Settings tile
├── PreferencesManager.java      # Encrypted prefs
├── AudioPlayer.java             # MediaPlayer wrapper
├── PlayerUtils.java             # Time/format utils
│
├── activity_login.xml           # Login layout
├── activity_main.xml            # Playback layout (portrait)
├── activity_main-land.xml       # Playback layout (landscape)
├── ic_play.xml                  # Play icon
├── ic_pause.xml                 # Pause icon
│
├── themes.xml                   # Material3 theme
├── colors.xml                   # Color palette
├── strings.xml                  # Text strings (Vi)
│
├── README.md                    # Main documentation
├── CHANGELOG.md                 # Release notes
├── ADVANCED_FEATURES.md         # Feature details
└── .gitignore                   # Git ignore patterns
```

---

## ✅ Checklist Before Submitting Build

- [ ] No compile errors (Build → Build Project success)
- [ ] No lint warnings in critical code (red squiggles)
- [ ] App launches without crashing
- [ ] Login flow works
- [ ] Audio file picked successfully
- [ ] Playback starts/pauses correctly
- [ ] Repeat mode toggles
- [ ] Seekbar moves position
- [ ] Logout clears session
- [ ] Notification appears during playback
- [ ] Quick Settings tile functional (if device supports)

---

## 🔗 Useful Resources

- **AndroidIDE GitHub**: https://github.com/Mohammed-Baqer-null/AndroidIDE-Rv2
- **Android Docs**: https://developer.android.com
- **Material3 Guide**: https://m3.material.io
- **Gradle Docs**: https://gradle.org/guide/

---

## 📞 Quick Support

**Build won't start?**
1. Verify Java version: `File → Project Settings → Java Version = 11`
2. Clear cache: `Settings → Cache Manager → Clear All`
3. Restart AndroidIDE

**App crashes?**
1. Open Logcat: `View → Logcat`
2. Look for RED lines with stack trace
3. Check line numbers in crash log match your code

**Slow builds?**
1. Close emulator if running
2. Increase Gradle heap: Settings → Gradle Options → `-Xmx1500m`
3. Check internet speed (first build downloads ~500MB)

---

**Happy Coding! 🎉**  
Built with ❤️ for AndroidIDE developers
