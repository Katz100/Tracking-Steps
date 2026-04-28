package com.example.utility.foreground

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ShareStepsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val steps = intent.getIntExtra("steps", 0)

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "I completed $steps steps!")
            type = "text/plain"
        }

        startActivity(Intent.createChooser(sendIntent, "Share your progress"))
        finish()
    }
}