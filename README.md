# Audio Quick Play

A lightweight Android application for quick audio playback with Quick Settings tile integration. Built with Material3 design and modern Android architecture.

![Version](https://img.shields.io/badge/version-1.1-blue)
![Min SDK](https://img.shields.io/badge/minSDK-26-green)
![Target SDK](https://img.shields.io/badge/targetSDK-34-blue)

## Features

✨ **Core Functionality:**
- 🎵 **Quick Audio Playback** - Select and play audio files from device storage
- 🔐 **Secure Login** - Simple authentication with validation and encrypted session management
- ⚡ **Quick Settings Tile** - Add a tile to Quick Settings for one-tap playback control
- 📱 **Foreground Service** - Reliable background audio playback with notification
- 🎚️ **Play/Pause Controls** - Toggle playback from MainActivity or Quick Settings
- 🌗 **Material3 Design** - Modern, responsive UI with light/dark theme support

## Requirements

- **Android SDK:** API 26 (Android 8.0) or higher
- **Target SDK:** API 34 (Android 15)
- **Java:** JDK 11 or higher
- **Gradle:** 8.0 or later

## Setup & Build

### 1. Clone the Repository

```bash
git clone https://github.com/Parzival235/debug.git
cd debug
```

### 2. Open in Android Studio

- Open Android Studio
- Select "Open an existing Android Studio project"
- Navigate to the project directory
- Wait for Gradle sync to complete

### 3. Build the Project

**Using Gradle (Command Line):**
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

**Using Android Studio:**
- Go to `Build` > `Make Project`
- To run: `Run` > `Run 'app'` or press `Shift + F10`

### 4. Install on Device/Emulator

**Using adb:**
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Using Android Studio:**
- Connect device or start emulator
- Click the "Run" button (green play icon)

## Usage

### First Launch

1. **Login Screen:**
   - Enter username (minimum 3 characters)
   - Enter password (minimum 4 characters)
   - Tap "Đăng nhập" (Login)

2. **Main Screen:**
   - Tap "Chọn file từ bộ nhớ" (Select audio file) to pick an audio file
   - Selected file name will be displayed
   - Tap "Phát ngay" (Play) to start/pause playback
   - Tap "Đăng xuất" (Logout) to return to login screen

### Quick Settings Integration

1. Swipe down to open Quick Settings
2. Tap the edit/pencil icon
3. Locate "Phát Âm Thanh" (Audio Player)
4. Drag it into your Quick Settings
5. From now on, tap the tile to play/pause the selected audio

## Permissions Required

The following permissions are requested:

| Permission | Purpose |
|---|---|
| `READ_EXTERNAL_STORAGE` (API <33) | Access audio files on device storage |
| `READ_MEDIA_AUDIO` (API ≥33) | Request audio file access (scoped) |
| `FOREGROUND_SERVICE` (API ≥31) | Display notification during playback |

**Grant permissions on first run when prompted.**

## Project Structure

```
.
├── LoginActivity.java          # Authentication UI and logic
├── MainActivity.java           # Main playback interface
├── AudioPlayerService.java     # Foreground service for playback
├── AudioTileService.java       # Quick Settings tile service
│
├── activity_login.xml          # Login screen layout
├── activity_main.xml           # Main playback layout
├── ic_play.xml                 # Play icon drawable
│
├── AndroidManifest.xml         # Application manifest
├── themes.xml                  # Material3 theme definition
├── colors.xml                  # Material3 color palette
├── strings.xml                 # String resources (Vietnamese)
│
├── build.gradle                # Gradle build configuration
├── settings.gradle             # Gradle settings
├── proguard-rules.pro          # ProGuard/R8 obfuscation rules
│
└── README.md                   # This file
```

## Architecture

### Components

- **LoginActivity:** Entry point with authentication logic
- **MainActivity:** Primary UI for file selection and playback control
- **AudioPlayerService:** Foreground service managing audio playback, lifecycle, and notifications
- **AudioTileService:** Quick Settings tile for quick playback toggle

### Data Flow

1. User selects audio file → stored in SharedPreferences
2. Play/Pause triggered → Intent sent to AudioPlayerService
3. Service updates playback state → broadcasts state change
4. MainActivity receives broadcast → updates button UI
5. Notification displays current playback state

## Development

### Adding New Features

1. **Add String Resources:**
   - Edit `strings.xml` for all user-facing text

2. **Update UI:**
   - Modify layout XML files in the root directory
   - Reference Material3 styles from `themes.xml`

3. **Storage Changes:**
   - Update SharedPreferences keys in `LoginActivity.PREFS_NAME`
   - Consider migration for existing data

### Building for Release

```bash
./gradlew assembleRelease
```

Signed APK will be in `app/build/outputs/apk/release/`.

### Code Quality

The project follows Android best practices:
- Material3 design components
- Proper lifecycle management
- Resource access control via SharedPreferences
- BroadcastReceiver for state synchronization
- ProGuard/R8 for release builds

## Security Notes

⚠️ **Important:**
- Passwords are NOT stored in SharedPreferences
- Only login session flags are persisted
- Audio file URIs are cached for recovery (non-sensitive)
- For production apps, consider:
  - EncryptedSharedPreferences for sensitive data
  - OAuth/JWT authentication instead of simple credentials
  - Secure token storage

## Testing

### Manual Testing Checklist

- [ ] Login with valid credentials
- [ ] Verify validation on short username/password
- [ ] Select audio file from device storage
- [ ] Start playback, verify notification appears
- [ ] Pause/resume playback from MainActivity
- [ ] Pause/resume playback from Quick Settings tile
- [ ] Verify playback stops on app close
- [ ] Logout returns to login screen
- [ ] Quick Settings tile reflects playback state

### Run Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## Troubleshooting

| Issue | Solution |
|---|---|
| App crashes on startup | Clear app data; Grant permissions |
| Quick Settings tile not appearing | Restart device; Reinstall app |
| No audio plays | Check file format (MP3, WAV, OGG supported); Grant READ_MEDIA_AUDIO |
| Notification doesn't appear | Update Android version; Check notification settings |
| Login always fails | Verify input is longer than requirements |

## Changelog

### v1.1 (Current)
- ✅ Improved login validation (no password storage)
- ✅ Foreground service for reliable playback
- ✅ Play/Pause toggle controls
- ✅ Material3 design implementation
- ✅ String resources localization
- ✅ ProGuard optimization
- ✅ Better lifecycle management

### v1.0
- Initial release with basic audio playback

## License

This project is provided as-is for educational and development purposes.

## Authors

- **Original Creator:** Mohammed Baqer (AndroidIDE-Rv2)
- **Upgrade & Modernization:** 2026

## Support

For issues, questions, or feature requests, please open an issue on GitHub.

---

**Built with ❤️ using Android Studio and Material3**
