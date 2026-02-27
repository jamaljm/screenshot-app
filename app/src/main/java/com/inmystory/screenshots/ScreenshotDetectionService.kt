package com.inmystory.screenshots

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.FileObserver
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.io.File

class ScreenshotDetectionService : Service() {

    private var fileObserver: FileObserver? = null
    private val NOTIFICATION_ID = 1001
    private val CHANNEL_ID = "screenshot_service"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification("Monitoring screenshots..."))
        startWatchingScreenshots()
    }

    private fun startWatchingScreenshots() {
        val screenshotPaths = listOf(
            "${Environment.getExternalStorageDirectory()}/Pictures/Screenshots",
            "${Environment.getExternalStorageDirectory()}/Screenshots",
            "${Environment.getExternalStorageDirectory()}/DCIM/Screenshots"
        )

        for (path in screenshotPaths) {
            val dir = File(path)
            if (dir.exists()) {
                watchDirectory(dir)
                break
            }
        }
    }

    private fun watchDirectory(directory: File) {
        fileObserver = object : FileObserver(directory.absolutePath, CREATE) {
            override fun onEvent(event: Int, path: String?) {
                if (path != null && (path.endsWith(".png") || path.endsWith(".jpg"))) {
                    val screenshotFile = File(directory, path)
                    onScreenshotDetected(screenshotFile)
                }
            }
        }
        fileObserver?.startWatching()
    }

    private fun onScreenshotDetected(file: File) {
        // Show action menu
        val intent = Intent(this, ActionMenuActivity::class.java)
        intent.putExtra("screenshot_path", file.absolutePath)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        // Update notification
        val notification = createNotification("Screenshot detected!")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Screenshot Detection",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitors for new screenshots"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(text: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Screenshot Workflows")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        fileObserver?.stopWatching()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
