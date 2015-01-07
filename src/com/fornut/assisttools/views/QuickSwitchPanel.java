package com.fornut.assisttools.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.GridView;

public class QuickSwitchPanel extends GridView {

    private static boolean DBG = false;
    private static String TAG = "AssistTools-QuickSwitchPanel";

    CatchKeyListener mCatchKeyListener;

    public QuickSwitchPanel(Context context) {
        this(context, null, 0);
        // TODO Auto-generated constructor stub
    }

    public QuickSwitchPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public QuickSwitchPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (mCatchKeyListener != null) {
            mCatchKeyListener.catchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setCatchKeyListener(CatchKeyListener l) {
        mCatchKeyListener = l;
    }

    public interface CatchKeyListener {
        public void catchKeyEvent(KeyEvent event);
    }
}
