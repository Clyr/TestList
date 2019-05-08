package com.matrix.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.matrix.myapplication.R;

public class Main2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
    }

    private void init() {
        final Intent intent = new Intent();
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {//WebViewJavaScript
            @Override
            public void onClick(View v) {
                intent.setClass(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(Main2Activity.this,MarqueeActivity.class);
                startActivity(intent);
            }
        });
    }
}
