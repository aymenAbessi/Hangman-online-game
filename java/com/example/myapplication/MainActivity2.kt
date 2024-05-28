package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import java.io.File

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()

        val radioGroup: RadioGroup = findViewById(R.id.difficultyRadioGroup)
        val button: Button = findViewById<View>(R.id.next) as Button
        val userName:EditText = findViewById(R.id.userName)
        button.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId

            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)
                val selectedDifficulty = selectedRadioButton.text.toString()


                Log.d("MainActivity", "Selected Difficulty: $selectedDifficulty")


                val intent = Intent(this, MainActivity3::class.java)
                intent.putExtra("selectedDifficulty", selectedDifficulty)
                Log.i("before xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",userName.text.toString())
                intent.putExtra("name",userName.text.toString())
                startActivity(intent)
            } else {
                Log.d("MainActivity", "No radio button selected")
            }
        }
    }



}