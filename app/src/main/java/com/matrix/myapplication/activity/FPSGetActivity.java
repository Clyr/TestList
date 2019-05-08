package com.matrix.myapplication.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.matrix.myapplication.R;
import com.matrix.myapplication.view.FlakeView;

public class FPSGetActivity extends BaseActivity {
    private FlakeView flakeView;
    private static CheckBox accelerated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpsget);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        accelerated = (CheckBox) findViewById(R.id.accelerated);
        flakeView = new FlakeView(this);
        container.addView(flakeView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        Button more = (Button) findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flakeView.addFlakes(flakeView.getNumFlakes());
            }
        });
        Button less = (Button) findViewById(R.id.less);
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flakeView.subtractFlakes(flakeView.getNumFlakes() / 2);
            }
        });
        if (Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.HONEYCOMB) {
            HoneycombHelper.setup(this);
        }
    }

    private static final class HoneycombHelper {
        static void setup(final FPSGetActivity activity) {
            accelerated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    activity.flakeView.setLayerType(
                            isChecked ? View.LAYER_TYPE_NONE : View.LAYER_TYPE_SOFTWARE, null);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        flakeView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();
    }

}
