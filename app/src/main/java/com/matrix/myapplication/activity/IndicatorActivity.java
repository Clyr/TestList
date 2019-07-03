package com.matrix.myapplication.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.matrix.myapplication.LoadingDialog.AVLoadingIndicatorView;
import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.ToastUtils;

/**
 * Created by Jack Wang on 2016/8/5.
 */

public class IndicatorActivity extends BaseActivity {

    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator);

        String indicator = getIntent().getStringExtra("indicator");
        ToastUtils.showLong(indicator);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
    }

    public void hideClick(View view) {
        avi.hide();
        // or avi.smoothToHide();
    }

    public void showClick(View view) {
        avi.show();
        // or avi.smoothToShow();
    }
}
