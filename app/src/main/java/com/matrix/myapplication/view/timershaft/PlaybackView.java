package com.matrix.myapplication.view.timershaft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by M S I of clyr on 2019/9/25.
 */
public class PlaybackView extends View {
    private static String TAG = "PlaybackView>>";
    private Paint mPaint;
    private static final int SCALE_COLOR = 0xFF7C7C7C;
    private static final int VIDEO_COLOR = 0xFFffab8a;
    private static final int MID_COLOR = 0xFF039BE5;
    private static final int MID_BACKGROUND_COLOR = 0x007C7C7C;
    private int VIEW_BACKGROUND_COLOR = 0x00000000;
    private static final float TWO_MIN_SCALE_HEIGHT = 5;
    private static final float TEN_MIN_SCALE_HEIGHT = 10;
    private static final float HOUR_SCALE_HEIGHT = 15;
    private float widthTimes = 1.5f;
    private List<PlaybackVo> videos = new ArrayList<>();


    private float SECOND_WIDTH = 30;//px

    private float MIN_SECOND_WIDTH = 5;
    private float MAX_SECOND_WIDTH = 30;


    private static final int TWO_MINUTE_SCALE_INTERVAL = 120;
    private static final int TEN_MINUTE_SCALE_INTERVAL = 600;
    private static final int TWENTY_MINUTE_SCALE_INTERVAL = 1200;
    private static final int ONE_HOUR_SCALE_INTERVAL = 3600;
    private static final int TWO_HOUR_SCALE_INTERVAL = 7200;


    private static final int SCALE_TEXT_SIZE = 12;
    private int midBackgroundWidth = 5;//dp


    private float mWidth, mHeight;

    private float mDensity;// 屏幕密度 dp*mDensity=px
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMinVelocity;

    private TimeAlgorithm mValue;// 当前刻度指示时间
    private TimeAlgorithm selectValue;//指定移动时间

    private int mScale = 3;

    public PlaybackView(Context context) {
        this(context, null);
    }

