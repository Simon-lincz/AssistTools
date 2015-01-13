package com.fornut.assisttools.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.fornut.assisttools.R;

public class ProgressPanel extends LinearLayout {

    int mPanelWidth = 0, mPanelHeight = 0, mProgressBarWidth = 0,
            mProgressBarHeight = 0;

    public ProgressPanel(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initProgressBars();
    }

    void initProgressBars() {
        View v = inflate(getContext(), R.layout.progressbar, null);
        LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(getResources()
                    .getDimensionPixelSize(R.dimen.progressbar_width),
                    getResources().getDimensionPixelSize(
                            R.dimen.progressbar_height));
        }
        addView(v, layoutParams);
    }

    @Override
    public void addView(View child) {
        // TODO Auto-generated method stub
        super.addView(child);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        for (int index = 0; index < getChildCount(); index++) {
            LayoutParams params = (LayoutParams) getChildAt(index)
                    .getLayoutParams();
            if (params == null) {
                params = new LayoutParams(getResources().getDimensionPixelSize(
                        R.dimen.progressbar_width), getResources()
                        .getDimensionPixelSize(R.dimen.progressbar_height));
            }
            params.weight = getResources().getDimensionPixelSize(
                    R.dimen.progressbar_width);
            params.weight = getResources().getDimensionPixelSize(
                    R.dimen.progressbar_height);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        // 这边必须要调用，否则就要自己实现，就是自己重写，可查看LinearLayout的方法
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawARGB(55, 255, 155, 55);
    }
}