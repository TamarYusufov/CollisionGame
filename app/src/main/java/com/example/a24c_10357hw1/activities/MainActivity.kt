package com.example.a24c_10357hw1.activities

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a24c_10357hw1.utilities.Constants
import com.example.a24c_10357hw1.utilities.Constants.Companion.COLS
import com.example.a24c_10357hw1.utilities.Constants.Companion.ROWS
import com.example.a24c_10357hw1.model.GameManager
import com.example.a24c_10357hw1.R
import com.example.a24c_10357hw1.interfaces.CallBack_MoveCallBack
import com.example.a24c_10357hw1.model.Game
import com.example.a24c_10357hw1.utilities.MoveDetector
import com.example.a24c_10357hw1.utilities.SharedPreferencesManager
import com.example.a24c_10357hw1.utilities.SingleSoundPlayer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_hearts: Array<ShapeableImageView>
    private lateinit var main_IMG_dogs: Array<ShapeableImageView>
    private lateinit var main_IMG_chocolate0: Array<ShapeableImageView>
    private lateinit var main_IMG_chocolate1: Array<ShapeableImageView>
    private lateinit var main_IMG_chocolate2: Array<ShapeableImageView>
    private lateinit var main_IMG_chocolate3: Array<ShapeableImageView>
    private lateinit var main_IMG_chocolate4: Array<ShapeableImageView>
    private lateinit var main_IMG_chocolate5: Array<ShapeableImageView>
    private lateinit var main_IMG_coin0: Array<ShapeableImageView>
    private lateinit var main_IMG_coin1: Array<ShapeableImageView>
    private lateinit var main_IMG_coin2: Array<ShapeableImageView>
    private lateinit var main_IMG_coin3: Array<ShapeableImageView>
    private lateinit var main_IMG_coin4: Array<ShapeableImageView>
    private lateinit var main_IMG_coin5: Array<ShapeableImageView>
    private lateinit var main_BTN_left: MaterialButton
    private lateinit var main_BTN_right: MaterialButton
    private lateinit var chocolateMatrix: Array<Array<ShapeableImageView>>
    private lateinit var coinMatrix: Array<Array<ShapeableImageView>>
    private lateinit var gameManager: GameManager
    private lateinit var main_LBL_score: MaterialTextView



    private var startTime: Long = 0
    private  var timerOn = false
    private lateinit var timerJob: Job


    private lateinit var singleSoundPlayer: SingleSoundPlayer
    private lateinit var mode: String

    private lateinit var moveDetector: MoveDetector

  //  private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var game: Game




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
        singleSoundPlayer = SingleSoundPlayer(this)

        mode = intent.getStringExtra(Constants.MODE).toString()

        setGameMode(mode)

        sharedPref = SharedPreferencesManager.init(this)

    }

    private fun setGameMode(mode: String) {
        when(mode) {
            "SENSOR_MODE"-> {
                main_BTN_left.visibility = View.INVISIBLE
                main_BTN_right.visibility = View.INVISIBLE
                initMoveDetector()
               // startTimer(Constants.DELAY)

            }
            "FAST_MODE" -> {
                startTimer(Constants.DELAY_FAST)

            }
            "SLOW_MODE" -> {
                startTimer(Constants.DELAY_SLOW)
            }
        }

    }


    private fun initMoveDetector() {

        moveDetector = MoveDetector(this ,
            object :CallBack_MoveCallBack  {
                override fun changeDirection(direction: Int) {
                    when(direction) {
                        0-> {
                            playerMovesInUI(1)
                        }
                        1-> {
                            playerMovesInUI(0)
                        }
                    }

                }

                override fun changeSpeed(speed: Float) {
                    when(speed){
                        Constants.DELAY_FAST.toFloat() -> { startTimer(Constants.DELAY_FAST) }
                        Constants.DELAY_SLOW.toFloat() -> { startTimer(Constants.DELAY_SLOW) }
                    }
                }


            })

        moveDetector.start()
    }

    private fun initViews() {
        main_IMG_dogs[0].visibility = View.INVISIBLE
        main_IMG_dogs[1].visibility = View.INVISIBLE
        main_IMG_dogs[2].visibility = View.VISIBLE
        main_IMG_dogs[3].visibility = View.INVISIBLE
        main_IMG_dogs[4].visibility = View.INVISIBLE

        for(i in 0 until ROWS)
            for(j in 0 until COLS)
                chocolateMatrix[i][j].visibility = View.INVISIBLE


        for(i in 0 until ROWS)
            for(j in 0 until COLS)
                coinMatrix[i][j].visibility = View.VISIBLE

        main_LBL_score.text = gameManager.score.toString()

        main_BTN_left.setOnClickListener {view: View? -> playerMovesInUI(0) }
        main_BTN_right.setOnClickListener{view: View? -> playerMovesInUI(1)}


    }

    private fun stopTimer() {
        timerOn = false
        timerJob.cancel()
    }

    private fun startTimer(speed: Long) {
        if (!timerOn) {
            startTime = System.currentTimeMillis()
            timerOn = true
            timerJob = lifecycleScope.launch {
                while (timerOn) {
                    chocolateMoveInUI()
                    coinMoveInUI()
                    checkGameStatus()
                    delay(speed)
                }
            }
        }
    }



    private fun checkGameStatus() {
        if(gameManager.hasCrushed()) {
            main_IMG_hearts[3- gameManager.numCrushes].visibility = View.INVISIBLE

            toastAndVibrate("Try again ")
            singleSoundPlayer.playSound(R.raw.eating_sound)
        }

        if(gameManager.gotCoin()) {
            main_LBL_score.text = gameManager.score.toString()
            singleSoundPlayer.playSound(R.raw.ding_sound)
        }


        if(gameManager.gameLost) {
            toastAndVibrate("YOU LOST" )
            singleSoundPlayer.playSound(R.raw.lose_sound)
            moveDetector.stop()
            stopTimer()

        }
    }


    private fun toastAndVibrate(text: String) {
        toast(text)
        vibrate()
    }

    private fun vibrate() {
        val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if(vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        }

    }


    private fun toast(text: String) {
        Toast
            .makeText(this, text, Toast.LENGTH_LONG).show()
    }


    private fun findViews() {

        findHeartsViews()
        findDogsViews()
        findChocolateViews()
        findCoinViews()

        main_LBL_score = findViewById(R.id.main_LBL_score)

        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)

    }

    private fun findHeartsViews() {
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
    }

    private fun findDogsViews() {
        main_IMG_dogs = arrayOf(
            findViewById(R.id.main_IMG_dog0),
            findViewById(R.id.main_IMG_dog1),
            findViewById(R.id.main_IMG_dog2),
            findViewById(R.id.main_IMG_dog3),
            findViewById(R.id.main_IMG_dog4)
        )
    }

    private fun findCoinViews() {
        main_IMG_coin0 = arrayOf(
            findViewById(R.id.main_IMG_coin00),
            findViewById(R.id.main_IMG_coin01),
            findViewById(R.id.main_IMG_coin02),
            findViewById(R.id.main_IMG_coin03),
            findViewById(R.id.main_IMG_coin04)
        )
        main_IMG_coin1 = arrayOf(
            findViewById(R.id.main_IMG_coin10),
            findViewById(R.id.main_IMG_coin11),
            findViewById(R.id.main_IMG_coin12),
            findViewById(R.id.main_IMG_coin13),
            findViewById(R.id.main_IMG_coin14)
        )
        main_IMG_coin2 = arrayOf(
            findViewById(R.id.main_IMG_coin20),
            findViewById(R.id.main_IMG_coin21),
            findViewById(R.id.main_IMG_coin22),
            findViewById(R.id.main_IMG_coin23),
            findViewById(R.id.main_IMG_coin24)
        )
        main_IMG_coin3 = arrayOf(
            findViewById(R.id.main_IMG_coin30),
            findViewById(R.id.main_IMG_coin31),
            findViewById(R.id.main_IMG_coin32),
            findViewById(R.id.main_IMG_coin33),
            findViewById(R.id.main_IMG_coin34)
        )
        main_IMG_coin4 = arrayOf(
            findViewById(R.id.main_IMG_coin40),
            findViewById(R.id.main_IMG_coin41),
            findViewById(R.id.main_IMG_coin42),
            findViewById(R.id.main_IMG_coin43),
            findViewById(R.id.main_IMG_coin44)
        )

        main_IMG_coin5 = arrayOf(
            findViewById(R.id.main_IMG_coin50),
            findViewById(R.id.main_IMG_coin51),
            findViewById(R.id.main_IMG_coin52),
            findViewById(R.id.main_IMG_coin53),
            findViewById(R.id.main_IMG_coin54)
        )

        coinMatrix = arrayOf(
            main_IMG_coin0,
            main_IMG_coin1,
            main_IMG_coin2,
            main_IMG_coin3,
            main_IMG_coin4,
            main_IMG_coin5
        )
    }

    private fun findChocolateViews() {
        main_IMG_chocolate0 = arrayOf(
            findViewById(R.id.main_IMG_chocolate00),
            findViewById(R.id.main_IMG_chocolate01),
            findViewById(R.id.main_IMG_chocolate02),
            findViewById(R.id.main_IMG_chocolate03),
            findViewById(R.id.main_IMG_chocolate04)
        )
        main_IMG_chocolate1 = arrayOf(
            findViewById(R.id.main_IMG_chocolate10),
            findViewById(R.id.main_IMG_chocolate11),
            findViewById(R.id.main_IMG_chocolate12),
            findViewById(R.id.main_IMG_chocolate13),
            findViewById(R.id.main_IMG_chocolate14)
        )
        main_IMG_chocolate2 = arrayOf(
            findViewById(R.id.main_IMG_chocolate20),
            findViewById(R.id.main_IMG_chocolate21),
            findViewById(R.id.main_IMG_chocolate22),
            findViewById(R.id.main_IMG_chocolate23),
            findViewById(R.id.main_IMG_chocolate24)
        )
        main_IMG_chocolate3 = arrayOf(
            findViewById(R.id.main_IMG_chocolate30),
            findViewById(R.id.main_IMG_chocolate31),
            findViewById(R.id.main_IMG_chocolate32),
            findViewById(R.id.main_IMG_chocolate33),
            findViewById(R.id.main_IMG_chocolate34)
        )
        main_IMG_chocolate4 = arrayOf(
            findViewById(R.id.main_IMG_chocolate40),
            findViewById(R.id.main_IMG_chocolate41),
            findViewById(R.id.main_IMG_chocolate42),
            findViewById(R.id.main_IMG_chocolate43),
            findViewById(R.id.main_IMG_chocolate44)
        )

        main_IMG_chocolate5 = arrayOf(
            findViewById(R.id.main_IMG_chocolate50),
            findViewById(R.id.main_IMG_chocolate51),
            findViewById(R.id.main_IMG_chocolate52),
            findViewById(R.id.main_IMG_chocolate53),
            findViewById(R.id.main_IMG_chocolate54)
        )

        chocolateMatrix = arrayOf(
            main_IMG_chocolate0,
            main_IMG_chocolate1,
            main_IMG_chocolate2,
            main_IMG_chocolate3,
            main_IMG_chocolate4,
            main_IMG_chocolate5
        )
    }


    private fun playerMovesInUI (sideClicked: Int ) { //0-left 1-right
       val move =  gameManager.playerMove(sideClicked)

        for (i in move.indices)
            if(move[i] == 1)
                main_IMG_dogs[i].visibility = View.VISIBLE
            else if(move[i] == 0) {
                main_IMG_dogs[i].visibility = View.INVISIBLE
            }
    }


    private  fun chocolateMoveInUI () {
        val chocolateArray = gameManager.moveObstacles()

        for(i in 0 until ROWS ) {
            for (j in 0 until COLS) {
                if (chocolateArray[i][j] == 1)
                    chocolateMatrix[ROWS - i - 1][j].visibility = View.VISIBLE
                else
                    chocolateMatrix[ROWS - i - 1][j].visibility = View.INVISIBLE
            }

        }

    }


    private  fun coinMoveInUI () {
        val coinArray = gameManager.moveCoins()

        for(i in 0 until ROWS ) {
            for (j in 0 until COLS) {
                if (coinArray[i][j] == 1)
                    coinMatrix[ROWS - i - 1][j].visibility = View.VISIBLE
                else
                    coinMatrix[ROWS - i - 1][j].visibility = View.INVISIBLE
            }

        }

    }
}