package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class EndGame : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    lateinit var winOrLose : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        val playAgain : Button = findViewById(R.id.button)
        val exit : Button = findViewById(R.id.exit)
        val msg: TextView = findViewById(R.id.msg)
        winOrLose = intent.getStringExtra("loseOrWin").toString()
        var imageView : ImageView = findViewById(R.id.imageView3)
        val info : Button = findViewById(R.id.info)


        Log.i("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",winOrLose)
        if (winOrLose.get(0)== '*') {

            winOrLose= winOrLose.substring(1)
            msg.text = "You win"
            mediaPlayer = MediaPlayer.create(this, R.raw.winsound)
            Glide.with(this)
                .load(R.drawable.winner)
                .into(imageView);
        } else {
            msg.text = "You lose\n the word was:"+winOrLose.toString()
            mediaPlayer = MediaPlayer.create(this, R.raw.gameover)
            Glide.with(this)
                .load(R.drawable.died)
                .into(imageView);

        }


        mediaPlayer?.start()
        playAgain.setOnClickListener(onClickListenerLevel)
        info.setOnClickListener(onClickListenerWordInfo)
        exit.setOnClickListener(onClickListenerExit)
    }


    private val onClickListenerLevel: View.OnClickListener = View.OnClickListener { pass_to_level_activity() }
    private val onClickListenerWordInfo: View.OnClickListener = View.OnClickListener { pass_to_word_info() }
    private val onClickListenerExit: View.OnClickListener = View.OnClickListener {exit() }

    fun pass_to_level_activity (){
        val intent= Intent(this,MainActivity2::class.java)
        startActivity(intent)
    }

    fun pass_to_word_info (){
        val intent= Intent(this,WordInfo::class.java)
        intent.putExtra("word",winOrLose)
        startActivity(intent)
    }
    fun exit(){
        finishAffinity();
        System.exit(0);
    }



    override fun onBackPressed() {
        // Handle back press if needed
    }



    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
