package com.mm131;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.matrix.myapplication.R;

public class ImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        Intent intent = getIntent();
        String ull = intent == null ? "" : intent.getStringExtra("url");
    }
}
