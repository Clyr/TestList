package com.matrix.myapplication.kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.matrix.myapplication.R
import com.matrix.myapplication.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_kotlin.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.text as text1

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        init();
    }

    fun init() {
//        val textView = findViewById<TextView>(R.id.text)
        /*text.setOnClickListener({
            text.setText("WTK")
        })*/
        btnAlert.setOnClickListener({
            text.setText("WTK")
            ToastUtils.showLong("btnAlert");
        })
        toast.setOnClickListener({
            Toast.makeText(this,"toast",Toast.LENGTH_SHORT).show();
        })
    }
}
