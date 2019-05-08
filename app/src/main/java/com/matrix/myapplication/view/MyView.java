package com.matrix.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.matrix.myapplication.R;

import java.util.ArrayList;

/**
 * Created by clyr on 2018/3/9 0009.
 */

/**
 * 自定义圆环
 */
public class MyView extends View {
    private int color = Color.parseColor("#e8e8e8");   //画圆的颜色  默认红色
    private float cirle = 200;   //半径范围大小 默认200
    private Paint mPaint;
    private int widthSize, heightSize;    //屏幕宽高
    private int[] colors;   //颜色数组
    private ArrayList<Float> percents;   //百分比数组
    private float percent;   //总共百分比
    private Boolean flag = true;    // 只执行一次
    private float[] step;   //保存每个圆弧的弧度
    private float[] percentnum;   //保存每个圆弧的起始弧度
    private boolean isAnim = false;    //是否使用动画 默认不使用


    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MyView_my_view_color:
                    color = a.getColor(R.styleable.MyView_my_view_color, 0);
                    break;
                case R.styleable.MyView_my_view_cirle:
                    cirle = a.getDimension(R.styleable.MyView_my_view_cirle, 0f);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);  //消除锯齿
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //外圈圆
        mPaint.setColor(color);   //默认颜色(底部最初圆环颜色)
        canvas.drawCircle(widthSize / 2, heightSize / 2, cirle, mPaint);
        RectF oval = new RectF(widthSize / 2 - cirle, heightSize / 2 - cirle, widthSize / 2 + cirle, heightSize / 2 + cirle);
        if (isAnim) {
            addCircleAnim(canvas, oval);
        } else {
            circleNoAnim(canvas, oval);
        }

        //内圈圆
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(widthSize / 2, heightSize / 2, cirle - cirle / 4, mPaint);


    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public ArrayList<Float> getPercents() {
        return percents;
    }

    public void setPercents(ArrayList<Float> percents) {
        this.percents = percents;
    }

    /**
     * 是否使用动画
     *
     * @param isHaveAnim
     */
    public void setIsHaveAnim(Boolean isHaveAnim) {
        if (isHaveAnim) {
            this.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            new Thread(new CircleThread()).start();
                            getViewTreeObserver().removeOnPreDrawListener(this);
                            return false;
                        }
                    });
        }
        this.isAnim = isHaveAnim;
    }

    /**
     * 检查百分比是否超过100%
     */
    private float checkPercent(ArrayList<Float> percents) {
        if (percents == null) {
            Log.d("tag", "总百分比为空");
            return 0;
        }
        for (int i = 0; i < percents.size(); i++) {
            percent += percents.get(i);
        }
        Log.d("tag", "percent-----" + percent);
        return percent;
    }

    private class CircleThread implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(20);
                    /**
                     * 在新的线程中，发送消息给View，更新界面数据
                     * 通过m++实现加速度方式的不断加速绘制弧形
                     * i代表要绘制扇形的角度大小，0-360
                     */
                    flag = false;
                    m += 2;
                    Message msg = new Message();
                    msg.what = 1;
                    if (i < 360) {
                        i = m;
                    } else {
                        i = 360;
                        return;
                    }
                    msg.obj = i;
                    circleHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private Handler circleHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int temp = (Integer) msg.obj;
                setTemp(temp);
                invalidate();
            }
        }
    };

    private int temp, i, m;

    private void setTemp(int temp) {
        this.temp = temp;
    }

    /**
     * 带动画的画圆
     *
     * @param canvas
     * @param oval
     */
    private void addCircleAnim(Canvas canvas, RectF oval) {
        float nextPercent = 0;
        //初始化赋值，flag = true 只执行一次（有动画效果）
        if (flag) {
            try {
                if (checkPercent(percents) > 1) {
                    throw new Exception("百分比不能大于1");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            step = new float[percents.size()];
            percentnum = new float[percents.size()];
            for (int i = 0; i < percents.size(); i++) {
                step[i] = 360 * percents.get(i);
                percentnum[i] = nextPercent;
                nextPercent += 360 * percents.get(i);
            }
        } else {
            for (int i = 0; i < percents.size(); i++) {
                if (temp <= step[i]) {
                    mPaint.setColor(colors[i]);
                    canvas.drawArc(oval, percentnum[i], temp, true, mPaint);
                } else {
                    mPaint.setColor(colors[i]);
                    canvas.drawArc(oval, percentnum[i], step[i], true, mPaint);
                }
            }
        }
    }

    /**
     * 不带动画的画圆
     *
     * @param canvas
     * @param oval
     */
    private void circleNoAnim(Canvas canvas, RectF oval) {
        try {
            if (checkPercent(percents) > 1) {
                throw new Exception("百分比不能大于1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //循环画圆环
        float nowPercent = 0;
        float nextPercent = 0;
        //绘制圆环  （无动画效果）
        for (int i = 0; i < percents.size(); i++) {
            mPaint.setColor(colors[i]);
            nextPercent += 360 * percents.get(i);
            canvas.drawArc(oval, nowPercent, 360 * percents.get(i), true, mPaint);     //   360*percents.get(i)此参数写法对画圆环很重要
            nowPercent = nextPercent;
        }
    }
}
