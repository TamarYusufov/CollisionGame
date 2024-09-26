package com.example.a24c_10357hw1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.a24c_10357hw1.R
import com.google.android.material.textview.MaterialTextView


class ScoreTableFragment : Fragment() {

    private lateinit var score_LBL: MaterialTextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val v =  inflater.inflate(R.layout.fragment_score_table, container, false)

        findViews(v)
        initViews(v)

        return v
    }

    private fun initViews(v: View) {

    }

    private fun findViews(v: View) {
        score_LBL = v.findViewById(R.id.score_LBL)
    }


}