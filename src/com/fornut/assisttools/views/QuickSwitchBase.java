package com.fornut.assisttools.views;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

import com.fornut.assisttools.R;

/**
 * 自定义View,背景自己画或者是设置图像
 * 
 * @author lcz
 */
public abstract class QuickSwitchBase extends Button {

    static boolean DBG = false;
    static String TAG = "AssistTools-QuickSwitchBase";

    Context mContext;
    Resources mResources;
    private Paint mPaint;
    private boolean mIsPressed = false;

    public String mQuickSwitchName;

    private String mTitle;
    private int mCustomTitleId = -1;
    private RectF mQuickSwitchIcon_Rect;
    private boolean mEnableCustomIcon = false;
    private Bitmap mCustomIconBitmap;
    private Rect mCustomIconBitmap_Rect;

    public int mAlphaPressed = 0, mAlphaNormal = 0;
    private int mWidth = 0, mHeight = 0, mIconTitleGap = 0, mTitleSize = 0,
            mTitlePadding = 0;

    private boolean mIsPointMove = false, mIsActionConsumed = true;
    private int mInitX = 0, mInitY = 0;
    private long mInitTime = 0;
    private int mTouchSlop = 0;

    private static final int MSG_BASE = 0;
    private static final int MSG_CLICK_CHECK = MSG_BASE + 1;
    private static final int MSG_LONGCLICK_CHECK = MSG_BASE + 2;

    MyHandler mHandler = new MyHandler(this);

    static class MyHandler extends Handler {
        SoftReference<QuickSwitchBase> mQuickSwitchBase;

        MyHandler(QuickSwitchBase quickSwitchBase) {
            mQuickSwitchBase = new SoftReference<QuickSwitchBase>(
                    quickSwitchBase);
        }

        @Override
        public void handleMessage(Message msg) {
            QuickSwitchBase quickSwitchBase = mQuickSwitchBase.get();
            switch (msg.what) {
            case MSG_CLICK_CHECK:
                quickSwitchBase.mIsActionConsumed = true;
                quickSwitchBase.performClick();
                quickSwitchBase.onClickCallback();
                Log.d(TAG, "onClick");
                removeMessages(MSG_LONGCLICK_CHECK);
                break;
            case MSG_LONGCLICK_CHECK:
                quickSwitchBase.mIsActionConsumed = true;
                quickSwitchBase.performLongClick();
                quickSwitchBase.onLongClickCallback();
                Log.d(TAG, "onLongClick");
                break;
            default:
                break;
            }
        }
    };

    public QuickSwitchBase(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        mResources = getResources();
        mPaint = new Paint();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() / 2;
        setBackgroundDrawable(null);
        mTitle = getResources().getString(R.string.app_name);
        refreshLayoutParams();
        mQuickSwitchName = getQuickSwitchName();
    }

    void refreshLayoutParams() {
        mWidth = mResources
                .getDimensionPixelSize(R.dimen.quickswitch_item_width);
        mHeight = mResources
                .getDimensionPixelSize(R.dimen.quickswitch_item_height);
        mIconTitleGap = mResources
                .getDimensionPixelSize(R.dimen.quickswitch_item_icon_title_gap);
        mTitleSize = mResources
                .getDimensionPixelSize(R.dimen.quickswitch_item_titlesize);
        mTitlePadding = mResources
                .getDimensionPixelSize(R.dimen.quickswitch_item_titlepadding);
        mAlphaNormal = mResources
                .getInteger(R.integer.quickswitch_item_alpha_normal);
        mAlphaPressed = mResources
                .getInteger(R.integer.quickswitch_item_alpha_pressed);
        int icon_width = mHeight - mTitleSize - mIconTitleGap;
        mQuickSwitchIcon_Rect = new RectF((mWidth - icon_width) / 2, 0, mWidth
                - (mWidth - icon_width) / 2, icon_width);
    }

    void setQuickSwitchCustomIcon(int id) {
        setQuickSwitchCustomIcon(BitmapFactory.decodeResource(getResources(),
                id));
    }

    void setQuickSwitchCustomIcon(Bitmap bitmap) {
        if (bitmap == null) {
            mEnableCustomIcon = false;
            mCustomIconBitmap = null;
            return;
        }
        mEnableCustomIcon = true;
        // 需要判断并在必要时进行裁剪
        mCustomIconBitmap = bitmap;
        mCustomIconBitmap_Rect = new Rect(0, 0, mCustomIconBitmap.getWidth(),
                mCustomIconBitmap.getHeight());
        invalidate();
    }

