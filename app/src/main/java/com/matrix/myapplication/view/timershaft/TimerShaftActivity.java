package com.matrix.myapplication.view.timershaft;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.myapplication.R;
import com.matrix.myapplication.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint({"InflateParams", "ClickableViewAccessibility", "HandlerLeak"})
public class TimerShaftActivity extends BaseActivity {
    PlaybackView playbackView;
    TextView textView;
    // 时间刻度的布局
    private LinearLayout flipper1;

    // 横向滑动的scroll
    private ScrollListenerHorizontalScrollView scrollView;

    // 整個時間軸的总长度
    private int maxlength = 0;

    // 可滑动最大距离
    private int scroll_max = 0;

    // 屏幕的宽度
    private int phone_wegith;

    // 用于HorizontalScrollView监听事件的handler
    private Handler handler = new Handler();

    // 每一个单位的秒数
    private int m;

    private DelayThread thread;

    private TextView now_time;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_shaft);
        initView();
    }

    private void initView() {
        /*playbackView = findViewById(R.id.playbackView);
        textView = findViewById(R.id.text);
        playbackView.setOnPlaybackViewListener(new PlaybackView.PlaybackViewListener() {
            @Override
            public void onValueChanged(String timeInMillis, long timeInMicros) {
                textView.setText(timeInMillis);
            }

            @Override
            public void onVideoStart(String timeInMillis, long timeInMicros) {

            }

            @Override
            public void onNoneVideo(String timeInMillis, long timeInMicros) {

            }
        });*/
        flipper1 = (LinearLayout) findViewById(R.id.flipper1);
        now_time = (TextView) findViewById(R.id.now_time);
        scrollView = (ScrollListenerHorizontalScrollView) findViewById(R.id.view);
        scrollView.setHandler(handler);
        // 获取手机屏幕的宽度
        WindowManager wm = this.getWindowManager();
        phone_wegith = wm.getDefaultDisplay().getWidth();
        scrollView.setScrollContainer(false);
        for (int i = 0; i < 28; i++) {
            addView(i);
        }
        thread = new DelayThread();
        thread.start();
        scrollView.setOnScrollStateChangedListener(new ScrollListenerHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(final ScrollListenerHorizontalScrollView.ScrollType scrollType) {
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
//                        MyLog.d(scrollView.getScrollX() + "--"+m+"--"+scroll_max);
                        if (scrollType == ScrollListenerHorizontalScrollView.ScrollType.IDLE) {
                            //停止滑动的状态
                            m = 86400 / scroll_max;
                        }
                        if (scrollType == ScrollListenerHorizontalScrollView.ScrollType.FLING) {
                            //处于正在滑动的状态
                            m = 86400 / scroll_max;
                        }
//                        now_time.setText(getStringTime((int) ((m + 0.571428571428) * scrollView.getScrollX())));
//                        now_time.setText(getStringTime((int) ((m + 0.142857142857) * scrollView.getScrollX())));
                        now_time.setText(getStringTime((int) (86400* scrollView.getScrollX())/scroll_max));

                    }
                };

            }
        });
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(scroll_max / 2, 0);

                try {
                    m = 86400 / scroll_max;
                    now_time.setText(getStringTime(43200));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }), 100);

    }

    public String getStringTime(int time) {
        if (time <= 0) {
            time = 0;
        }
        if (time >= 86399) {
            time = 86399;
        }

//        MyLog.d(time + "");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        Date date = new Date(time * 1000);
        return sdf.format(date);
    }

    /**
     * 添加时间轴的布局
     */
    @SuppressLint("NewApi")
    private void addView(int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.timeritem, null);
        TextView text = (TextView) view.findViewById(R.id.zheng);
        text.setText((i - 1) + ":");
        TextView ling = (TextView) view.findViewById(R.id.ling);
//        RelativeLayout play_line = (RelativeLayout) view.findViewById(R.id.play_line);
        RelativeLayout line = (RelativeLayout) view.findViewById(R.id.line);
        RelativeLayout shu = (RelativeLayout) view.findViewById(R.id.shu);

        // 获取view的宽高 此方法会加载onMeasure三次
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        maxlength = maxlength + width;
        if (i == 0) {
            ling.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
//            play_line.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
            // phone_wegith / 2 - width 屏幕一半的宽度 减去view的宽度 得到的值给予第一个view 从而使得时间轴正好对着初始0的位置
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(phone_wegith / 2 - width, RelativeLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(l);

        } else if (i == 1) {
            ling.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
//            play_line.setVisibility(View.INVISIBLE);
        } else if (i == 26) {
            text.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
            shu.setVisibility(View.INVISIBLE);
//            play_line.setVisibility(View.INVISIBLE);
        } else if (i == 27) {
            // phone_wegith / 2 - width 屏幕一半的宽度 减去view的宽度 得到的值给予第一个view 从而使得时间轴正好对着最后24的位置
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(phone_wegith / 2 - width, RelativeLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(l);
            text.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
            shu.setVisibility(View.INVISIBLE);
//            play_line.setVisibility(View.INVISIBLE);
            ling.setVisibility(View.INVISIBLE);
        } else {
//            play_line.setVisibility(View.VISIBLE);
            scroll_max = scroll_max + width;
        }
        flipper1.addView(view);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 线程用于监控滑动状态 以及后去滑动后的值
     */
    class DelayThread extends Thread {
        public void run() {
            while (true) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }
    }

}

