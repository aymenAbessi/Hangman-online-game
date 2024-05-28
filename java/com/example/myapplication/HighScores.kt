package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.widget.TextView


class HighScores : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        val scoreManager = ScoreManager(this)
        val topScores = scoreManager.topScores

        updateTopScoresViews(topScores)
    }

    private fun updateTopScoresViews(topScores: List<TopScore>) {
        updateTopScoreViews(topScores.find { it.level == "Easy" }, R.id.textViewPlayerNameEasy, R.id.textViewLevelEasy, R.id.textViewTimesPlayedEasy, R.id.textViewWordEasy,R.id.textViewTimeEasy)
        updateTopScoreViews(topScores.find { it.level == "Medium" }, R.id.textViewPlayerNameMedium, R.id.textViewLevelMedium, R.id.textViewTimesPlayedMedium, R.id.textViewWordMedium,R.id.textViewTimeMedium)
        updateTopScoreViews(topScores.find { it.level == "Hard" }, R.id.textViewPlayerNameHard, R.id.textViewLevelHard, R.id.textViewTimesPlayedHard, R.id.textViewWordHard,R.id.textViewTimeHard)
    }

    private fun updateTopScoreViews(topScore: TopScore?, playerNameId: Int, levelId: Int, livesLeftId: Int, wordId: Int,timeId : Int) {
        topScore?.let {
            // Update views with top score data
            findViewById<TextView>(playerNameId).text = "Player Name: ${it.playerName}"
            findViewById<TextView>(levelId).text = "Level: ${it.level}"
            findViewById<TextView>(livesLeftId).text = "NB of lifes rest: ${it.livesLeft}"
            findViewById<TextView>(wordId).text = "Word: ${it.word}"
            findViewById<TextView>(timeId).text = "Remaining Time: ${it.time} seconds"
        }
    }


}

