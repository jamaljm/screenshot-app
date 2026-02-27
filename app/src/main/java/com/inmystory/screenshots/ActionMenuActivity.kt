package com.inmystory.screenshots

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ActionMenuActivity : AppCompatActivity() {

    private lateinit var screenshotPath: String
    private val api = ApiClient.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        screenshotPath = intent.getStringExtra("screenshot_path") ?: run {
            finish()
            return
        }

        showActionMenu()
    }

    private fun showActionMenu() {
        val actions = arrayOf(
            "📁 Organize Bill",
            "📅 Add to Calendar",
            "💬 Share to WhatsApp",
            "🎬 Add to Letterboxd",
            "❌ Cancel"
        )

        AlertDialog.Builder(this)
            .setTitle("What to do with this screenshot?")
            .setItems(actions) { dialog, which ->
                when (which) {
                    0 -> handleOrganizeBill()
                    1 -> handleCalendarEvent()
                    2 -> handleShareArticle()
                    3 -> handleLetterboxd()
                    4 -> finish()
                }
                dialog.dismiss()
            }
            .setOnCancelListener { finish() }
            .show()
    }

    private fun handleOrganizeBill() {
        showProgress("Organizing bill...")
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(screenshotPath)
                val result = api.processScreenshot(file, "organize_bill")
                
                withContext(Dispatchers.Main) {
                    if (result.success) {
                        // Create folder and move file
                        val folderPath = result.folderPath ?: "/sdcard/Documents/Bills"
                        val folder = File(folderPath)
                        folder.mkdirs()
                        
                        val newFile = File(folder, result.suggestedFilename ?: file.name)
                        file.renameTo(newFile)
                        
                        showSuccess("Bill organized!\n${result.data?.get("restaurant")} - ${result.data?.get("total")}\nSaved to: $folderPath")
                    } else {
                        showError("Could not process bill")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                }
            }
        }
    }

    private fun handleCalendarEvent() {
        showProgress("Extracting event details...")
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(screenshotPath)
                val result = api.processScreenshot(file, "calendar_event")
                
                withContext(Dispatchers.Main) {
                    if (result.success && result.calendarIntent != null) {
                        // Open calendar with intent
                        val intent = Intent(Intent.ACTION_INSERT).apply {
                            data = Uri.parse("content://com.android.calendar/events")
                            putExtra("title", result.calendarIntent.title)
                            putExtra("eventLocation", result.calendarIntent.location)
                            putExtra("description", result.calendarIntent.description)
                        }
                        
                        try {
                            startActivity(intent)
                            showSuccess("Opening calendar...")
                        } catch (e: Exception) {
                            showError("Could not open calendar")
                        }
                    } else {
                        showError("Could not extract event details")
                    }
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                }
            }
        }
    }

    private fun handleShareArticle() {
        showProgress("Extracting article...")
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(screenshotPath)
                val result = api.processScreenshot(file, "share_article")
                
                withContext(Dispatchers.Main) {
                    if (result.success && result.shareText != null) {
                        // Share to WhatsApp (hardcoded 3 friends for now)
                        val friends = listOf("+919876543210", "+918765432109", "+917654321098")
                        
                        // For demo: just share to WhatsApp without specific number
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            `package` = "com.whatsapp"
                            putExtra(Intent.EXTRA_TEXT, result.shareText)
                        }
                        
                        try {
                            startActivity(intent)
                            showSuccess("Opening WhatsApp...")
                        } catch (e: Exception) {
                            // Fallback to generic share
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, result.shareText)
                            }
                            startActivity(Intent.createChooser(shareIntent, "Share article"))
                        }
                    } else {
                        showError("Could not extract article")
                    }
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                }
            }
        }
    }

    private fun handleLetterboxd() {
        showProgress("Finding movie...")
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = File(screenshotPath)
                val result = api.processScreenshot(file, "letterboxd")
                
                withContext(Dispatchers.Main) {
                    if (result.success && result.searchQuery != null) {
                        // Try opening Letterboxd app
                        val letterboxdIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("letterboxd://search?query=${result.searchQuery}")
                        }
                        
                        try {
                            startActivity(letterboxdIntent)
                            showSuccess("Opening Letterboxd...")
                        } catch (e: Exception) {
                            // Fallback to web
                            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://letterboxd.com/search/${result.searchQuery}/")
                            }
                            startActivity(webIntent)
                        }
                    } else {
                        showError("Could not find movie")
                    }
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                }
            }
        }
    }

    private fun showProgress(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess(message: String) {
        AlertDialog.Builder(this)
            .setTitle("✓ Success")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> finish() }
            .show()
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("✗ Error")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> finish() }
            .show()
    }
}
