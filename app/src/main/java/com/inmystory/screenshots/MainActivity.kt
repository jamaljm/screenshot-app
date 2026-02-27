package com.inmystory.screenshots

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var enableSwitch: SwitchCompat
    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
            return
        }

        statusText = findViewById(R.id.statusText)
        enableSwitch = findViewById(R.id.enableSwitch)
        testButton = findViewById(R.id.testButton)

        // Check notification listener permission
        checkNotificationListenerPermission()
        
        // Check and request permissions
        checkPermissions()

        // Load saved state
        val prefs = getSharedPreferences("ScreenshotWorkflows", MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("service_enabled", false)
        enableSwitch.isChecked = isEnabled

        updateStatus(isEnabled)

        // Enable/disable service
        enableSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("service_enabled", isChecked).apply()
            if (isChecked) {
                startScreenshotService()
            } else {
                stopScreenshotService()
            }
            updateStatus(isChecked)
        }

        // Test API connection
        testButton.setOnClickListener {
            // Test by showing the menu directly
            val intent = Intent(this, ActionMenuActivity::class.java)
            intent.putExtra("screenshot_path", "/sdcard/test.png")
            startActivity(intent)
            Toast.makeText(this, "Opening test menu...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1001)
        }
    }

    private fun startScreenshotService() {
        val intent = Intent(this, ScreenshotDetectionService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Screenshot detection enabled", Toast.LENGTH_SHORT).show()
    }

    private fun stopScreenshotService() {
        val intent = Intent(this, ScreenshotDetectionService::class.java)
        stopService(intent)
        Toast.makeText(this, "Screenshot detection disabled", Toast.LENGTH_SHORT).show()
    }

    private fun updateStatus(isEnabled: Boolean) {
        if (isEnabled) {
            statusText.text = "✓ Active - Monitoring screenshots"
            statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        } else {
            statusText.text = "✗ Inactive - Enable to start"
            statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions required for app to work", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun checkNotificationListenerPermission() {
        // Check if Accessibility Service is enabled
        val accessibilityEnabled = try {
            android.provider.Settings.Secure.getInt(
                contentResolver,
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED
            ) == 1
        } catch (e: Exception) {
            false
        }
        
        val enabledServices = android.provider.Settings.Secure.getString(
            contentResolver,
            android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        
        val isOurServiceEnabled = enabledServices?.contains(packageName) == true
        
        if (!accessibilityEnabled || !isOurServiceEnabled) {
            // Not enabled, show message and open settings
            Toast.makeText(
                this,
                "Please enable Accessibility for Screenshot Workflows\n\nThis allows automatic screenshot detection",
                Toast.LENGTH_LONG
            ).show()
            
            // Open accessibility settings after delay
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                try {
                    startActivity(Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 2000)
        }
    }
}
