package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.*

var db = Firebase.database.reference
var roomId =""


class Player1WaitingForPlayer2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player1_waiting_for_player2)

        roomId = intent.getStringExtra("roomId").toString()


        waitForPlayer2(this)
    }

    fun waitForPlayer2(context: Context){

       db.child(roomId).child("player2").addValueEventListener(object : ValueEventListener {
            override fun onDataChange( dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists() ) {
                    val intent = Intent(context,PlayModeOnl::class.java)
                    intent.putExtra("roomId", roomId)
                    intent.putExtra("player1Or2","player1")

                    startActivity(intent)

                    Firebase.database.reference.child(roomId).child("player2").removeEventListener(this)

                }


            }

            override fun onCancelled( databaseError: DatabaseError) {}
        })


    }
    override fun onBackPressed() {
        // Handle back press if needed
    }
}