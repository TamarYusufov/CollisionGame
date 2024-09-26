package com.example.a24c_10357hw1.model

import android.util.Log
import com.example.a24c_10357hw1.utilities.Constants.Companion.COLS
import com.example.a24c_10357hw1.utilities.Constants.Companion.POINTS_FOR_COIN
import com.example.a24c_10357hw1.utilities.Constants.Companion.ROWS
import kotlin.random.Random

class GameManager (private val lifeCount: Int = 3) {

   var numCrushes: Int = 0
        private set

    var score: Int = 0
        private set

    var gamesData : List<Game> = mutableListOf()

    private var obstacleArray = arrayOf(
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0)
    )

    private var coinArray = arrayOf(
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0),
        arrayOf(0,0,0,0,0)
    )

    private var playerLocation =
        arrayOf(0,0,1,0,0)


    val gameLost: Boolean
        get() = numCrushes == lifeCount


    fun addGame(game: Game) = apply {(this.gamesData as MutableList).add(game) }


    fun playerMove(side: Int): Array<Int> { //0 to left , 1 to right
        val location = getPlayerLocation()

        if (location == 1) {
            if (side == 0)
                setPlayerLocation(1, 0, 0,0,0, playerLocation)
            if (side == 1)
                setPlayerLocation(0, 0, 1,0,0, playerLocation)
        } else if (location == 0) {
            if (side == 0)
                setPlayerLocation(1, 0, 0,0,0, playerLocation)
            if (side == 1)
                setPlayerLocation(0, 1, 0,0,0, playerLocation)
        } else if (location == 2) {
            if (side == 0)
                setPlayerLocation(0, 1, 0,0,0, playerLocation)
            if (side == 1)
                setPlayerLocation(0, 0, 0,1,0, playerLocation)
        } else if (location == 3) {
            if (side == 0)
                setPlayerLocation(0, 0,1, 0,0, playerLocation)
            if (side == 1)
                setPlayerLocation(0,0,0, 0, 1, playerLocation)
        } else if (location == 4) {
            if (side == 0)
                setPlayerLocation(0,0,0,1,0, playerLocation)
            if (side == 1)
                setPlayerLocation(0,0,0, 0, 1, playerLocation)
        }

        return playerLocation

    }

    private fun getPlayerLocation(): Int {
        var location = 0
        for (i in playerLocation.indices)
            if (playerLocation[i] == 1)
                location = i

        return location
    }

    private fun setPlayerLocation(a: Int, b: Int, c: Int,d: Int, e:Int, arr: Array<Int>) {
        arr[0] = a
        arr[1] = b
        arr[2] = c
        arr[3] = d
        arr[4] = e
    }

    fun moveObstacles(): Array<Array<Int>> {
        val randomNumber: Int = Random.nextInt(5)
        Log.d("random" , "mess$randomNumber")

        for (i in 0 until ROWS - 1)
            obstacleArray[ROWS - i - 1] = obstacleArray[ROWS - i - 2]

        obstacleArray[0] = arrayOf(0,0,0,0,0)

        for (j in 0 until COLS) {
            if (j == randomNumber)
                obstacleArray[0][j] = 1
            else
                obstacleArray[0][j] = 0
        }
        return obstacleArray

    }



    fun moveCoins(): Array<Array<Int>> {
        val randomNumber: Int = Random.nextInt(5)
        Log.d("random" , "mess$randomNumber")

        for (i in 0 until ROWS - 1)
            coinArray[ROWS - i - 1] = coinArray[ROWS - i - 2]

        coinArray[0] = arrayOf(0,0,0,0,0)

        for (j in 0 until COLS) {
            if (j == randomNumber)
                coinArray[0][j] = 1
            else
                coinArray[0][j] = 0


        }
        return coinArray
    }


    fun hasCrushed(): Boolean {
       if(!gameLost) {
           for (i in 0 until COLS)
               if (playerLocation[i] == 1 && obstacleArray[ROWS-1][i] == 1) {
                   numCrushes += 1
                   return true

               }
       }
        return false

    }

    fun gotCoin(): Boolean {
        if(!gameLost) {
            for (i in 0 until COLS)
                if (playerLocation[i] == 1 && coinArray[ROWS-1][i] == 1) {
                    score += POINTS_FOR_COIN
                    return true
                }
        }
        return false
    }



}