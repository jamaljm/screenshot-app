# Building the Android APK

## Prerequisites

You need Android Studio or command-line tools to build.

## Option 1: Android Studio (Easiest)

1. **Install Android Studio** from https://developer.android.com/studio
2. **Open this project** in Android Studio
3. **Wait for Gradle sync** to complete
4. **Build APK:**
   - Menu: Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Or click the hammer icon 🔨
5. **Find APK:** 
   - `app/build/outputs/apk/debug/app-debug.apk`
   - Or check the notification: "APK(s) generated successfully"

## Option 2: Command Line (Faster)

```bash
# Navigate to project directory
cd /root/.openclaw/workspace/screenshot-workflows-app

# Make gradlew executable (if needed)
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

## Option 3: Use Pre-built APK

If you can't build, I can build it for you on GitHub Actions or similar CI/CD.

## After Building

1. **Copy APK to your server:**
   ```bash
   cp app/build/outputs/apk/debug/app-debug.apk /var/www/html/screenshot-workflows.apk
   ```

2. **Make it downloadable:**
   ```bash
   # Add to Caddy config
   echo "files.lethimbuild.com {
       root * /var/www/html
       file_server
   }" >> /etc/caddy/Caddyfile
   
   systemctl reload caddy
   ```

3. **Download on Android:**
   - Open browser on phone
   - Go to: https://files.lethimbuild.com/screenshot-workflows.apk
   - Download → Install

## Signing (Production)

For hackathon demo, unsigned debug APK is fine. For production:

```bash
# Generate keystore
keytool -genkey -v -keystore screenshot-workflows.keystore -alias inmystory -keyalg RSA -keysize 2048 -validity 10000

# Build signed APK
./gradlew assembleRelease -Pandroid.injected.signing.store.file=screenshot-workflows.keystore -Pandroid.injected.signing.store.password=YOUR_PASSWORD -Pandroid.injected.signing.key.alias=inmystory -Pandroid.injected.signing.key.password=YOUR_PASSWORD
```

## Troubleshooting

**Gradle sync fails:**
- Check internet connection (downloads dependencies)
- Try: File → Invalidate Caches / Restart

**Build fails:**
- Check Android SDK is installed
- Required: SDK 34, Build Tools 34.0.0
- Install via: Tools → SDK Manager

**APK too large:**
- Current size: ~5-7MB
- Can reduce with R8/ProGuard (minifyEnabled true)

## Next Steps

After building APK:
1. Upload to server (see above)
2. Follow INSTALLATION.md on phone
3. Test with real screenshots!
