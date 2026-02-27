# 🔧 Troubleshooting Screenshot Detection

## Symptom: No menu appears after screenshot

### Check 1: App Status
- Open app
- Status should show: **"✓ Active - Monitoring screenshots"**
- If shows "✗ Inactive", toggle switch ON

### Check 2: Permissions
**Settings → Apps → Screenshot Workflows → Permissions:**
- ✅ Storage/Files: **Allowed**
- ✅ Notifications: **Allowed**

**For Android 13+:**
- Settings → Apps → Screenshot Workflows → Permissions → Photos and videos: **Allowed**

### Check 3: Battery Optimization
**CRITICAL - Most common issue!**

**Settings → Battery → Screenshot Workflows:**
- Select **"Don't optimize"** or **"Unrestricted"**

**Some phones (Xiaomi, Oppo, Realme):**
- Settings → Battery → App battery saver → Screenshot Workflows → **No restrictions**

### Check 4: Auto-start Permission
**Xiaomi/MIUI:**
- Security → Permissions → Autostart → Screenshot Workflows: **ON**

**Oppo/ColorOS:**
- Settings → Privacy → Permission manager → Autostart → Screenshot Workflows: **ON**

### Check 5: Screenshot Folder
Different phones save screenshots to different locations:
- `/sdcard/Screenshots/`
- `/sdcard/Pictures/Screenshots/`
- `/sdcard/DCIM/Screenshots/`

**To test:**
1. Take a screenshot
2. Open **Files** app
3. Note where it saved
4. If it's NOT in `/sdcard/Screenshots/`, the app might not detect it

### Check 6: Restart App
1. Settings → Apps → Screenshot Workflows → **Force Stop**
2. Open app again
3. Toggle detection **ON**
4. Take screenshot

### Check 7: Restart Phone
Sometimes Android needs a reboot after installing new apps with background services.

### Check 8: Test with System Screenshot
- Use **Power + Volume Down** (standard Android screenshot)
- Don't use screenshot from Quick Settings tile
- Don't use screenshot from app-specific features

## Still Not Working?

### Option A: Check Logs (Developer Mode)
If you enabled developer mode:
1. Settings → Developer options → Bug report
2. Or use `adb logcat | grep Screenshot`

### Option B: Reinstall
1. Uninstall app
2. Download fresh: https://files.lethimbuild.com/screenshot-workflows.apk
3. Install
4. Grant ALL permissions
5. Disable battery optimization
6. Enable detection
7. Test

### Option C: Alternative - Use Built-in Share
As a workaround until service works:
1. Take screenshot
2. Open screenshot in Gallery/Photos
3. Tap **Share**
4. Look for "Screenshot Workflows"
5. Select action

## Common Issues by Phone Brand

### Xiaomi/MIUI:
- **Autostart:** Security → Permissions → Autostart → ON
- **Battery:** Battery saver → App battery saver → No restrictions
- **Background restriction:** Settings → Apps → Manage apps → Screenshot Workflows → Other permissions → Display pop-up windows: **ON**

### Oppo/ColorOS:
- **Auto-launch:** Settings → Privacy → Permission manager → Autostart → ON
- **Battery:** Battery → App energy saver → Screenshot Workflows → Don't optimize

### Samsung/One UI:
- **Sleeping apps:** Settings → Battery → Background usage limits → Remove from sleeping apps
- **Deep sleeping apps:** Remove from deep sleeping apps

### Realme:
- Similar to Oppo/ColorOS

### Stock Android/Pixel:
- Usually works fine, just check battery optimization

## Debug Information to Share

If still not working, share:
1. Phone brand and model
2. Android version
3. Where screenshots are saved (Files app location)
4. Screenshot of app status screen
5. Whether you see persistent notification "Monitoring screenshots..."
