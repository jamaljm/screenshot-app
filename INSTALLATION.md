# Installing Screenshot Workflows App

**NO LAPTOP NEEDED!** Install directly on your Android phone.

---

## 📱 Step-by-Step Installation

### Step 1: Enable Unknown Sources (1 minute)

**Android 8.0+:**
1. Settings → Security (or Biometrics & Security)
2. Find "Install unknown apps"
3. Select your browser (Chrome/Firefox)
4. Toggle ON: "Allow from this source"

**Older Android:**
1. Settings → Security
2. Toggle ON: "Unknown sources"
3. Confirm the warning

### Step 2: Download APK (1 minute)

**On your Android phone:**
1. Open Chrome/Firefox
2. Go to: `https://lethimbuild.com/download/screenshot-workflows.apk`
3. Download will start automatically
4. Check notification: "Download complete"

**Alternative:** Scan QR code (I can generate one)

### Step 3: Install APK (30 seconds)

1. **Notification method:**
   - Tap "Download complete" notification
   - Tap "Install"
   - Tap "Install" again to confirm

2. **File manager method:**
   - Open Files app
   - Go to Downloads folder
   - Tap `screenshot-workflows.apk`
   - Tap "Install"

### Step 4: Grant Permissions (1 minute)

After opening the app for the first time:

1. **Storage permission:**
   - Popup: "Allow Screenshot Workflows to access photos and media?"
   - Tap "Allow"

2. **Notification permission:**
   - Popup: "Allow notifications?"
   - Tap "Allow"

3. **Battery optimization (important!):**
   - Settings → Battery → Battery optimization
   - Find "Screenshot Workflows"
   - Select "Don't optimize"
   - This prevents Android from killing the service

### Step 5: Enable Detection (10 seconds)

1. Open the app
2. Toggle ON: "Enable Screenshot Detection"
3. Status changes to: "✓ Active - Monitoring screenshots"
4. You'll see a persistent notification: "Monitoring screenshots..."

---

## 🧪 Test It!

### Test 1: Take Any Screenshot
```
1. Go to any app (Instagram, Chrome, etc.)
2. Take screenshot (Power + Volume Down)
3. Menu appears: "What to do with this screenshot?"
4. Select any action to test
```

### Test 2: Food Bill
```
1. Open Google Images
2. Search "restaurant bill receipt"
3. Screenshot a bill
4. Select "📁 Organize Bill"
5. Check: Files → Documents → Bills
```

### Test 3: Instagram Event
```
1. Find any event post on Instagram
2. Screenshot it
3. Select "📅 Add to Calendar"
4. Calendar opens with details pre-filled
```

### Test 4: Article
```
1. Open any tech article in Chrome
2. Screenshot it
3. Select "💬 Share to WhatsApp"
4. WhatsApp opens with formatted message
```

### Test 5: Movie
```
1. Google any movie name
2. Screenshot the result card
3. Select "🎬 Add to Letterboxd"
4. Letterboxd (or browser) opens with search
```

---

## 🔧 Configuration

### WhatsApp Friends (Optional)

To customize the 3 friends for article sharing:

1. Open app
2. Go to Settings (future feature)
3. Enter phone numbers: +919876543210, etc.

**For now:** It opens WhatsApp with the message, you manually select recipients.

### Bills Folder Location

Default: `/sdcard/Documents/Bills/`

Files are organized like:
```
/sdcard/Documents/Bills/
├── 2026-02/
│   ├── restaurant/
│   │   └── Starbucks_2026-02-27.png
│   ├── cafe/
│   └── grocery/
└── 2026-01/
```

---

## 🐛 Troubleshooting

### "App not installed"
- Make sure "Unknown sources" is enabled for your browser
- Try downloading again (file might be corrupted)
- Check if you have enough storage space

### Menu doesn't appear after screenshot
- Check app status: Should say "✓ Active"
- Disable battery optimization for the app
- Restart the app
- Take screenshot again

### Permissions denied
- Go to: Settings → Apps → Screenshot Workflows → Permissions
- Enable: Storage, Notifications

### API timeout / No response
- Check internet connection
- Test API: Open browser, go to https://screenshots.lethimbuild.com/health
- Should show: {"status":"ok","service":"screenshot-api"}

### Service stops automatically
- **Battery optimization:** Most common issue!
  - Settings → Battery → Screenshot Workflows → Don't optimize
- **Background restrictions:**
  - Settings → Apps → Screenshot Workflows → Battery → Background restriction: OFF
- **Auto-start:**
  - Some phones (Xiaomi, Oppo) have auto-start restrictions
  - Settings → Permissions → Auto-start → Enable for Screenshot Workflows

---

## 🔐 Permissions Explained

**Why each permission is needed:**

1. **Storage (READ_EXTERNAL_STORAGE):**
   - To detect new screenshots
   - To read screenshot files for processing
   - To organize bills into folders

2. **Notifications:**
   - To show "Screenshot detected" alerts
   - To display action results
   - Required for foreground service

3. **Internet:**
   - To send screenshots to your API
   - To receive processed results
   - All data sent to YOUR server (not third-party)

---

## 🎓 First-Time Setup Checklist

- [ ] Enable "Unknown sources" for browser
- [ ] Download APK from lethimbuild.com
- [ ] Install APK
- [ ] Grant Storage permission
- [ ] Grant Notifications permission
- [ ] Disable battery optimization
- [ ] Toggle ON "Enable Screenshot Detection"
- [ ] Test with any screenshot
- [ ] Verify menu appears
- [ ] Test each of the 4 workflows

---

## 🚀 Ready for Hackathon Demo

Once installed and tested:

1. **Prepare 4 sample screenshots** before the demo:
   - Food bill
   - Instagram event
   - Tech article in Chrome
   - Movie on Google

2. **Practice the flow:**
   - Take screenshot → Menu → Select action → Result
   - Each workflow should take < 30 seconds

3. **Backup plan:**
   - If live demo fails, have pre-recorded video
   - Or show screenshots of the results

---

## 💡 Pro Tips

1. **Test before the demo** - Don't do live API calls for first time on stage
2. **Good internet** - API needs connection to work
3. **Charge phone** - Background service uses battery
4. **Clear notifications** - Looks cleaner during demo
5. **Use real data** - Real bills/events are more impressive than samples

---

## 📊 App Info

- **Package:** com.inmystory.screenshots
- **Size:** ~5-7MB
- **Android:** Requires 7.0+ (API level 24+)
- **Permissions:** Storage, Notifications, Internet
- **Background:** Runs as foreground service (persistent notification)
- **API:** screenshots.lethimbuild.com (your VPS)

---

## 🔄 Updates

To update to a new version:
1. Download new APK
2. Install over existing app (data preserved)
3. No need to uninstall first

---

## 🆘 Support

**Issues?** Check:
1. App status: "✓ Active"
2. Internet connection
3. API health: https://screenshots.lethimbuild.com/health
4. Battery optimization disabled
5. Permissions granted

**Still broken?** DM me or check logs (requires developer mode + ADB)

---

**Download:** https://lethimbuild.com/download/screenshot-workflows.apk  
**Source:** /root/.openclaw/workspace/screenshot-workflows-app/

**Let's win this hackathon!** 🔥
