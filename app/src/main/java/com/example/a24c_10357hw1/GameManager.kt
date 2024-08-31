package com.example.a24c_10357hw1

import android.util.Log
import com.example.a24c_10357hw1.Constants.Companion.COLS
import com.example.a24c_10357hw1.Constants.Companion.ROWS
import kotlin.random.Random

class GameManager (private val lifeCount: Int = 3) {

   var numCrushes: Int = 0
        private set


    private var obstacleArray = arrayOf(
        arrayOf(0, 0, 0),
        arrayOf(0, 0, 0),
        arrayOf(0, 0, 0),
        arrayOf(0, 0, 0),
        arrayOf(0, 0, 0),
        arrayOf(0, 0, 0)
    )

    private var playerLocation =
        arrayOf(0, 1, 0) //player starts at the middle -> 0 = left , 1 = middle , 2 = right


    val gameLost: Boolean
        get() = numCrushes == lifeCount


    fun playerMove(side: Int): Array<Int> { //0 to left , 1 to right
        val location = getPlayerLocation()

        if (location == 1) {
            if (side == 0)
                setPlayerLocation(1, 0, 0, playerLocation)
            if (side == 1)
                setPlayerLocation(0, 0, 1, playerLocation)
        } else if (location == 0) {
            if (side == 0)
                setPlayerLocation(1, 0, 0, playerLocation)
            if (side == 1)
                setPlayerLocation(0, 1, 0, playerLocation)
        } else if (location == 2) {
            if (side == 0)
                setPlayerLocation(0, 1, 0, playerLocation)
            if (side == 1)
                setPlayerLocation(0, 0, 1, playerLocation)
        }

        return playerLocation

    }

    private fun getPlayerLocation(): Int {
        var location: Int = -1
        for (i in 0 until playerLocation.size)
            if (playerLocation[i] == 1)
                location = i;

        return location
    }

    private fun setPlayerLocation(a: Int, b: Int, c: Int, arr: Array<Int>) {
        arr[0] = a
        arr[1] = b
        arr[2] = c
    }

    fun moveObstacles(): Array<Array<Int>> {
        val randonNumber: Int = Random.nextInt(3)
        Log.d("random" , "mess" + randonNumber)

        for (i in 0 until ROWS - 1)
            obstacleArray[ROWS - i - 1] = obstacleArray[ROWS - i - 2]

        obstacleArray[0] = arrayOf(0,0,0)

        for (j in 0 until COLS) {
            if (j == randonNumber)
                obstacleArray[0][j] = 1
            else
                obstacleArray[0][j] = 0


        }
       // obstacleArray[0] = arrayOf(0,0,0)
       // obstacleArray[0][randonNumber] = 1

        return obstacleArray

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



}