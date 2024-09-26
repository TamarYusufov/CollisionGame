package com.example.a24c_10357hw1.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.a24c_10357hw1.R
import com.example.a24c_10357hw1.utilities.Constants
import com.google.android.material.imageview.ShapeableImageView

class MenuActivity : AppCompatActivity() {

    private lateinit var fast_BTN_mode: Button //1
    private lateinit var slow_BTN_mode: Button //2
    private lateinit var sensor_BTN_mode: Button //3
    private lateinit var record_table_BTN: Button // 4
    private lateinit var  menu_IMG_icon: ShapeableImageView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()

    }

    private fun initViews() {

        fast_BTN_mode.setOnClickListener{ switchActivities(1)}
        slow_BTN_mode.setOnClickListener{ switchActivities(2)}
        sensor_BTN_mode.setOnClickListener{ switchActivities(3)}

        record_table_BTN.setOnClickListener{ switchActivities(4) }


    }



    private fun switchActivities(buttonClicked: Int) {
        when (buttonClicked) {
            1 -> {
                val mainActivity = Intent(this , MainActivity::class.java)
                mainActivity.putExtra(Constants.MODE , "FAST_MODE")
                startActivity(mainActivity)
            }
            2 -> {
                val mainActivity = Intent(this , MainActivity::class.java)
                mainActivity.putExtra(Constants.MODE , "SLOW_MODE")
                startActivity(mainActivity)
            }
            3 -> {
                val mainActivity = Intent(this , MainActivity::class.java)
                mainActivity.putExtra(Constants.MODE , "SENSOR_MODE")
                startActivity(mainActivity)
            }
            4 -> {
                val scoresActivity = Intent(this , ScoresActivity::class.java)
                startActivity(scoresActivity)
            }
        }

    }

    private fun findViews() {
        fast_BTN_mode = findViewById(R.id.fast_BTN_mode)
        slow_BTN_mode = findViewById(R.id.slow_BTN_mode)
        sensor_BTN_mode = findViewById(R.id.sensor_BTN_mode)
        record_table_BTN = findViewById(R.id.record_table_BTN)
        menu_IMG_icon = findViewById(R.id.menu_IMG_icon)
    }
}