    void setQuickSwitchcustomTitle(int id) {
        mCustomTitleId = id;
        setQuickSwitchcustomTitle(getResources().getString(id));
    }

    void setQuickSwitchcustomTitle(String title) {
        if (title == null) {
            mCustomTitleId = -1;
            return;
        }
        mTitle = title;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        Log.d(TAG, "onTouchEvent action: " + action);
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            mInitX = (int) event.getX();
            mInitY = (int) event.getY();
            mInitTime = System.currentTimeMillis();
            mIsPressed = true;
            mIsPointMove = false;
            mIsActionConsumed = false;
            mHandler.sendEmptyMessageDelayed(MSG_LONGCLICK_CHECK,
                    ViewConfiguration.getLongPressTimeout());
            invalidate();
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_OUTSIDE:
            mIsPressed = false;
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            mIsPressed = false;
            if (!mIsPointMove && !mIsActionConsumed) {
                mHandler.sendEmptyMessage(MSG_CLICK_CHECK);
            }
            if (System.currentTimeMillis() - mInitTime < ViewConfiguration
                    .getLongPressTimeout()) {
                Log.d(TAG, "timeout");
                mHandler.removeMessages(MSG_LONGCLICK_CHECK);
            }
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            if (!checkPointPosition(event.getX(), event.getY(), mTouchSlop)) {
                mHandler.removeMessages(MSG_LONGCLICK_CHECK);
                Log.d(TAG, "point had move");
                mIsPointMove = true;
            }
            break;
        default:
            break;
        }
        return super.onTouchEvent(event);
    }

    public boolean checkPointPosition(float localX, float localY, float slop) {
        return (Math.abs(localX - mInitX) < slop)
                && (Math.abs(localY - mInitY) < slop);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Log.d(TAG, "onDraw mEnableCustomIcon " + mEnableCustomIcon);

        drawTitle(canvas);

        mPaint.setAntiAlias(true);
        if (mEnableCustomIcon && mCustomIconBitmap != null) {
            if (mIsPressed) {
                mPaint.setAlpha(mAlphaPressed);
            } else {
                mPaint.setAlpha(mAlphaNormal);
            }
            canvas.drawBitmap(mCustomIconBitmap, mCustomIconBitmap_Rect,
                    mQuickSwitchIcon_Rect, mPaint);
            return;
        }
        drawQuickSwitchIcon(canvas, mQuickSwitchIcon_Rect, mPaint, mIsPressed);
    }

    /**
     * 画文字的时候，要注意y是指文字的中间位置
     * 
     * @param canvas
     */
    void drawTitle(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mTitleSize);
        float titleWidth = mPaint.measureText(mTitle);
        if (mWidth > titleWidth + 2 * mTitlePadding) {
            canvas.drawText(mTitle, 0, mTitle.length(),
                    (mWidth - titleWidth) / 2, mHeight - mTitleSize / 2, mPaint);
        } else {
            float perCharWidth = titleWidth / mTitle.length();
            int maxStringvisiable = (int) Math
                    .floor((mWidth - 2 * mTitlePadding) / perCharWidth);
            maxStringvisiable--;// for show "."
            StringBuilder tmp = new StringBuilder();
            tmp.append(mTitle.substring(0, maxStringvisiable));
            tmp.append(".");
            float tmpWidth = mPaint.measureText(tmp.toString());
            canvas.drawText(tmp, 0, tmp.length(), (mWidth - tmpWidth) / 2,
                    mHeight - mTitleSize / 2, mPaint);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        refreshLayoutParams();
        if (mCustomTitleId != -1) {
            setQuickSwitchcustomTitle(mCustomTitleId);
        }
        invalidate();
    }

    /**
     * 自己重画ICON，必须注意要实现按下的效果，还有上下最好各留空20%为了美观 icon要水平居中
     * 
     * @param canvas
     * @param rectF
     * @param paint
     * @param isPressed
     */
    public abstract void drawQuickSwitchIcon(Canvas canvas, RectF rectF,
            Paint paint, boolean isPressed);

    public abstract void onClickCallback();

    public abstract void onLongClickCallback();

    public abstract String getQuickSwitchName();

    public abstract boolean isQuickSwitchSupported();
}