package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.database.*


class PlayModeOnl : AppCompatActivity() {
    var roomId = ""
    var player1 = true
    var myTurn = false
    lateinit var roomid : TextView
    lateinit var playerRole : TextView
    lateinit var meNbOfLifes : TextView
    lateinit var otherNbOfLifes : TextView
    lateinit var hangmanImg : ImageView
    lateinit var gridLayout : GridLayout
    var gameInitialised = false
    var meNbLifes = 7
    var otherNbLIfes = 7
    var otherPlayerName = ""
    var wordToGuess = ""
    var hiddenWord = ""
    var myRole= 1
    var gameFinish=false

    val db = Firebase.database.reference
    lateinit var hiddenWordTextView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_mode_onl)


        // inoit view
        roomid = findViewById(R.id.room)
        playerRole = findViewById(R.id.playerR)
        meNbOfLifes = findViewById(R.id.melifes)
        otherNbOfLifes = findViewById(R.id.otherLifes)
        hangmanImg = findViewById(R.id.meHangman)
        gridLayout = findViewById(R.id.grid)
        hiddenWordTextView = findViewById(R.id.hiddenW)


        roomId= intent.getStringExtra("roomId").toString()
        if(intent.getStringExtra("player1Or2").equals("player2")){
            player1=false
        }else{
            myTurn=true
        }


           init_game()


        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        for (letter in alphabet) {
            val button = Button(this)
            button.text = letter.toString()
            button.gravity = Gravity.CENTER
            button.setOnClickListener {
                if(myTurn && !gameFinish){
                    val clickedWord = button.text.toString()[0]
                    // play
                    play(clickedWord)
                    letTheFlag()
                }
            }
            // Add button to GridLayout
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            button.layoutParams = params
            gridLayout.addView(button)
        }

        checkTheRole()
        launchTheEndGame()


    }

    fun checkTheRole(){
        db.child(roomId).child("role").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange( dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! role updated ${wordToGuess} ",  myTurn.toString())
                    var role = dataSnapshot.getValue().toString().toInt()
                    if((role==1 && player1) || (role== 2 && !player1)){
                        playerRole.text="it's your turn"
                        myTurn = true
                    }else{
                        myTurn= false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
        var player="player1"
        if(player1) player="player2"
        db.child(roomId).child(player).child("nbLife").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange( dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    otherNbLIfes = dataSnapshot.getValue().toString().toInt()
                    otherNbOfLifes.text=otherPlayerName+" still have ${otherNbLIfes}"

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }


    fun updateHidenWoed(response:String,hidenW:String) : String {
        var goodGeuss=false
        var newHidenWord=""
        for(i in hidenW.indices){
            if(response[i]!='_'){
                newHidenWord+=response[i]
                if(hidenW[i]=='_'){
                    goodGeuss=true
                }
            }else{
                newHidenWord+=hidenW[i]
            }
        }
        if(!goodGeuss){
            meNbLifes --
            printHang(meNbLifes,hangmanImg)
            meNbOfLifes.text = "you still have "+meNbLifes
            // submit the new nb of lifes
            if(player1){
                db.child(roomId).child("player1").child("nbLife").setValue(meNbLifes)
            }else{
                db.child(roomId).child("player2").child("nbLife").setValue(meNbLifes)
            }
            if(meNbLifes==0){
                setThefinishState(1)
            }
        }else{
            //checkWinState
            if(wordToGuess.equals(newHidenWord)){
                setThefinishState(0)
            }

        }



        return newHidenWord

    }

    fun setThefinishState(lose :Int){
        gameFinish=true
        db.child(roomId).child("nbOfDonePlayers").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val nb = it.value.toString().toInt() + 1
            db.child(roomId).child("nbOfDonePlayers").setValue(nb)
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        if(lose==0){
            if(player1){
                db.child(roomId).child("player1").child("win").setValue(1)
            }else{
                db.child(roomId).child("player2").child("win").setValue(1)
            }
        }else{
            if(player1){
                db.child(roomId).child("player1").child("win").setValue(0)
            }else{
                db.child(roomId).child("player2").child("win").setValue(0)
            }
        }
    }



    fun play(selectedLetter: Char){

            var response =""
            for(i in wordToGuess.indices){
                if (selectedLetter==wordToGuess[i].uppercaseChar()){
                    response+=wordToGuess[i]
                }
                else{
                    response+="_"
                }

            }


            hiddenWord=updateHidenWoed(response,hiddenWord)
            hiddenWordTextView.text=hiddenWord

    }

    fun letTheFlag(){
        playerRole.text = otherPlayerName+" are guessing ..."
        var player="player2"
        if(player1)
            player="player1"

        db.child(roomId).child("nbOfDonePlayers").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange( dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var nb = dataSnapshot.getValue().toString().toInt()
                    if(nb==0){
                        if(player1)
                            db.child(roomId).child("role").setValue(2)
                        else
                            db.child(roomId).child("role").setValue(1)
                    }
                }
                db.child(roomId).child("nbOfDonePlayers").removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {

            }


        }
        )


    }

    fun printHang(nb:Int,hangImage:ImageView){
        if(nb==6)
            hangImage.setImageResource(R.drawable.stage0)
        else if (nb==5)
            hangImage.setImageResource(R.drawable.stage1)
        else if (nb==4)
            hangImage.setImageResource(R.drawable.stage2)
        else if (nb==3)
            hangImage.setImageResource(R.drawable.stage3)
        else if (nb==2)
            hangImage.setImageResource(R.drawable.stage4)
        else if (nb==1)
            hangImage.setImageResource(R.drawable.stage5)
        else if (nb==0)
            hangImage.setImageResource(R.drawable.stage6)

    }

    fun init_game(){
        roomid.text="room ID : ${roomId} // "
        // i'am the player  2 who enter the room
        if(!player1){
            myRole*=-1
            db.child(roomId).child("player1").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange( dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! player name ",   dataSnapshot.getValue().toString())
                        wordToGuess = dataSnapshot.child("word").value.toString()
                        otherPlayerName = dataSnapshot.child("name").value.toString()
                        otherNbLIfes= dataSnapshot.child("nbLife").value.toString().toInt()
                        playerRole.text=otherPlayerName+" are geussing ...."
                        otherNbOfLifes.text=otherPlayerName+" still have ${otherNbLIfes}"
                        meNbOfLifes.text= " you still have ${meNbLifes}"
                        hiddenWord = hideWord(wordToGuess)
                        hiddenWordTextView.text = hiddenWord
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })
        }
        // i'am the player 1 who create the room
        else{
                db.child(roomId).child("player2").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange( dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                          Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! player name ",   dataSnapshot.getValue().toString())
                            wordToGuess = dataSnapshot.child("word").value.toString()
                            otherPlayerName = dataSnapshot.child("name").value.toString()
                            otherNbLIfes= dataSnapshot.child("nbLife").value.toString().toInt()
                            playerRole.text="it's your turn"
                            otherNbOfLifes.text=otherPlayerName+" still have ${otherNbLIfes}"
                            meNbOfLifes.text= " you still have ${meNbLifes}"
                            hiddenWord = hideWord(wordToGuess)
                            hiddenWordTextView.text = hiddenWord

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

        })




    }
        gameInitialised = true
}

    fun hideWord(selectedWord:String):String{
        var wordWithHidenL = selectedWord
        for (i in 0 until selectedWord.length / 2) {
            wordWithHidenL = wordWithHidenL.replace(
                selectedWord.get(((Math.random() * selectedWord.length - 1).toInt())),
                '_'
            )
        }
        return wordWithHidenL
    }
    fun launchTheEndGame (){
        db.child(roomId).child("nbOfDonePlayers").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange( dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var nb = dataSnapshot.getValue().toString().toInt()
                    if(nb==2){
                     // endGame
                        endGame()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    fun endGame(){
        val intent = Intent(this, EndGameOnlineM::class.java)
        intent.putExtra("roomId",roomId)
        if(player1)
            intent.putExtra("player1","yes")
        else
            intent.putExtra("player1","no")

        startActivity(intent)
        finish()
    }


    override fun onBackPressed() {
        // Handle back press if needed
    }

}