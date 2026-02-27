package com.inmystory.screenshots

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class ScreenshotNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        
        sbn?.let {
            // Check if it's a screenshot notification
            val packageName = it.packageName
            val text = it.notification?.extras?.getCharSequence("android.text")?.toString() ?: ""
            val title = it.notification?.extras?.getCharSequence("android.title")?.toString() ?: ""
            
            // Detect screenshot notification
            if ((packageName.contains("systemui", ignoreCase = true) || 
                 packageName.contains("screenshot", ignoreCase = true) ||
                 packageName == "android") &&
                (text.contains("screenshot", ignoreCase = true) || 
                 title.contains("screenshot", ignoreCase = true) ||
                 text.contains("Screen capture", ignoreCase = true) ||
                 title.contains("Screen capture", ignoreCase = true))) {
                
                // Screenshot detected! Show menu
                val intent = Intent(this, ActionMenuActivity::class.java)
                intent.putExtra("screenshot_path", "/sdcard/screenshot_detected.png")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}
