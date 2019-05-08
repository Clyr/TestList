package com.matrix.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by clyr on 2018/3/29 0029.
 */

public class ProgressCircleView extends View {
    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private int mCircleRadius;
    private RectF arcRectF;

    public ProgressCircleView(Context context) {
        super(context,null);
    }

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.GRAY);

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(50);
        allArcPaint.setColor(Color.RED);
        allArcPaint.setStrokeCap(Paint.Cap.SQUARE);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.SQUARE);
        progressPaint.setStrokeWidth(40);
        progressPaint.setColor(Color.GREEN);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        //圓弧的外接矩形
        arcRectF = new RectF();

    }
    private int mArcWidth;
    private PaintFlagsDrawFilter mDrawFilter;
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(mDrawFilter);
        //整个弧
        arcRectF.set(getWidth()/2-mCircleRadius+mArcWidth/2,getHeight()/2-mCircleRadius+mArcWidth/2
                ,getWidth()/2+mCircleRadius-mArcWidth/2,getHeight()/2+mCircleRadius-mArcWidth/2);
        canvas.drawArc(arcRectF, 135, 270, false, allArcPaint);
        progressPaint.setShader(new SweepGradient(getWidth()/2,getHeight()/2,Color.GREEN,Color.RED));
        canvas.drawArc(arcRectF, 135, 270, false, allArcPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureDimension(widthMeasureSpec),measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int specMode=MeasureSpec.getMode(measureSpec);
        int specSize=MeasureSpec.getSize(measureSpec);
        if(specMode==MeasureSpec.EXACTLY){
            result=specSize;
        }else{
            result=mCircleRadius*2;
            if(specMode==MeasureSpec.AT_MOST){
                result=Math.min(result,specSize);
            }
        }
        return result;
    }
}
