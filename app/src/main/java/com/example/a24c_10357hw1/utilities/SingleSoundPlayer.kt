package com.example.a24c_10357hw1.utilities

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SingleSoundPlayer (context: Context){

    private val context:Context = context.applicationContext

    private val executor: Executor = Executors.newSingleThreadScheduledExecutor()

    fun playSound(resId: Int) {
        executor.execute{
            val mediaPlayer = MediaPlayer.create(context,resId)
            mediaPlayer.isLooping = false
            mediaPlayer.setVolume(0.5f, 0.5f)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp: MediaPlayer? ->
                var mp = mp
                mp!!.stop()
                mp.release()
                mp = null


            }
        }
    }


}