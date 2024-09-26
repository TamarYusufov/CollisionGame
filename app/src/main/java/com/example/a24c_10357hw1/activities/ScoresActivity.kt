package com.example.a24c_10357hw1.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.a24c_10357hw1.R
import com.example.a24c_10357hw1.fragments.MapsFragment
import com.example.a24c_10357hw1.fragments.ScoreTableFragment

class ScoresActivity : AppCompatActivity() {

    private lateinit var main_FRAME_list: FrameLayout
    private lateinit var main_FRAME_map: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_score)
        findViews()
        initViews()



    }

    private fun initViews() {
        val mapFragment = MapsFragment()
        supportFragmentManager.beginTransaction().add(main_FRAME_map.id,mapFragment).commit()

        val scoreTableFragment = ScoreTableFragment()
        supportFragmentManager.beginTransaction().add(main_FRAME_list.id,scoreTableFragment).commit()

    }

    private fun findViews() {
        main_FRAME_list  = findViewById(R.id.main_FRAME_list)
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
    }
}