# 🚀 Quick Start - Audio Quick Play trên AndroidIDE

**Cách chạy app trong 5 phút (with AndroidIDE)**

---

## 1️⃣ Chuẩn Bị

```bash
# Vào terminal (adb shell hoặc AndroidIDE terminal)
$ cd ~/AndroidIDEProjects/
$ git clone https://github.com/Parzival235/debug.git audio-quick-play
$ cd audio-quick-play
```

---

## 2️⃣ Mở trong AndroidIDE

1. **Launch AndroidIDE**
2. **File** → **Open Project**
3. **Chọn** `audio-quick-play` folder
4. **Tap Open** (đợi sync Gradle ~30-60s lần đầu)

---

## 3️⃣ Build & Run

**Option A: Dùng UI**
```
Build → Build Project (chờ success)
Run → Run (auto install + launch)
```

**Option B: Dùng Script** (nhanh hơn)
```bash
cd audio-quick-play
bash build.sh run    # Build + install + launch
```

---

## 4️⃣ Test App

1. **Login**: Username + password (3+ & 4+ chars)
2. **Pick File**: Tap "Chọn file" → select MP3/WAV
3. **Play**: Tap "Phát ngay"
4. **Controls**:
   - 🔄 Repeat button (OFF/ONE/ALL)
   - 🔊 Seekbar (drag to jump)
   - ⏸️ Play/Pause toggle
   - 🚪 "Đăng xuất" (Logout)

---

## ⚡ Build Commands

```bash
bash build.sh clean     # Clean build cache
bash build.sh debug     # Build APK only
bash build.sh install   # Build + install
bash build.sh run       # Build + install + launch
```

---

## 🛠️ Troubleshooting

| Problem | Fix |
|---------|-----|
| **Build fails** | Settings → Clear Cache → Build again |
| **"Java not found"** | Check Java 11+ installed: `java -version` |
| **App crashes** | View → Logcat (look for RED errors) |
| **Slow build** | Increase Gradle heap in Settings |

---

## 📚 Full Docs

- **Setup Guide**: See `ANDROIDIDE_GUIDE.md`
- **Features**: See `ADVANCED_FEATURES.md`
- **Main Docs**: See `README.md`

---

**That's it! Enjoy Audio Quick Play 🎵**
