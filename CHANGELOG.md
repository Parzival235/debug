# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1] - 2026-02-14

### Added
- ✨ Foreground Service (`AudioPlayerService`) for reliable background audio playback
- 🎵 Media Notification with play/pause controls
- 🔄 BroadcastReceiver in MainActivity to sync UI with playback state
- 🎚️ Play/Pause toggle functionality (switch between playing and paused states)
- 📱 Integration between MainActivity and Quick Settings tile via shared service
- 🌗 Material3 complete design implementation (colors, typography, components)
- 📋 String resources file (`strings.xml`) for all user-facing text
- 🎨 Color palette file (`colors.xml`) with Material3 theme colors
- 📐 Enhanced layouts with Material3 components and better spacing
- 🔐 Input validation on login (minimum length checks)
- 📝 Comprehensive README.md with setup, usage, and troubleshooting
- 🏗️ Gradle build configuration (`build.gradle`, `settings.gradle`)
- 🛡️ ProGuard/R8 optimization rules for release builds
- 📦 Property files for gradle configuration

### Improved
- 🔒 **Security**: Removed raw password storage from SharedPreferences (only username + login flag)
- 🎯 **Error Messages**: Specific validation error messages for username and password
- 🧹 **Code Quality**: 
  - Moved hardcoded strings to string resources
  - Better resource management and lifecycle handling
  - Proper cleanup in onDestroy() methods
  - State synchronization via broadcasts
- 📱 **User Experience**: 
  - Better visual hierarchy with Material3 typography
  - Responsive layouts that work on different screen sizes
  - Clear feedback for all user actions (toasts, notifications)
- 🖼️ **UI Polish**: 
  - Background colors reflect theme selection
  - Proper button styles and spacing
  - Card-based layout with elevation and stroke
  - Scrollable content area for smaller screens

### Fixed
- 🐛 **MediaPlayer Lifecycle**: Proper cleanup to prevent resource leaks
- 🐛 **Tile State Sync**: Quick Settings tile now properly reflects actual playback state
- 🐛 **Null Pointer Exceptions**: Better null checks in service initialization
- 🐛 **Broadcast Registration**: Proper registration/unregistration of receivers to prevent leaks

### Changed
- ♻️ Refactored audio playback logic from MainActivity to dedicated `AudioPlayerService`
- ♻️ Quick Settings tile now delegates to service instead of managing MediaPlayer directly
- ♻️ Login validation moved to dedicated methods with clear error states
- 🔄 Updated `AudioTileService` to use service-based playback control

### Deprecated
- ⚠️ Direct MediaPlayer management in MainActivity (use AudioPlayerService instead)
- ⚠️ Hardcoded strings (migrate to string resources)

### Removed
- ❌ Direct password storage in preferences
- ❌ Duplicate MediaPlayer instances across components
- ❌ Hardcoded text strings from Java files and layouts

### Technical Notes
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 34 (Android 15)
- Requires Java 11 or higher
- Material3 dependency version: 1.11.0
- AndroidX AppCompat: 1.6.1

## [1.0] - Initial Release

### Features
- Basic audio playback functionality
- Login screen with SharedPreferences persistence
- Quick Settings tile integration
- Simple Material Design UI
- File selection from device storage
