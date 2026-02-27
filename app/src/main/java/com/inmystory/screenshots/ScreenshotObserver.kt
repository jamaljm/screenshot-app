package com.inmystory.screenshots

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import java.io.File

class ScreenshotObserver(
    private val context: Context,
    private val onScreenshotDetected: (File) -> Unit
) : ContentObserver(Handler(Looper.getMainLooper())) {

    private var lastScreenshotPath: String? = null

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        
        uri?.let {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME
            )
            
            context.contentResolver.query(
                it,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    
                    if (pathIndex != -1 && nameIndex != -1) {
                        val path = cursor.getString(pathIndex)
                        val name = cursor.getString(nameIndex)
                        
                        // Check if it's a screenshot (by path or name)
                        if (path != null && 
                            (path.contains("screenshot", ignoreCase = true) || 
                             name.contains("screenshot", ignoreCase = true)) &&
                            path != lastScreenshotPath) {
                            
                            lastScreenshotPath = path
                            val file = File(path)
                            if (file.exists()) {
                                onScreenshotDetected(file)
                            }
                        }
                    }
                }
            }
        }
    }

    fun register() {
        context.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            this
        )
    }

    fun unregister() {
        context.contentResolver.unregisterContentObserver(this)
    }
}
