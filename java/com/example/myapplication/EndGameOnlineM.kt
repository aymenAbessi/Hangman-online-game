package com.example.myapplication

import android.content.Context
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
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class EndGameOnlineM : AppCompatActivity() {
    lateinit var msg : TextView
    var roomId=""
    private var mediaPlayer: MediaPlayer? = null
    lateinit var imageView : ImageView
    var player1=true
    val db =   Firebase.database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game_online_m)
        msg=findViewById(R.id.winonl)
        val exit : Button = findViewById(R.id.button4)
        val playAgain : Button = findViewById(R.id.button5)

        imageView=findViewById(R.id.gifToShow)
        roomId=intent.getStringExtra("roomId").toString()
        if(intent.getStringExtra("player1").toString().equals("no"))
            player1=false

        msg(this)
        exit.setOnClickListener(onClickListenerExit)
        playAgain.setOnClickListener(onClickListenerPlayAgain)

    }
    private val onClickListenerPlayAgain: View.OnClickListener = View.OnClickListener { pass_to_play_again_activity() }
    private val onClickListenerExit: View.OnClickListener = View.OnClickListener {exit() }

    fun pass_to_play_again_activity (){
        val intent= Intent(this,PlayOnline::class.java)

        intent.putExtra("roomId",roomId)
        if(player1){
            intent.putExtra("player1OLdId","yes")
              db.child(roomId).child("player1").removeValue()
            db.child(roomId).child("player2").removeValue()

        }

        else{
            intent.putExtra("player1OLdId","no")
            db.child(roomId).child("nbOfDonePlayers").setValue(0)
            db.child(roomId).child("role").setValue(1)
        }


        startActivity(intent)
    }

    fun msg(context: Context){

        var player1Win=true
        var player2Win = true

            db.child(roomId).child("player1").child("win").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange( dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.i("zzzzzzzzzzzzzplayer 111zzzzzzzzzzzzzzzzzz",dataSnapshot.getValue().toString())
                        if(dataSnapshot.getValue().toString().toInt()==0)
                            player1Win=false
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })


            db.child(roomId).child("player2").child("win").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange( dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Log.i("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz",dataSnapshot.getValue().toString())
                        if(dataSnapshot.getValue().toString().toInt()==0)
                            player2Win=false

                        if((player1Win && player2Win) || (!player1Win && !player2Win)){
                            msg.text="DRAW"
                            mediaPlayer = MediaPlayer.create(context, R.raw.draw)
                            Glide.with(context)
                                .load(R.drawable.draw)
                                .into(imageView);
                        }else{
                            if(player1 && !player1Win){
                                msg.text = "YOU LOSE"
                                mediaPlayer = MediaPlayer.create(context, R.raw.gameover)
                                Glide.with(context)
                                    .load(R.drawable.lose)
                                    .into(imageView);
                            }else if (player1 && player1Win){
                                msg.text = "YOU WIN"
                                mediaPlayer = MediaPlayer.create(context, R.raw.winsound)
                                Glide.with(context)
                                    .load(R.drawable.win)
                                    .into(imageView);

                            }else if(!player1 && player2Win){
                                msg.text = "YOU WIN"
                                mediaPlayer = MediaPlayer.create(context, R.raw.winsound)
                                Glide.with(context)
                                    .load(R.drawable.win)
                                    .into(imageView);


                            }else if(!player1 && !player2Win){
                                msg.text = "YOU LOSE"
                                mediaPlayer = MediaPlayer.create(context, R.raw.gameover)
                                Glide.with(context)
                                    .load(R.drawable.lose)
                                    .into(imageView);
                            }
                        }
                        mediaPlayer?.start()


                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



        }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
    override fun onBackPressed() {
        // Handle back press if needed
    }

    fun exit(){
        finishAffinity();
        System.exit(0);
    }

}