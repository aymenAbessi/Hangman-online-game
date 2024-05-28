package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class PlayOnline : AppCompatActivity() {


   lateinit var  roomId : EditText
   lateinit var playerName : EditText
   lateinit var playerWord : EditText
    var player1 = false

    var oldId=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_online)


         val create : Button = findViewById(R.id.create)
        create.setOnClickListener(createListenner)
        val valid : Button = findViewById(R.id.valid)
        valid.setOnClickListener(validListenner)

        roomId = findViewById(R.id.roomId)
        playerName = findViewById(R.id.playerName)
        playerWord = findViewById(R.id.playerWord)

        if( !intent.getStringExtra("roomId").toString().equals("no_id")){
            roomId.visibility=View.INVISIBLE
            create.visibility=View.INVISIBLE
            oldId=true
            if (intent.getStringExtra("player1OLdId").toString() == "yes"){
                player1=true
            }
            roomId.setText(intent.getStringExtra("roomId").toString())

        }
        if(intent.getStringExtra("action").toString().equals("enter"))
            create.visibility=View.INVISIBLE

    }

    val createListenner : View.OnClickListener=View.OnClickListener {create() }
    val validListenner : View.OnClickListener= View.OnClickListener { verify(roomId.text.toString()) }

    fun create(){
        val database = Firebase.database
        val myRef = database.getReference(roomId.text.toString())
        val init = Role(1)
        myRef.setValue(init)
        val init2=NbOfDonePlayer(0)
        myRef.setValue(init2)
        player1= true

    }


    fun valid(id : String){

        val player = Player(playerName.text.toString(),playerWord.text.toString(),7)
            // add player data
        if(oldId){
            val id= intent.getStringExtra("roomId").toString()
            if(player1)
                Firebase.database.reference.child(id).child("player1").setValue(player)
            else
                Firebase.database.reference.child(id).child("player2").setValue(player)
        }else{
            if(player1)
                Firebase.database.reference.child(id).child("player1").setValue(player)
            else
                Firebase.database.reference.child(id).child("player2").setValue(player)
        }

        lunchPlayScreen()
    }
    fun verify(id:String){
        Firebase.database.reference.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange( dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                       if(dataSnapshot.key.toString().equals(id) ){
                           valid(dataSnapshot.key.toString())

                       }
                } else {

                }
            }

            override fun onCancelled( databaseError: DatabaseError) {}
        })

    }

    fun lunchPlayScreen (){
        val intent = Intent (this,PlayModeOnl::class.java)
        val intent2 = Intent (this,Player1WaitingForPlayer2::class.java)
        intent.putExtra("roomId", roomId.text.toString())
        intent2.putExtra("roomId", roomId.text.toString())

        if(player1) {
            if(oldId){
                intent2.putExtra("ignore","yes")
            }else{
                intent2.putExtra("ignore","no")
            }
            startActivity(intent2)
        }
        else {
            intent.putExtra("player1Or2", "player2")
            startActivity(intent)
        }
    }
}

data class Role(var role:  Int)
data class NbOfDonePlayer(var nbOfDonePlayers:  Int)
