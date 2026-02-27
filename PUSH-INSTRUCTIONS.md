# 🚀 Push to GitHub

Your repo is ready: https://github.com/jamaljm/screenshot-app.git

## To Push Code:

```bash
cd /root/.openclaw/workspace/screenshot-workflows-app

git push -u origin main
```

## Authentication:

When prompted:
- **Username:** jamaljm
- **Password:** YOUR_PERSONAL_ACCESS_TOKEN (not your GitHub password!)

## Don't Have Token?

Create one:
1. https://github.com/settings/tokens
2. Generate new token (classic)
3. Check: `repo` (full control)
4. Generate → Copy token
5. Use as password when pushing

## After Push:

1. Go to: https://github.com/jamaljm/screenshot-app/actions
2. Watch "Build Android APK" run (~10 minutes)
3. When done, click workflow → Download "app-debug" artifact
4. Extract ZIP → Get app-debug.apk
5. Upload to server: `mv app-debug.apk /var/www/html/screenshot-workflows.apk`
6. Install on phone: https://files.lethimbuild.com/screenshot-workflows.apk

## That's It!

After push, everything is automatic! 🎉
