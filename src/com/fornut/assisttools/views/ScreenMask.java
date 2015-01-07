package com.fornut.assisttools.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.fornut.assisttools.views.QSScreenMask.ScreenMaskListener;

public class ScreenMask extends View implements ScreenMaskListener {

    private WindowManager mWindowManager;
    private int mScreenWidth = 0, mScreenHeight = 0;
    private Paint mPaint;
    private int mAlpha = 100, mRed = 0, mGreen = 10, mBlue = 0;
    private QSScreenMask mQsScreenMask;

    public ScreenMask(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mPaint = new Paint();
        refreshLayoutParams();
    }

    void refreshLayoutParams() {
        Point outSize = new Point();
        mWindowManager.getDefaultDisplay().getSize(outSize);
        mScreenWidth = outSize.x;
        mScreenHeight = outSize.y;
    }

    public void setQSScreenMask(QSScreenMask qsScreenMask) {
        if (mQsScreenMask == null) {
            mQsScreenMask = qsScreenMask;
            mQsScreenMask.setScreenMaskListener(this);
        }
    }

    public void setColor(int alpha, int red, int green, int blue) {
        mAlpha = alpha;
        mRed = red;
        mGreen = green;
        mBlue = blue;
        if (getVisibility() == View.VISIBLE) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        mPaint.setColor(Color.argb(mAlpha, mRed, mGreen, mBlue));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, mPaint);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        refreshLayoutParams();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return super.onTouchEvent(event);
    }

    @Override
    public void switchStatus() {
        // TODO Auto-generated method stub
        if (getVisibility() == View.VISIBLE) {
            setVisibility(View.GONE);
            mQsScreenMask.setScreenMaskEnable(false);
        } else {
            setVisibility(View.VISIBLE);
            mQsScreenMask.setScreenMaskEnable(true);
        }
    }
}
