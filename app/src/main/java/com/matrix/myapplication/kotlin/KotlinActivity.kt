package com.matrix.myapplication.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.matrix.myapplication.R
import kotlinx.android.synthetic.main.activity_kotlin.*
import kotlinx.android.synthetic.main.activity_main.*

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        init();
    }

    fun init() {
//        val textView = findViewById<TextView>(R.id.text)
        textview.setOnClickListener({
            textview.setText("WTK")
        })
        btnAlert.setOnClickListener({

        })
    }
}