    public PlaybackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaybackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mDensity = context.getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPaint.setColor(SCALE_COLOR);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mValue = new TimeAlgorithm(System.currentTimeMillis() * 1000);
        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        //   Log.d(TAG, "init: " + mDensity);

    }

    public void moveToValue(long micros) {
        if (micros == mValue.getTimeInMicros()) return;
        Message msg = Message.obtain();
        msg.what = SET_VALUE;
        selectValue = new TimeAlgorithm(micros);
        if (isCurrentDay(mValue, selectValue)) {
            moveX = (selectValue.getTimeInSecond() - mValue.getTimeInSecond()) * SECOND_WIDTH / SET_VALUE_AUTO_MOVE_COUNT;
            if (Math.abs(moveX) < SECOND_WIDTH && moveX != 0) {
                moveX = moveX * SECOND_WIDTH / moveX;
            }
            //    Log.d(TAG, "moveToValue: " + moveX + ">>" + SECOND_WIDTH);
            handler.sendMessage(msg);
        }
    }

    public void setValue(long micros) {
        removeAllMessage();
        mValue = new TimeAlgorithm(micros);
        postInvalidate();
        if (listener != null) {
            listener.onValueChanged(mValue.getStringTimeInSecond(), mValue.getTimeInMicros());
        }
        handler.sendEmptyMessageDelayed(DELAY_MOVE_TO_NEARBY_VIDEO, delayMoveToNearbyVideoMs);
    }

    private void removeAllMessage() {
        handler.removeMessages(DELAY_MOVE_TO_NEARBY_VIDEO);
        handler.removeMessages(SET_VALUE);
    }

    public void setViewBackgroundColor(int color) {
        this.VIEW_BACKGROUND_COLOR = color;
    }

    public void setVideos(List<PlaybackVo> videos) {
        if (videos == null || videos.size() == 0) return;
        this.videos = videos;
        moveToNearbyVideo();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Log.d(TAG, "onSizeChanged: ");
        mWidth = w;
        mHeight = h;

        MAX_SECOND_WIDTH = mWidth / (1800 * mScale);
        MIN_SECOND_WIDTH = mWidth / (60 * 60 * 24 * mScale);
        SECOND_WIDTH = MAX_SECOND_WIDTH;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // Log.d(TAG, "onLayout:  " + changed + ">>left=" + left + ">>top=" + top + ">>right=" + right + ">>bottom=" + bottom);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //  Log.d(TAG, "onDraw: ");
    }

    private int SET_VALUE_AUTO_MOVE_TIME = 100;//ms
    private int SET_VALUE_AUTO_MOVE_COUNT = 10;//times
    private float moveX = 0;
    private static final int SET_VALUE = 0;
    private static final int DELAY_MOVE_TO_NEARBY_VIDEO = 1;
    private int delayMoveToNearbyVideoMs = 1000;//ms
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (hasMessages(DELAY_MOVE_TO_NEARBY_VIDEO)) {
                removeMessages(DELAY_MOVE_TO_NEARBY_VIDEO);
            }
            switch (msg.what) {
                case SET_VALUE:
                    autoMoving();
                    break;
                case DELAY_MOVE_TO_NEARBY_VIDEO:
                    moveToNearbyVideo();
                    break;

            }
        }
    };

    private void autoMoving() {
        TimeAlgorithm value = mValue.addOrSub((int) (moveX / SECOND_WIDTH));
        if (moveX > 0) {
            if (value.getTimeInMicros() > selectValue.getTimeInMicros()) {
                mValue = new TimeAlgorithm(selectValue.getTimeInMicros());
                // Log.d(TAG, "autoMoving: >");
                invalidate();
                handler.removeMessages(SET_VALUE);
            }
        } else if (moveX < 0) {
            if (value.getTimeInMicros() < selectValue.getTimeInMicros()) {
                mValue = new TimeAlgorithm(selectValue.getTimeInMicros());
                invalidate();
                // Log.d(TAG, "autoMoving: <");
                handler.removeMessages(SET_VALUE);
            }
        }
        if (mValue.getTimeInMicros() != selectValue.getTimeInMicros()) {
            moveX(moveX);
            handler.sendEmptyMessageDelayed(SET_VALUE, SET_VALUE_AUTO_MOVE_TIME / SET_VALUE_AUTO_MOVE_COUNT);
        } else {
            if (listener != null) {
                listener.onVideoStart(mValue.getStringTimeInSecond(), mValue.getTimeInMicros());
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // Log.d(TAG, "dispatchDraw: ")；
        // canvas.drawColor(VIEW_BACKGROUND_COLOR);
        drawVideos(canvas);
        drawScale(canvas);
        drawMidLine(canvas);


    }

    private int VIDEO_PADDING = 1;

    private void drawVideos(Canvas canvas) {
        if (videos == null || videos.size() == 0) return;
        canvas.save();
        for (int i = 0; i < videos.size(); i++) {

            PlaybackVo vo = videos.get(i);
            float startX = mWidth / 2 + (vo.getStartTA().getTimeInSecond() - mValue.getTimeInSecond()) * SECOND_WIDTH;
            float endX = mWidth / 2 + (vo.getEndTA().getTimeInSecond() - mValue.getTimeInSecond()) * SECOND_WIDTH;
            if (endX < 0 || startX > mWidth) continue;
            RectF rectF = new RectF(startX, VIDEO_PADDING, endX, mHeight - VIDEO_PADDING);
            mPaint.setColor(VIDEO_COLOR);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectF, mPaint);
        }
        canvas.restore();
    }


    private void drawScale(Canvas canvas) {
        canvas.save();
        mPaint.setColor(SCALE_COLOR);
        mPaint.setStrokeWidth(mDensity);

        int scaleMode = getScaleMode();
        int mod = mValue.mod(scaleMode);
        float drawWidth = 0;

        for (int i = 0; drawWidth < mWidth * 1.1; i++) {

            float height = TWO_MIN_SCALE_HEIGHT;
            float x = 0;
            int mode = 0;
            x = mWidth / 2 + (scaleMode - mod + scaleMode * i) * SECOND_WIDTH;
            TimeAlgorithm rightTime = mValue.addOrSub(scaleMode - mod + scaleMode * i);
            String time = rightTime.getStringTimeInMinute();
            Rect rect = new Rect();
//            Log.d(TAG, "isCurrentDayContainZero: " + rightTime.getStringTime() + ">>" + scaleMode
//                    + ">>" + isCurrentDayContainZero(mValue, rightTime) + ">>" + isDrawScaleLine(rightTime)
//                    + ">>" + mValue.getTimeInMillis() + ">>" + mValue.getCurrentMaxTimeInMillis() + ">>" + mValue.getCurrentMinTimeInMillis() + ">>" + rightTime.getTimeInMillis());
            if (isCurrentDayContainZero(mValue, rightTime)) {
                mode = rightTime.getScaleMode();
                height = getScaleHeight(rightTime) * mDensity;
                if (isDrawScaleLine(rightTime)) {
                    canvas.drawLine(x, 0, x, height, mPaint);
                    canvas.drawLine(x, mHeight, x, mHeight - height, mPaint);
                }
                mPaint.setTextSize(SCALE_TEXT_SIZE * mDensity);
                mPaint.getTextBounds(time, 0, time.length(), rect);
                if (isDrawText(rightTime, rect.width() * widthTimes)) {
                    if (time.equals("00:00")) time = "24:00";
                    canvas.drawText(time, x - rect.width() / 2, height + rect.height() + 2 * mDensity, mPaint);
                }

            }

            x = mWidth / 2 - (mod + scaleMode * i) * SECOND_WIDTH;
            TimeAlgorithm leftTime = mValue.addOrSub(-(mod + scaleMode * i));
            height = getScaleHeight(leftTime) * mDensity;
            if (isCurrentDayContainZero(mValue, leftTime)) {
                if (isDrawScaleLine(leftTime)) {
                    canvas.drawLine(x, 0, x, height, mPaint);
                    canvas.drawLine(x, mHeight, x, mHeight - height, mPaint);
                }
                time = leftTime.getStringTimeInMinute();
                mPaint.getTextBounds(time, 0, time.length(), rect);
                mode = leftTime.getScaleMode();
                if (isDrawText(leftTime, rect.width() * widthTimes)) {
                    canvas.drawText(time, x - rect.width() / 2, height + rect.height() + 2 * mDensity, mPaint);
                }
            }
            drawWidth += 2 * scaleMode * SECOND_WIDTH;
        }
        mPaint.setStrokeWidth(1.5f * mDensity);
        canvas.drawLine(0, 0, mWidth, 0, mPaint);
        canvas.drawLine(0, mHeight, mWidth, mHeight, mPaint);
        canvas.restore();
    }

    private void drawMidLine(Canvas canvas) {
        canvas.save();
        //  Log.d(TAG, "drawMidLine: width=" + mWidth + ">>height=" + mHeight);
        mPaint.setColor(MID_BACKGROUND_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF((mWidth - midBackgroundWidth * mDensity) / 2, 0, (mWidth + midBackgroundWidth * mDensity) / 2, mHeight);
        canvas.drawRect(rectF, mPaint);
        mPaint.setStrokeWidth(1.5f * mDensity);
        mPaint.setColor(MID_COLOR);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, mPaint);
        canvas.restore();
        // Log.d(TAG, "drawMidLine: " + mValue.getStringTime());
    }

    private boolean isCurrentDayContainZero(TimeAlgorithm current, TimeAlgorithm compare) {
        boolean isCurrentDay = true;
        if (current.getCurrentMaxTimeInMillis() / 1000 + 1 < compare.getTimeInMillis() / 1000) {
            isCurrentDay = false;
        } else if (current.getCurrentMinTimeInMillis() > compare.getTimeInMillis()) {
            isCurrentDay = false;
        }
        return isCurrentDay;
    }

    private boolean isCurrentDay(TimeAlgorithm current, TimeAlgorithm compare) {
        boolean isCurrentDay = true;
        if (current.getCurrentMaxTimeInMillis() / 1000 < compare.getTimeInMillis() / 1000) {
            isCurrentDay = false;
        } else if (current.getCurrentMinTimeInMillis() > compare.getTimeInMillis()) {
            isCurrentDay = false;
        }
        return isCurrentDay;
    }

    private int getScaleMode() {
        int mode = TWO_MINUTE_SCALE_INTERVAL;
        if (ONE_HOUR_SCALE_INTERVAL * SECOND_WIDTH <= TWO_MINUTE_SCALE_INTERVAL * MAX_SECOND_WIDTH) {
            mode = ONE_HOUR_SCALE_INTERVAL;
        } else if (TWENTY_MINUTE_SCALE_INTERVAL * SECOND_WIDTH <= TWO_MINUTE_SCALE_INTERVAL * MAX_SECOND_WIDTH) {
            mode = TWENTY_MINUTE_SCALE_INTERVAL;
        } else if (TEN_MINUTE_SCALE_INTERVAL * SECOND_WIDTH <= 2 * TWO_MINUTE_SCALE_INTERVAL * MAX_SECOND_WIDTH) {
            mode = TEN_MINUTE_SCALE_INTERVAL;
        }
        return mode;
    }

    private boolean isDrawScaleLine(TimeAlgorithm time) {
        boolean draw = false;
        switch (time.getScaleMode()) {
            case TimeAlgorithm.MODE_TWO_MINUTE:
                draw = TEN_MINUTE_SCALE_INTERVAL * SECOND_WIDTH > TWO_MINUTE_SCALE_INTERVAL * MAX_SECOND_WIDTH * 2;
                break;
            case TimeAlgorithm.MODE_TEN_MINUTE:
                draw = (time.isTwentyMinuteMultiple() && ONE_HOUR_SCALE_INTERVAL * SECOND_WIDTH > 2 * TWO_MINUTE_SCALE_INTERVAL * MAX_SECOND_WIDTH)
                        || (!time.isTwentyMinuteMultiple() && TWENTY_MINUTE_SCALE_INTERVAL * SECOND_WIDTH > 1.5 * TWO_MINUTE_SCALE_INTERVAL * MAX_SECOND_WIDTH);
                break;
            case TimeAlgorithm.MODE_HOUR:
                draw = ONE_HOUR_SCALE_INTERVAL * SECOND_WIDTH > TWO_MINUTE_SCALE_INTERVAL * MAX_SECOND_WIDTH
                        || time.isTwoHourMultiple();
                break;

        }

        return draw;
    }

    private boolean isDrawText(TimeAlgorithm time, float width) {
        if (time.getScaleMode() < TimeAlgorithm.MODE_TEN_MINUTE) return false;
        //  Log.d(TAG, "isDrawText: " + time.isTwoHourMultiple() + ">>" + time.getStringTime());
        return TEN_MINUTE_SCALE_INTERVAL * SECOND_WIDTH > width
                || (TWENTY_MINUTE_SCALE_INTERVAL * SECOND_WIDTH > width && time.isTwentyMinuteMultiple())
                || (time.getScaleMode() == TimeAlgorithm.MODE_HOUR && ONE_HOUR_SCALE_INTERVAL * SECOND_WIDTH > width)
                || time.isTwoHourMultiple();
    }

    private float getScaleHeight(TimeAlgorithm t) {
        float height = TWO_MIN_SCALE_HEIGHT;
        switch (t.getScaleMode()) {
            case TimeAlgorithm.MODE_HOUR:
                height = HOUR_SCALE_HEIGHT;
                break;
            case TimeAlgorithm.MODE_TEN_MINUTE:
                height = TEN_MIN_SCALE_HEIGHT;
                break;
            case TimeAlgorithm.MODE_TWO_MINUTE:
                height = TWO_MIN_SCALE_HEIGHT;
                break;
        }
        return height;
    }


    private float downOneX, downTwoX, downOneY, downTwoY;
    private int clickCount = 0;
    private float distance;
    private boolean needDelay;


    private long clickTime;//ms

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        lastX = 0;
        mScroller.forceFinished(true);
        //  Log.d(TAG, "onTouchEvent: ");
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                handler.removeMessages(DELAY_MOVE_TO_NEARBY_VIDEO);
                handler.removeMessages(SET_VALUE);
                downOneX = event.getX(0);
                downOneY = event.getY(0);
                clickCount++;
                if (clickCount == 2 && System.currentTimeMillis() - clickTime > 100) {//防抖
                    SECOND_WIDTH = MAX_SECOND_WIDTH;//SECOND_WIDTH == MAX_SECOND_WIDTH ? MIN_SECOND_WIDTH : MAX_SECOND_WIDTH;
                    // Log.d(TAG, "onTouchEvent: 恢复 >>" + (System.currentTimeMillis() - clickTime));
                    invalidate();
                }
                clickTime = System.currentTimeMillis();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clickCount = 0;
                    }
                }, 200);
                //  Log.d(TAG, "onTouchEvent: ACTION_DOWN " + downOneX + ">>" + event.getPointerCount());
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() > 1) {
                    downTwoX = event.getX(1);
                    downTwoY = event.getY(1);
                }
                //   Log.d(TAG, "onTouchEvent: ACTION_POINTER_DOWN " + downTwoX + ">>" + event.getPointerCount());

                break;
            case MotionEvent.ACTION_MOVE:
                move(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                distance = 0;
                needDelay = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        needDelay = false;
                    }
                }, 200);
                //  Log.d(TAG, "onTouchEvent: ACTION_POINTER_UP " + event.getPointerCount());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Log.d(TAG, "onTouchEvent: ACTION_UP");
                distance = 0;
                autoScroll();
                needDelay = false;
                break;
        }
        return super.onTouchEvent(event);

    }

    private void autoScroll() {
        if (needDelay) {
            handler.sendEmptyMessageDelayed(DELAY_MOVE_TO_NEARBY_VIDEO, delayMoveToNearbyVideoMs);
            return;
        }
        mVelocityTracker.computeCurrentVelocity((int) mWidth, Float.MAX_VALUE);
        float xVelocity = mVelocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > mMinVelocity) {
            //   Log.d(TAG, "autoScroll: " + xVelocity + ">>" + mMinVelocity);
            mScroller.forceFinished(false);
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
            postInvalidate();
        } else if (listener != null) {
            //  Log.d(TAG, "autoScroll: DELAY_MOVE_TO_NEARBY_VIDEO");
            handler.sendEmptyMessageDelayed(DELAY_MOVE_TO_NEARBY_VIDEO, delayMoveToNearbyVideoMs);
        }
    }

    private synchronized void move(MotionEvent event) {
        int num = event.getPointerCount();
        if (num > 1) {
            float d = Math.abs(event.getX(1) - event.getX(0));
            distance = Math.abs(downTwoX - downOneX);
            float scale = 1 + (d - distance) / distance;
            float width = SECOND_WIDTH;
            width *= scale;
            if (width > MAX_SECOND_WIDTH) {
                width = MAX_SECOND_WIDTH;
            } else if (width < MIN_SECOND_WIDTH) {
                width = MIN_SECOND_WIDTH;
            }
            if (width != SECOND_WIDTH) {
                SECOND_WIDTH = width;
                invalidate();
            }
//            Log.d(TAG, "onTouchEvent: ACTION_MOVE 22  scale= " + scale);
            downOneX = event.getX(0);
            downOneY = event.getY(0);
            downTwoX = event.getX(1);
            downTwoY = event.getY(1);
            distance = d;
        } else {
            if (!needDelay) {
                float move = downOneX - event.getX(0);
                // Log.d(TAG, "onTouchEvent: ACTION_MOVE " + move + ">>" + SECOND_WIDTH);
                moveX(move);

            }
            downOneX = event.getX(0);
            downOneY = event.getY(0);
        }


    }

    private void moveX(float move) {
        int sec = (int) (move / SECOND_WIDTH);
        TimeAlgorithm value = mValue.addOrSub(sec);
        if (value.getTimeInMillis() < mValue.getCurrentMinTimeInMillis()) {
            if (mScroller != null) mScroller.forceFinished(true);
            value.setTimeInMillis(mValue.getCurrentMinTimeInMillis());
        } else if (value.getTimeInMillis() > mValue.getCurrentMaxTimeInMillis()) {
            if (mScroller != null) mScroller.forceFinished(true);
            value.setTimeInMillis(mValue.getCurrentMaxTimeInMillis());
        }
        if (value.getTimeInMillis() != mValue.getTimeInMillis()) {
            mValue = value;
            if (listener != null) {
                listener.onValueChanged(mValue.getStringTimeInSecond(), mValue.getTimeInMicros());
            }
            postInvalidate();
        }
    }

    private int lastX = 0;

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX()) {
                lastX = 0;
                handler.sendEmptyMessageDelayed(DELAY_MOVE_TO_NEARBY_VIDEO, delayMoveToNearbyVideoMs);
                //   Log.d(TAG, "computeScroll: end DELAY_MOVE_TO_NEARBY_VIDEO");
            } else {
                int xPosition = mScroller.getCurrX();
                //   Log.d(TAG, "computeScroll: scroll " + lastX + ">>" + xPosition + ">>" + SECOND_WIDTH);
                if ((lastX - xPosition) / SECOND_WIDTH == 0) {
                    handler.sendEmptyMessageDelayed(DELAY_MOVE_TO_NEARBY_VIDEO, delayMoveToNearbyVideoMs);
                } else {
                    moveX(lastX - xPosition);
                }
                lastX = xPosition;

            }
        } else {
            //  Log.d(TAG, "computeScrollOffset: false");
        }
    }

    private void moveToNearbyVideo() {
        if (videos == null || videos.size() == 0) {
            if (listener != null)
                listener.onNoneVideo(mValue.getStringTimeInSecond(), mValue.getTimeInMicros());
            return;
        }
        long value = 0;
        for (int i = 0; i < videos.size(); i++) {
            PlaybackVo vo = videos.get(i);
            if (vo.getStartTime() < mValue.getTimeInMicros() && vo.getEndTime() > mValue.getTimeInMicros()) {
                // Log.d(TAG, "moveToNearbyVideo: between video");
                if (listener != null)
                    listener.onVideoStart(mValue.getStringTimeInSecond(), mValue.getTimeInMicros());
                return;
            }
            if (vo.getStartTime() > mValue.getTimeInMicros() && isCurrentDay(mValue, vo.getStartTA())) {
                value = vo.getStartTime();
                break;
            }
        }
        //   Log.d(TAG, "moveToNearbyVideo: " + value);
        if (value != 0) {
            moveToValue(value);
        } else {
            if (listener != null)
                listener.onNoneVideo(mValue.getStringTimeInSecond(), mValue.getTimeInMicros());
        }
    }

    private float getMaxAbs(float moveOneX, float moveTwoX) {
        return Math.abs(moveOneX) > Math.abs(moveTwoX) ? Math.abs(moveOneX) : Math.abs(moveTwoX);
    }

    public void setOnPlaybackViewListener(PlaybackViewListener listener) {
        this.listener = listener;
        listener.onValueChanged(mValue.getStringTimeInSecond(), mValue.getTimeInMicros());
    }

    private PlaybackViewListener listener;

    public interface PlaybackViewListener {
        void onValueChanged(String timeInMillis, long timeInMicros);

        void onVideoStart(String timeInMillis, long timeInMicros);

        void onNoneVideo(String timeInMillis, long timeInMicros);
    }

    public static class TimeAlgorithm {
        private long time;//microsecond 微秒
        public static final int MODE_HOUR = 2;
        public static final int MODE_TEN_MINUTE = 1;
        public static final int MODE_TWO_MINUTE = 0;

        public TimeAlgorithm(long micros) {
            this.time = micros;
        }


        public void setTimeInMillis(long millis) {
            time = millis * 1000;
        }

        public String getStringTimeInSecond() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date(time / 1000);
            return sdf.format(date);
        }

        public String getStringTimeInMinute() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date date = new Date(time / 1000);
            return sdf.format(date);
        }

        public boolean isTwentyMinuteMultiple() {
            Date date = new Date(time / 1000);
            Calendar calendarObj = Calendar.getInstance();
            calendarObj.setTime(date);
            int m = calendarObj.get(Calendar.MINUTE);
            int s = calendarObj.get(Calendar.SECOND);
            return (m * 60 + s) % 1200 == 0;
        }


        public long getCurrentMaxTimeInMillis() {
            Date date = new Date(time / 1000);
            Calendar calendarObj = Calendar.getInstance();
            calendarObj.setTime(date);
            calendarObj.set(Calendar.HOUR_OF_DAY, 24);
            calendarObj.set(Calendar.MINUTE, 0);
            calendarObj.set(Calendar.SECOND, 0);
            calendarObj.set(Calendar.MILLISECOND, 0);
            // Log.d(TAG, "getCurrentMaxTimeInMillis: " + (calendarObj.getTimeInMillis() - 1000));
            return calendarObj.getTimeInMillis() - 1000;
        }

        public long getCurrentMinTimeInMillis() {
            Date date = new Date(time / 1000);
            Calendar calendarObj = Calendar.getInstance();
            calendarObj.setTime(date);
            calendarObj.set(Calendar.HOUR_OF_DAY, 0);
            calendarObj.set(Calendar.MINUTE, 0);
            calendarObj.set(Calendar.SECOND, 0);
            calendarObj.set(Calendar.MILLISECOND, 0);
            // Log.d(TAG, "getCurrentMinTimeInMillis: " + calendarObj.getTimeInMillis());
            return calendarObj.getTimeInMillis();
        }

        public boolean isTwoHourMultiple() {
            Date date = new Date(time / 1000);
            Calendar calendarObj = Calendar.getInstance();
            calendarObj.setTime(date);
            int h = calendarObj.get(Calendar.HOUR);
            int m = calendarObj.get(Calendar.MINUTE);
            int s = calendarObj.get(Calendar.SECOND);
            return (h * 60 * 60 + m * 60 + s) % (2 * 60 * 60) == 0;
        }


        public int getScaleMode() {
            Date date = new Date(time / 1000);
            Calendar calendarObj = Calendar.getInstance();
            calendarObj.setTime(date);
            int h = calendarObj.get(Calendar.HOUR_OF_DAY);
            int m = calendarObj.get(Calendar.MINUTE);
            int s = calendarObj.get(Calendar.SECOND);
            int mode = -1;
            if ((h * 60 * 60 + 60 * m + s) % 3600 == 0) {
                mode = MODE_HOUR;
            } else if ((60 * m + s) % 600 == 0) {
                mode = MODE_TEN_MINUTE;
            } else if ((60 * m + s) % 120 == 0) {
                mode = MODE_TWO_MINUTE;
            }
            return mode;
        }

        // 加上或减去_sec秒
        public TimeAlgorithm addOrSub(int _sec) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(time / 1000);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.SECOND, _sec);
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            TimeAlgorithm mt = new TimeAlgorithm(calendar.getTimeInMillis() * 1000);
            return mt;
        }

        public long getTimeInMicros() {
            return time;
        }

        public long getTimeInSecond() {
            return time / (1000 * 1000);
        }

        public long getTimeInMillis() {
            return time / 1000;
        }


        public int mod(int _timeInterval) {
            Date date = new Date(time / 1000);
            Calendar calendarObj = Calendar.getInstance(Locale.CHINA);
            calendarObj.setTime(date);
            int m = calendarObj.get(Calendar.MINUTE);
            int s = calendarObj.get(Calendar.SECOND);
            return (60 * m + s) % _timeInterval;
        }
    }

    public static class PlaybackVo implements Serializable {
        private long startTime;
        private long endTime;
        private long size;
        private byte type;
        private TimeAlgorithm startTA;
        private TimeAlgorithm endTA;

        public PlaybackVo(long startTime, long endTime, long size, byte type) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.size = size;
            this.type = type;
            this.startTA = new TimeAlgorithm(startTime);
            this.endTA = new TimeAlgorithm(endTime);
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public TimeAlgorithm getStartTA() {
            return startTA;
        }


        public TimeAlgorithm getEndTA() {
            return endTA;
        }

    }
}
