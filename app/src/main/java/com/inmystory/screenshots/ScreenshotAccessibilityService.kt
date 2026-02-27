package com.inmystory.screenshots

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.util.Log

class ScreenshotAccessibilityService : AccessibilityService() {

    private val TAG = "ScreenshotAccessibility"
    private var lastEventTime = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            // Detect screenshot by notification or toast
            val text = event.text.toString().lowercase()
            val className = event.className?.toString() ?: ""
            
            Log.d(TAG, "Event: type=${event.eventType}, text=$text, class=$className")
            
            // Check for screenshot indicators
            if (event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED ||
                event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                
                if (text.contains("screenshot") || 
                    text.contains("screen capture") ||
                    text.contains("capture") && text.contains("screen")) {
                    
                    // Prevent duplicate triggers
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastEventTime > 2000) {
                        lastEventTime = currentTime
                        
                        Log.d(TAG, "Screenshot detected!")
                        showMenu()
                    }
                }
            }
        }
    }

    private fun showMenu() {
        try {
            val intent = Intent(this, ActionMenuActivity::class.java)
            intent.putExtra("screenshot_path", "/sdcard/screenshot_detected.png")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error showing menu", e)
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility service connected!")
    }
}
