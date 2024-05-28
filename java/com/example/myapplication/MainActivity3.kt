package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class MainActivity3 : AppCompatActivity() {

    lateinit var hangImage : ImageView
    var gameTimer: CountDownTimer? = null
    private var mediaPlayer: MediaPlayer? = null
    lateinit var still : TextView
    lateinit var timer : TextView
    var currentTime : Long=0
    lateinit var selectedWord :String
    lateinit var userName:String
    lateinit var scoreManager :ScoreManager
    var selectedDifficulty=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()

        // init words list and tree
        val wordStorage =WordsFile(this)
        val wordsList = wordStorage.wordsList
        val binaryTree = BinaryWordsTree(wordsList)
        scoreManager= ScoreManager(this)

        still = findViewById(R.id.nbOfTime)
        val wordView : TextView = findViewById(R.id.hiddenWord)
        hangImage = findViewById(R.id.hang)
        timer = findViewById(R.id.timer)
        val hint:ImageView = findViewById(R.id.imgHint)
        hint.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                hint()
            }
        })



        // game varibale
        var level = 0
        var manyTime = 7
        var indexOfW = 0;
        var timeRemaing = 30000
        //load button dynamiclly
        val gridLayout: GridLayout = findViewById(R.id.gridLayout)





        //select level and word index

         selectedDifficulty = intent.getStringExtra("selectedDifficulty").toString()
        userName = intent.getStringExtra("name").toString()
        if (selectedDifficulty != null) {
            Log.d("NextActivity", "Received Difficulty: $selectedDifficulty")
            if(selectedDifficulty.equals("Hard")){
                level=3
                indexOfW = ((Math.random() * wordStorage.wordsRate.size).toInt());
                while(wordStorage.wordsRate.get(indexOfW)<4){
                    indexOfW = ((Math.random() * wordStorage.wordsRate.size).toInt())
                }
            }

            else if(selectedDifficulty.equals("Medium")){
                timeRemaing = 45000
                level=2
                indexOfW = ((Math.random() * wordStorage.wordsRate.size).toInt());
                while(wordStorage.wordsRate.get(indexOfW)>3.5 || wordStorage.wordsRate.get(indexOfW)<2.5){
                    indexOfW = ((Math.random() * wordStorage.wordsRate.size).toInt())
                }
            }
            else if(selectedDifficulty.equals("Easy")){
                timeRemaing = 60000
                level=1
                    indexOfW = ((Math.random() * wordStorage.wordsRate.size).toInt());
                    while(wordStorage.wordsRate.get(indexOfW)>2){
                        indexOfW = ((Math.random() * wordStorage.wordsRate.size).toInt())
                    }
            } else {
            Log.d("NextActivity", "No difficulty received")
        }
            selectedWord = wordStorage.wordsList[indexOfW];
            var path= binaryTree.search(selectedWord);


            // hide letters

            var wordWithHidenL = selectedWord
            for (i in 0 until selectedWord.length / 2) {
                wordWithHidenL = wordWithHidenL.replace(
                    selectedWord.get(((Math.random() * selectedWord.length - 1).toInt())),
                    '_'
                )
            }

            Log.d ("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",selectedWord+":"+path+":"+wordStorage.wordsRate.get(indexOfW)+", hidden word :"+wordWithHidenL);

            wordView.setText(wordWithHidenL.toString())

            val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            for (letter in alphabet) {
                val button = Button(this)
                button.text = letter.toString()
                button.gravity = Gravity.CENTER
                button.setOnClickListener {
                    val clickedWord = button.text.toString()
                    var sb = StringBuilder(wordWithHidenL)
                    val gameState =play(clickedWord[0],binaryTree,wordWithHidenL,manyTime,path,level,selectedWord)
                    wordWithHidenL = gameState.hidenWord.toString()
                    manyTime=gameState.nbTime
                    if(gameState.endGame){
                        endGame(gameState.nbTime)
                    }

                    Log.d ("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",selectedWord+" hidden word :"+wordWithHidenL+" many time :"+gameState.nbTime);
                    Log.d("MainActivity", "Word clicked: $clickedWord")
                    wordView.setText(wordWithHidenL.toString())
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

    }
       gameTimer= object : CountDownTimer(timeRemaing.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timer.setText("seconds remaining: " + millisUntilFinished / 1000)
                currentTime=millisUntilFinished
            }

            override fun onFinish() {
               timer.setText("time finish!")
                endGame(0)
            }
        }.start()


    }

    fun play (
        c: Char,
        bt:BinaryWordsTree, wordWithHidenL: String, manyTime: Int,
        path: String , level : Int, selectedWord : String): GameData {
        var gameState = GameData(false ,manyTime,java.lang.StringBuilder(wordWithHidenL))
        // check word
        var rightGuess = false
        val lettersFound: String = bt.pathToWordWithTheGuessedLetterOnly(path, c)
        for (i in 0 until lettersFound.length) {
            if (lettersFound[i] != '_' && lettersFound[i] != gameState.hidenWord[i]) {
                rightGuess = true
                gameState.hidenWord.setCharAt(i, lettersFound[i])
            }


        }

        if (!rightGuess) {
            if (level == 1) gameState.nbTime-=1 else if (level == 2) {
                if (manyTime == 1) gameState.nbTime-- else gameState.nbTime -= 2
            } else if (level == 3) {
                if (manyTime == 1) gameState.nbTime-- else gameState.nbTime -= 3
            }
            printHang(gameState.nbTime)
            still.text=gameState.nbTime.toString()
        }
        if(gameState.hidenWord.toString() == selectedWord || gameState.nbTime==0)
            gameState.endGame = true
        else
            sound(rightGuess)

        return gameState
    }

    fun printHang(nb:Int){
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

    fun sound(correct: Boolean){
        mediaPlayer = if(correct)
            MediaPlayer.create(this, R.raw.correctanswer)
        else
            MediaPlayer.create(this,R.raw.wronganswer)

        mediaPlayer?.setOnCompletionListener {

        }
        mediaPlayer?.start()
    }

    fun endGame(nb : Int){
        val intent = Intent(this, EndGame::class.java)
        if(nb==0)
            intent.putExtra("loseOrWin",selectedWord)
        else {
            scoreManager.addOrUpdateTopScore(
                userName,
                selectedDifficulty,
                nb,
                selectedWord,
                (currentTime / 1000).toString()
            )
            intent.putExtra("loseOrWin", "*$selectedWord")
        }
        gameTimer?.cancel()
        startActivity(intent)
    }
    fun hint(){
        val intent = Intent(this, Hint::class.java)
        intent.putExtra("word",selectedWord)
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer when the activity is destroyed
        mediaPlayer?.release()
    }
    override fun onBackPressed() {
        // Handle back press if needed
    }


}



data class GameData(
    var endGame: Boolean,
    var nbTime: Int ,
    var hidenWord : StringBuilder
)
