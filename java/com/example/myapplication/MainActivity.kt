package com.example.myapplication

import android.R.attr.action
import android.R.id
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        val playSingleMode: Button = findViewById<View>(R.id.start) as Button
        playSingleMode.setOnClickListener(onClickListener)
        val playMultiOffMode: Button = findViewById(R.id.button2)
        playMultiOffMode.setOnClickListener(onClickListener2)
        val playMultiOnlMode: Button = findViewById(R.id.button3)
        playMultiOnlMode.setOnClickListener(onClickListener5)
        val editFile : ImageView = findViewById(R.id.editFile)
        editFile.setOnClickListener(onClickListener3)
        val topScore : ImageView = findViewById(R.id.topScore)
        topScore.setOnClickListener(onClickListener4)
    }


    val onClickListener: View.OnClickListener = View.OnClickListener { pass_to_level_activity() }
    val onClickListener2: View.OnClickListener = View.OnClickListener { pass_to_multi_players() }
    val onClickListener3: View.OnClickListener = View.OnClickListener {pass_to_edit_file() }
    val onClickListener4: View.OnClickListener = View.OnClickListener { pass_to_top_score() }
    val onClickListener5: View.OnClickListener = View.OnClickListener { pass_to_play_online() }


    fun pass_to_level_activity (){

        //move to the next activity
        val intent= Intent(this,MainActivity2::class.java)
        startActivity(intent)
    }


    fun pass_to_multi_players (){

        //move to the next activity
        val intent= Intent(this,MultiPlayers::class.java)
        startActivity(intent)
    }

    fun pass_to_play_online() {
        val options = arrayOf("Create Room", "Enter Room")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, which ->

            when (which) {
                0 -> {
                    // Create Room
                    val createRoomIntent = Intent(this, PlayOnline::class.java)
                    createRoomIntent.putExtra("roomId", "no_id")
                    createRoomIntent.putExtra("action", "create")
                    startActivity(createRoomIntent)
                }
                1 -> {
                    // Enter Room
                    val enterRoomIntent = Intent(this, PlayOnline::class.java)
                    enterRoomIntent.putExtra("roomId", "no_id")
                    enterRoomIntent.putExtra("action", "enter")
                    startActivity(enterRoomIntent)
                }
            }
        }

        builder.show()
    }







    fun pass_to_edit_file (){

        //move to the next activity
        val intent= Intent(this,EditFile::class.java)
        startActivity(intent)
    }
    fun pass_to_top_score (){

        //move to the next activity
        val intent= Intent(this,HighScores::class.java)
        startActivity(intent)
    }

}
data class DialogOption(val text: String, val icon: Int)
