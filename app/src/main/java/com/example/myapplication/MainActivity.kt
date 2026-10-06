package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : ComponentActivity() {

    private lateinit var questionText : TextView
    private lateinit var questionNumberText : TextView
    private lateinit var optionsGroup : RadioGroup
    private lateinit var nextButton : Button

    private val questions = arrayOf(
        "Which planet is known as the Red Planet?",
        "What is the largest planet in our solar system?",
        "Which galaxy is home to Earth and our solar system?",
        "What is the closest star to Earth?",
        "Which planet has the most prominent rings in our solar system?"
    )

    private val options = arrayOf(
        arrayOf("Venus", "Mars", "Jupiter", "Saturn"),
        arrayOf("Jupiter", "Saturn", "Neptune", "Uranus"),
        arrayOf("Andromeda", "Milky Way", "Whirlpool", "Sombrero"),
        arrayOf("Proxima Centauri", "Sirius", "The Sun", "Alpha Centauri"),
        arrayOf("Jupiter", "Uranus", "Saturn", "Neptune")
    )

    private val correctAnswers = intArrayOf(
        1, 0, 1, 2, 2
    )

    private var currentQuestion = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionText = findViewById(R.id.questionText)
        questionNumberText = findViewById(R.id.questionNumberText)
        optionsGroup = findViewById(R.id.optionsGroup)
        nextButton = findViewById(R.id.nextButton)

        showQuestion()

        nextButton.setOnClickListener {
            val answered = checkAnswer()
            if (!answered) {
                return@setOnClickListener
            }
            if (currentQuestion < questions.size - 1) {
                currentQuestion++
                showQuestion()
            } else {
                openResultScreen()
            }
        }
    }

    private fun showQuestion() {
        questionText.text = questions[currentQuestion]
        questionNumberText.text = "${currentQuestion + 1}/${questions.size}"

        val radioButtons = arrayOf(
            findViewById<RadioButton>(R.id.option1),
            findViewById<RadioButton>(R.id.option2),
            findViewById<RadioButton>(R.id.option3),
            findViewById<RadioButton>(R.id.option4)
        )

        for (i in radioButtons.indices) {
            radioButtons[i].text = options[currentQuestion][i]
        }
        optionsGroup.clearCheck()
    }

    private fun checkAnswer(): Boolean {
        val selectedId = optionsGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Select an Answer", Toast.LENGTH_SHORT).show()
            return false
        }
        val selectedButton = findViewById<RadioButton>(selectedId)
        val selectedIndex = options[currentQuestion].indexOf(
            selectedButton.text.toString()
        )
        if (selectedIndex == correctAnswers[currentQuestion]) {
            score++

            val snackbar = Snackbar.make(findViewById(android.R.id.content), "Correct! 🌟", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(Color.parseColor("#161B33"))
            val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.gravity = Gravity.CENTER
            textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_correct,
                0,
                0,
                0
            )
            textView.setCompoundDrawablePadding(16)
            textView.setTextColor(Color.parseColor("#4CC9F0"))
            snackbar.show()

        } else {
            val correctAnswerText = options[currentQuestion][correctAnswers[currentQuestion]]
            val snackbar = Snackbar.make(findViewById(android.R.id.content), "Wrong! Correct: $correctAnswerText", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(Color.parseColor("#161B33"))
            val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.gravity = Gravity.CENTER
            textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_wrong,
                0,
                0,
                0
            )
            textView.setCompoundDrawablePadding(16)
            textView.setTextColor(Color.parseColor("#FFD166"))
            snackbar.show()
        }
        return true
    }

    private fun openResultScreen() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL", questions.size)

        startActivity(intent)
        finish()
    }
}