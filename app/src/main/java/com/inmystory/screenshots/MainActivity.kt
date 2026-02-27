package com.inmystory.screenshots

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var enableSwitch: Switch
    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        enableSwitch = findViewById(R.id.enableSwitch)
        testButton = findViewById(R.id.testButton)

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
            Toast.makeText(this, "Testing API connection...", Toast.LENGTH_SHORT).show()
            // TODO: Add API test call
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
}
