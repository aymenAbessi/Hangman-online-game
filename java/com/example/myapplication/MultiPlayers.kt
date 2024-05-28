package com.example.myapplication
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MultiPlayers : AppCompatActivity() {

    private var player1Name: String = ""
    private var player2Name: String = ""
    private var player1HiddenWord: String = ""
    private var player2HiddenWord: String = ""
    private var player1SelectedWord: String = ""
    private var player2SelectedWord: String = ""
    lateinit var  player1View : TextView
    lateinit var player2View : TextView
    lateinit var playerRoleView : TextView
    lateinit var  playerLifes1View : TextView
    var player1Lifes=7
    var player2Lifes=7
    lateinit var playerLifes2View : TextView
    lateinit var hang1Img : ImageView
    lateinit var hang2Img : ImageView
    lateinit var player1HidenWordView : TextView
    lateinit var player2HidenWordView : TextView
    var playerRole = 1
    var gameEnd= false
    var player1Win = false
    var player2Win = false
    var player1lose = false
    var player2lose = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_players)

        val gridLayout: GridLayout = findViewById(R.id.gridLayout2)
        player1View = findViewById(R.id.player1)
        player2View  = findViewById(R.id.player2)
        hang1Img  =findViewById(R.id.hang1)
         hang2Img  =findViewById(R.id.hang2)
         player1HidenWordView  = findViewById(R.id.hiddenWord1)
         player2HidenWordView  = findViewById(R.id.hiddenWord2)
        playerLifes1View= findViewById(R.id.lifesPlayer1)
        playerLifes2View= findViewById(R.id.lifesPlayer2)
        playerRoleView= findViewById(R.id.playerRole)

        // alphabet
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        for (letter in alphabet) {
            val button = Button(this)
            button.text = letter.toString()
            button.gravity = Gravity.CENTER
            button.setOnClickListener {
                val clickedWord = button.text.toString()[0]
               if(!gameEnd)
                   play(clickedWord)
               // Log.d("MainActivity", "$player2lose ${playerRole} Word clicked: $clickedWord")

            }

            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            button.layoutParams = params
            gridLayout.addView(button)
        }


        showInputDialog(this, 2)
        showInputDialog(this, 1)


    }


    private fun showInputDialog(context: Context, playerNumber: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Enter Player $playerNumber Details")

        // Create a LinearLayout to hold the EditText fields
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        // Add EditText for Player Name
        val playerNameEditText = EditText(context)
        playerNameEditText.hint = "Player Name"
        layout.addView(playerNameEditText)

        // Add EditText for Hidden Word
        val hiddenWordEditText = EditText(context)
        hiddenWordEditText.hint = "Hidden Word"
        layout.addView(hiddenWordEditText)

        builder.setView(layout)

        // Set up the buttons
        builder.setPositiveButton("Confirm") { _, _ ->
            // Retrieve values from the EditText fields
            val playerName = playerNameEditText.text.toString()
            val hiddenWord = hiddenWordEditText.text.toString()

            // Log the values
            Log.d("PlayerDetails", "Player $playerNumber Name: $playerName")
            Log.d("PlayerDetails", "Player $playerNumber Hidden Word: $hiddenWord")

            // Assign values to class-level variables based on the player number
            when (playerNumber) {
                1 -> {
                    player1Name = playerName
                    player1SelectedWord = hiddenWord
                }
                2 -> {
                    player2Name = playerName
                    player2SelectedWord = hiddenWord
                    Log.i("PlayerDetails", "Player 1: Name=$player1Name, HiddenWord=$player1HiddenWord, Player 2: Name=$player2Name, HiddenWord=$player2HiddenWord")

                    // Set the TextView values after both players have entered their details
                    player1View.text = player1Name
                    player2View.text = player2Name
                    // hide words

                    player1HiddenWord = hideWord(player1SelectedWord)
                    player2HiddenWord = hideWord(player2SelectedWord)
                    player1HidenWordView.text=player2HiddenWord
                    player2HidenWordView.text=player1HiddenWord
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        // Create and show the alert dialog and save the user data
        val alertDialog = builder.create()
        alertDialog.show()


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
            if(playerRole==1){
                player1Lifes--
                playerLifes1View.text=player1Lifes.toString()
                printHang(player1Lifes,hang1Img)
                if(player1Lifes==0){
                    player1lose=true
                }
            }else{
                player2Lifes--
                playerLifes2View.text=player2Lifes.toString()
                printHang(player2Lifes,hang2Img)
                if(player2Lifes==0){
                    player2lose=true

                }
            }
        }else{
            //checkWinState
            if ( playerRole==1 && newHidenWord.equals(player2SelectedWord)){
                player1Win = true
                player1View.text=player1Name+" Win"
                playerRoleView.text=player2Name+" Role"
            }

            else if (playerRole==-1 && newHidenWord.equals(player1SelectedWord)){
                player2Win=true
                player2View.text=player2Name+" Win"
            }



        }


        return newHidenWord

    }



    fun play(selectedLetter: Char){
        if(playerRole==1 && (!player1Win && !player1lose) ){
            var response =""
            for(i in player2SelectedWord.indices){
                if (selectedLetter==player2SelectedWord[i].uppercaseChar()){
                    response+=player2SelectedWord[i]
                }
                else{
                    response+="_"
                }

            }


            player2HiddenWord=updateHidenWoed(response,player2HiddenWord)
           player1HidenWordView.text=player2HiddenWord
            if(!player2Win && !player2lose){
                playerRoleView.text=player2Name+" Role"
                playerRole*=-1
            }


        }else if(playerRole==-1 && (!player2Win && !player2lose)){
            var response =""
            for(i in player1SelectedWord.indices){
                if (selectedLetter==player1SelectedWord[i].uppercaseChar())
                    response+=player1SelectedWord[i]
                else
                    response+="_"
            }


            player1HiddenWord=updateHidenWoed(response,player1HiddenWord)
            player2HidenWordView.text=player1HiddenWord
            if(!player1Win && !player1lose){
                playerRoleView.text=player1Name+" Role"
                playerRole*=-1
            }

        }

        if ((player1Win || player1lose) && (player2Win || player2lose)) {
            gameEnd = true
            endGame()
        }
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

    fun endGame(){
        val intent = Intent(this, EndGame2Players::class.java)
        if(player1Win && player2Win) {
            intent.putExtra("loseOrWin", "both win")
            intent.putExtra("winner", "both")
            intent.putExtra("loser","none")
        }
        else if(player1Win){
            intent.putExtra("loseOrWin", "player 1")
            intent.putExtra("winner",player1Name)
            intent.putExtra("loser",player2Name)
        }
        else if(player2Win){
            intent.putExtra("loseOrWin", "player 2")
            intent.putExtra("winner",player2Name)
            intent.putExtra("loser",player1Name)
        }else{
            intent.putExtra("loseOrWin", "both lose")
            intent.putExtra("winner","none")
            intent.putExtra("loser","both")
        }

        startActivity(intent)
    }

    override fun onBackPressed() {

    }

}
