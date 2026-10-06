package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var scoreText : TextView
    private lateinit var messageText : TextView
    private lateinit var playAgainButton : Button
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result)

        scoreText = findViewById(R.id.scoreText)
        messageText = findViewById(R.id.messageText)
        playAgainButton = findViewById(R.id.playAgainButton)
        image = findViewById(R.id.resultImage)

        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 0)
        scoreText.text = "$score/$total"

        messageText.text = when {
            score == total -> "Perfect! You're a Space Master! 🌟"
            score >= total / 2 -> "Good job, Space Explorer! 🚀"
            else -> "Nice try, Space Cadet! 🛰️"
        }

        image.setImageResource(when {
            score == total -> R.drawable.excelent
            score >= total / 2 -> R.drawable.goodjob
            else -> R.drawable.nicetry
        })

        playAgainButton.setOnClickListener {
            takeScreenshot()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun takeScreenshot() {
        val rootView = window.decorView.rootView

        rootView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(rootView.drawingCache)
        rootView.isDrawingCacheEnabled = false

        val filename = "screenshot_${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }

        bitmap.recycle()
    }
}