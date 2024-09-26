package com.example.a24c_10357hw1.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.a24c_10357hw1.interfaces.CallBack_MoveCallBack


class MoveDetector (context: Context , private val moveCallBack: CallBack_MoveCallBack){

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private lateinit var sensorEventListener : SensorEventListener

    private var timeStamp:Long = 0L


    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object:SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val x = event!!.values[0]
                val y = event.values[1]
                calculateMove(x,y)

            }



            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //do Nothing
            }

        }
    }
    private fun calculateMove(x: Float, speed: Float) {
        if(System.currentTimeMillis() - timeStamp >= 500) {
            timeStamp = System.currentTimeMillis()

            if(x >= 2.0)
                moveCallBack.changeDirection(1)  //right

            if(x <= -2.0)
               moveCallBack.changeDirection(0) //left

            if(speed >= 2.0)
                moveCallBack.changeSpeed(Constants.DELAY_FAST.toFloat())

            if(speed <= -2.0)
                moveCallBack.changeSpeed(Constants.DELAY_SLOW.toFloat())
        }

    }


     fun start() {
        sensorManager.registerListener(sensorEventListener, sensor,SensorManager.SENSOR_DELAY_NORMAL)

    }

     fun stop() {
        sensorManager.unregisterListener(sensorEventListener, sensor)
    }
}