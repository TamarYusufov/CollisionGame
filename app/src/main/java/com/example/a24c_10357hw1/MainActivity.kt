package com.example.a24c_10357hw1

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a24c_10357hw1.Constants.Companion.COLS
import com.example.a24c_10357hw1.Constants.Companion.ROWS
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
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

    private lateinit var main_BTN_left: MaterialButton

    private lateinit var main_BTN_right: MaterialButton

    private lateinit var chocolateMatrix: Array<Array<ShapeableImageView>>

    private lateinit var gameManager: GameManager

    private var startTime: Long = 0
    private  var timerOn = false
    private lateinit var timerJob: Job






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
        startTimer()


    }

    private fun initViews() {
        main_IMG_dogs[0].visibility = View.INVISIBLE
        main_IMG_dogs[1].visibility = View.VISIBLE
        main_IMG_dogs[2].visibility = View.INVISIBLE

        for(i in 0 until ROWS)
            for(j in 0 until COLS)
                chocolateMatrix[i][j].visibility = View.INVISIBLE

        main_BTN_left.setOnClickListener {view: View? -> playerMovesInUI(0) }
        main_BTN_right.setOnClickListener{view: View? -> playerMovesInUI(1)}


    }

    private fun stopTimer() {
        timerOn = false
        timerJob.cancel()
    }

    private fun startTimer() {
        if (!timerOn) {
            startTime = System.currentTimeMillis()
            timerOn = true
            timerJob = lifecycleScope.launch {
                while (timerOn) {
                    chocolateMoveInUI()
                    checkGameStatus()
                    delay(Constants.DELAY)
                }
            }
        }
    }

    private fun checkGameStatus() {
        if(gameManager.hasCrushed()) {
            main_IMG_hearts[3- gameManager.numCrushes].visibility = View.INVISIBLE

            toastAndVibrate("Try again ")
        }


        if(gameManager.gameLost) {
            toastAndVibrate("YOU LOST" )
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
            .makeText(
                this,
                text,
                Toast.LENGTH_LONG
            ).show()
    }


    private fun findViews() {
        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )
        main_IMG_dogs = arrayOf(
            findViewById(R.id.main_IMG_dog0),
            findViewById(R.id.main_IMG_dog1),
            findViewById(R.id.main_IMG_dog2)
        )
        main_IMG_chocolate0 = arrayOf(
            findViewById(R.id.main_IMG_chocolate00),
            findViewById(R.id.main_IMG_chocolate01),
            findViewById(R.id.main_IMG_chocolate02)
        )
        main_IMG_chocolate1 = arrayOf(
            findViewById(R.id.main_IMG_chocolate10),
            findViewById(R.id.main_IMG_chocolate11),
            findViewById(R.id.main_IMG_chocolate12)
        )
        main_IMG_chocolate2 = arrayOf(
            findViewById(R.id.main_IMG_chocolate20),
            findViewById(R.id.main_IMG_chocolate21),
            findViewById(R.id.main_IMG_chocolate22)
        )
        main_IMG_chocolate3 = arrayOf(
            findViewById(R.id.main_IMG_chocolate30),
            findViewById(R.id.main_IMG_chocolate31),
            findViewById(R.id.main_IMG_chocolate32)
        )
        main_IMG_chocolate4 = arrayOf(
            findViewById(R.id.main_IMG_chocolate40),
            findViewById(R.id.main_IMG_chocolate41),
            findViewById(R.id.main_IMG_chocolate42)
        )

        main_IMG_chocolate5 = arrayOf(
            findViewById(R.id.main_IMG_chocolate50),
            findViewById(R.id.main_IMG_chocolate51),
            findViewById(R.id.main_IMG_chocolate52)
        )

        chocolateMatrix = arrayOf(
            main_IMG_chocolate0,
            main_IMG_chocolate1,
            main_IMG_chocolate2,
            main_IMG_chocolate3,
            main_IMG_chocolate4,
            main_IMG_chocolate5
        )

        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)

    }


    private fun playerMovesInUI (sideClicked: Int ) { //0-left 1-right
       val move =  gameManager.playerMove(sideClicked)

        for (i in 0 until move.size)
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
}