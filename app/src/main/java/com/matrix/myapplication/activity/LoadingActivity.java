package com.matrix.myapplication.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.matrix.myapplication.R;
import com.matrix.myapplication.view.MyView;

import java.util.ArrayList;

public class LoadingActivity extends BaseActivity {
int[] colors={Color.GREEN,Color.RED,Color.CYAN,Color.BLUE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        MyView myView = (MyView) findViewById(R.id.myview);
        myView.setIsHaveAnim(true);
        myView.setColors(colors);
        myView.setPercents(getPercentValue());
    }
    public ArrayList<Float> getPercentValue() {
        ArrayList<Float> list = new ArrayList<>();
//        for (int i = 0;i<5;i++){
//            list.add(0.15f);
//        }
        list.add(0.7f);
        list.add(0.1f);
        list.add(0.1f);
        list.add(0.1f);
        return list;
    }
}
