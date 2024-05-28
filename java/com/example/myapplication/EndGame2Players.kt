package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import com.bumptech.glide.Glide

class EndGame2Players : AppCompatActivity() {
    private lateinit var photo1ImageView: ImageView
    private lateinit var message1TextView: TextView
    private lateinit var photo2ImageView: ImageView
    private lateinit var message2TextView: TextView
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var exit:Button
    private lateinit var playAgain:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game2_players)


        photo1ImageView = findViewById(R.id.photo1ImageView)
        message1TextView = findViewById(R.id.message1TextView)
        photo2ImageView = findViewById(R.id.photo2ImageView)
        message2TextView = findViewById(R.id.message2TextView)
        exit= findViewById(R.id.exit2)
        playAgain=findViewById(R.id.button2)

        val loseOrWin = intent.getStringExtra("loseOrWin")
        val winnerName = intent.getStringExtra("winner")
        val loserName = intent.getStringExtra("loser")



            if (loseOrWin != null && winnerName!=null && loserName!=null) {

                when (loseOrWin) {
                    "both win" -> {
                        Glide.with(this)
                            .load(R.drawable.bothwin)
                            .into(photo1ImageView)

                        message1TextView.visibility = View.VISIBLE
                        message1TextView.text = "It's a tie! Both players win."
                        mediaPlayer = MediaPlayer.create(this, R.raw.twowin)
                    }
                    "player 1" -> {

                        Glide.with(this)
                            .load(R.drawable.win1)
                            .into(photo1ImageView)
                        Glide.with(this)
                            .load(R.drawable.lose1)
                            .into(photo2ImageView)

                        message1TextView.visibility = View.VISIBLE
                        photo2ImageView.visibility = View.VISIBLE
                        photo1ImageView.visibility = View.VISIBLE
                        message2TextView.visibility = View.VISIBLE

                        message1TextView.text = "Player 1 ($winnerName) wins!"
                        message2TextView.text = "Player 2 ($loserName) loses."
                        mediaPlayer = MediaPlayer.create(this, R.raw.onewin)
                    }
                    "player 2" -> {
                        // Load GIF into the ImageView using Glide
                        Glide.with(this)
                            .load(R.drawable.win2)
                            .into(photo2ImageView)

                        Glide.with(this)
                            .load(R.drawable.lose2)
                            .into(photo1ImageView)
                        message1TextView.visibility = View.VISIBLE
                        photo2ImageView.visibility = View.VISIBLE
                        photo1ImageView.visibility = View.VISIBLE
                        message2TextView.visibility = View.VISIBLE

                        message1TextView.text = "Player 1 ($loserName) loses."
                        message2TextView.text = "Player 2 ($winnerName) wins!"
                        mediaPlayer = MediaPlayer.create(this, R.raw.onewin)
                    }
                    else -> {
                        // Load GIF into the ImageView using Glide
                        Glide.with(this)
                            .load(R.drawable.bothlose)
                            .into(photo1ImageView)

                        message1TextView.visibility = View.VISIBLE
                        message1TextView.text = "Game Over, you both lose!"
                        mediaPlayer = MediaPlayer.create(this, R.raw.twolose)
                    }
                }
                mediaPlayer?.start()
            }

            exit.setOnClickListener(onClickListenerExit)
            playAgain.setOnClickListener(onClickListenerPlayAgain)

    }
    private val onClickListenerExit:View.OnClickListener=View.OnClickListener { exit() }
    private val onClickListenerPlayAgain : View.OnClickListener=View.OnClickListener { playAgain() }

    fun exit(){
        finishAffinity()
        System.exit(0)
    }

    fun playAgain(){
        val intent = Intent(this,MultiPlayers::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {

    }

}