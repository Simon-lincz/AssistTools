package com.fornut.assisttools.models;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fornut.assisttools.R;
import com.fornut.assisttools.misc.QuickSwitchesUtils;
import com.fornut.assisttools.misc.SharedPreferenceUtils;
import com.fornut.assisttools.misc.Utils;
import com.fornut.assisttools.models.SpeicalKeyListener.OnSpecialKeyListener;
import com.fornut.assisttools.views.ProgressPanel;
import com.fornut.assisttools.views.QSEmpty;
import com.fornut.assisttools.views.QSScreenMask;
import com.fornut.assisttools.views.QuickSwitchBase;
import com.fornut.assisttools.views.QuickSwitchPanel;
import com.fornut.assisttools.views.QuickSwitchPanel.CatchKeyListener;
import com.fornut.assisttools.views.ScreenMask;
import com.fornut.assisttools.views.WhiteDot;

/**
 * 悬浮view的创建、控制及动画
 * 
 * @author lcz
 */
public class DropzoneManager implements CatchKeyListener, OnSpecialKeyListener {

    private static boolean DBG = false;
    private static String TAG = "AssistTools-DropzoneManager";

    private boolean mIsWhiteDotAdded = false; // 是否已增加悬浮按钮
    private WhiteDot mWhiteDot;
    private LayoutParams mWhiteDot_Params;
    private int mWhiteDotClickCounter = 0;
    public final static String POSITION_SPLIT = ",";

    private boolean mIsControlBoardAdded = false; // 控制板
    private View mControlBoard;
    private LayoutParams mControlBoard_Params;

    private boolean mAreQuickSwitchesAdded = false;
    private QuickSwitchPanel mQuickSwitchPanel;
    private QuickSwitchesAdapter mQuickSwitchesAdapter;
    private HashMap<String, QuickSwitchBase> mAllQuickSwitches;

    private boolean mIsScreenMaskAdded = false;  // 屏幕滤镜
    private ScreenMask mScreenMask;

    private boolean mIsProgressPanelAdded = false;  // 进度条控制板
    private ProgressPanel mProgressPanel;
    private LayoutParams mProgressPanel_Params;

    private Context mContext;
    private WindowManager mWindowManager;
    private LayoutInflater mLayoutInflater;
    private int mScreenWidth = 0, mScreenHeight = 0;

    private SpeicalKeyListener mSpeicalKeyListener;
    public static final int SKL_STOPWATCH_TIMEOUT = 3000;

    private static final int MSG_BASE = 0;
    private static final int MSG_CREATE_WHITEDOT = MSG_BASE + 1;
    private static final int MSG_CREATE_CONTROLBOARD = MSG_BASE + 2;
    private static final int MSG_INIT = MSG_BASE + 3;
    private static final int MSG_SHOW_WHITEDOT = MSG_BASE + 4;
    private static final int MSG_HIDE_WHITEDOT = MSG_BASE + 5;
    private static final int MSG_SPEICALKEYLISTENER_STARTWATCH = MSG_BASE + 6;
    private static final int MSG_SPEICALKEYLISTENER_STOPWATCH = MSG_BASE + 7;

    MyHandler mHandler = new MyHandler(this);

    static class MyHandler extends Handler {
        WeakReference<DropzoneManager> mDropzoneManager;

        MyHandler(DropzoneManager dropzoneManager) {
            mDropzoneManager = new WeakReference<DropzoneManager>(
                    dropzoneManager);
        }

        @Override
        public void handleMessage(Message msg) {
            DropzoneManager dropzoneManager = mDropzoneManager.get();
            switch (msg.what) {
            case MSG_CREATE_WHITEDOT:
                dropzoneManager.createWhiteDot(dropzoneManager.mContext);
                sendEmptyMessage(MSG_INIT);
                break;
            case MSG_CREATE_CONTROLBOARD:
                dropzoneManager.createContolBoard(dropzoneManager.mContext);
                dropzoneManager.initQuickSwitches();
                dropzoneManager.createScreenMask(dropzoneManager.mContext);
                dropzoneManager.createProgressPanel(dropzoneManager.mContext);
                sendEmptyMessage(MSG_INIT);
                break;
            case MSG_INIT:
                if (!dropzoneManager.mIsWhiteDotAdded) {
                    sendEmptyMessage(MSG_CREATE_WHITEDOT);
                }
                if (!dropzoneManager.mIsControlBoardAdded) {
                    sendEmptyMessage(MSG_CREATE_CONTROLBOARD);
                }
                if (dropzoneManager.mIsWhiteDotAdded
                        && dropzoneManager.mIsControlBoardAdded) {
                    dropzoneManager.mControlBoard.setVisibility(View.GONE);
                }
                break;
            case MSG_SHOW_WHITEDOT:
                sendEmptyMessageDelayed(MSG_SPEICALKEYLISTENER_STOPWATCH,
                        SKL_STOPWATCH_TIMEOUT);
                dropzoneManager.mWhiteDot.setVisibility(View.VISIBLE);
                dropzoneManager.mControlBoard.setVisibility(View.GONE);
                break;
            case MSG_HIDE_WHITEDOT:
                sendEmptyMessage(MSG_SPEICALKEYLISTENER_STARTWATCH);
                dropzoneManager.mWhiteDot.setVisibility(View.GONE);
                dropzoneManager.mControlBoard.setVisibility(View.VISIBLE);
                break;
            case MSG_SPEICALKEYLISTENER_STARTWATCH:
                removeMessages(MSG_SPEICALKEYLISTENER_STOPWATCH);
                Log.d(TAG, "startWatch");
                dropzoneManager.mSpeicalKeyListener.startWatch();
                break;
            case MSG_SPEICALKEYLISTENER_STOPWATCH:
                dropzoneManager.mSpeicalKeyListener.stopWatch();
                break;
            default:
                break;
            }
        }
    };

    private static DropzoneManager sInstance;

    public static DropzoneManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DropzoneManager(context);
        }
        return sInstance;
    }

    public DropzoneManager(Context context) {
        // TODO Auto-generated constructor stub
        sInstance = this;
        mContext = context;
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSpeicalKeyListener = new SpeicalKeyListener(mContext);
        mSpeicalKeyListener.setOnHomePressedListener(this);
        mHandler.sendEmptyMessage(MSG_INIT);
        refreshResources();
    }

    public void onConfigurationChanged(Configuration config) {
        refreshResources();
    }

    void refreshResources() {
        Point outSize = new Point();
        mWindowManager.getDefaultDisplay().getSize(outSize);
        mScreenWidth = outSize.x;
        mScreenHeight = outSize.y;
        Log.d(TAG, "screen W x H: " + mScreenWidth + " x " + mScreenHeight);
    }

    /**
     * 创建悬浮按钮
     */
    private void createWhiteDot(Context context) {
        if (mIsWhiteDotAdded) {
            return;
        }
        mWhiteDot = new WhiteDot(context);
        mWhiteDot_Params = new WindowManager.LayoutParams();
        // mWhiteDot_Params = new WindowManager.LayoutParams(-1, -1,
        // _type, _flags, _format)
        // 设置window type
        mWhiteDot_Params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
         * 即拉下通知栏不可见
         */

        mWhiteDot_Params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        // 透传 LayoutParams.FLAG_NOT_TOUCH_MODAL |
        // LayoutParams.FLAG_NOT_FOCUSABLE;
        mWhiteDot_Params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        int w = context.getResources().getDimensionPixelSize(
                R.dimen.whitedot_width);
        int h = context.getResources().getDimensionPixelSize(
                R.dimen.whitedot_height);

        // 设置悬浮按钮的长得宽
        mWhiteDot_Params.width = w;
        mWhiteDot_Params.height = h;
        // 通知view组件重绘
        mWhiteDot.invalidate();

        // 设置悬浮按钮的Touch监听
        mWhiteDot.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mWhiteDotClickCounter = 0;
                    if (DBG)
                        Log.d(TAG, "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateWhiteDotPosition((int) event.getRawX() - mScreenWidth
                            / 2, (int) event.getRawY() - mScreenHeight / 2);
                    mWhiteDotClickCounter++;
                    if (DBG)
                        Log.d(TAG, "ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (DBG)
                        Log.d(TAG, "ACTION_UP");
                    autoMoveWhiteDot((int) event.getRawX(),
                            (int) event.getRawY());
                    break;
                }
                return false;// true 其他的动作都捕捉不到，比如Click，longClick等等
            }
        });

        /*
         * OnClickListener只发生在up之后
         */
        mWhiteDot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (DBG)
                    Log.d(TAG, "onClick");
                if (mWhiteDotClickCounter < 5) {
                    mHandler.sendEmptyMessage(MSG_HIDE_WHITEDOT);
                }
            }
        });

        /*
         * OnLongClickListene可能发生在down-move LongClick move-up
         */
        mWhiteDot.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                if (DBG)
                    Log.d(TAG, "onLongClick");
                return false;
            }
        });
        mWindowManager.addView(mWhiteDot, mWhiteDot_Params);
        String posi = SharedPreferenceUtils.getInstance(mContext)
                .getWhiteDotPosition();
        mIsWhiteDotAdded = true;
        if (posi != null) {
            String[] xy = posi.split(POSITION_SPLIT);
            Log.d(TAG, "posi " + posi + " x " + xy[0] + " y " + xy[1]);
            updateWhiteDotPosition(Integer.parseInt(xy[0]),
                    Integer.parseInt(xy[1]));
        }
    }

    /**
     * 更新悬浮按钮位置
     * 
     * @param x
     * @param y
     */
    void updateWhiteDotPosition(int x, int y) {
        if (mIsWhiteDotAdded) {
            mWhiteDot_Params.x = x;
            mWhiteDot_Params.y = y;
            Log.d(TAG, "updateWhiteDotPosition x " + x + " y " + y);
            mWindowManager.updateViewLayout(mWhiteDot, mWhiteDot_Params);
        }
    }

    void autoMoveWhiteDot(int x, int y) {
        int delta_x = Math.min(Math.abs(x - mScreenWidth), x);
        int delta_y = Math.min(Math.abs(y - mScreenHeight), y);
        int targetX = 0, targetY = 0;
        if (delta_x <= delta_y) {
            targetY = y - mScreenHeight / 2;// whitedot's origin position is in
                                            // center of screen
            if (x <= mScreenWidth / 2) {
                targetX = -1 * mScreenWidth / 2;
            } else {
                targetX = mScreenWidth / 2;
            }
        } else {
            targetX = x - mScreenWidth / 2; // whitedot's origin position is in
                                            // center of screen
            if (y <= mScreenHeight / 2) {
                targetY = -1 * mScreenHeight / 2;
            } else {
                targetY = mScreenHeight / 2;
            }
        }
        Log.d(TAG, "autoMoveWhiteDot x " + x + " y " + y + " dx " + delta_x
                + " dy " + delta_y + " tx " + targetX + " ty " + targetY);
        updateWhiteDotPosition(targetX, targetY);
        SharedPreferenceUtils.getInstance(mContext).saveWhiteDotPosition(
                targetX + POSITION_SPLIT + targetY);
        mWhiteDot.showMoveAnimation(x - mScreenWidth / 2,
                y - mScreenHeight / 2, targetX, targetY);
    }

    /**
     * 创建控制盘
     */
    private void createContolBoard(Context context) {

        if (mIsControlBoardAdded) {
            return;
        }
        mControlBoard = mLayoutInflater.inflate(R.layout.control_board, null);

        mQuickSwitchPanel = (QuickSwitchPanel) mControlBoard
                .findViewById(R.id.quickswitch_panel);

        mQuickSwitchPanel.setCatchKeyListener(this);

        mControlBoard_Params = new WindowManager.LayoutParams();
        // 设置window type
        mControlBoard_Params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
         * 即拉下通知栏不可见
         */

        mControlBoard_Params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        mControlBoard_Params.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        int w = context.getResources().getDimensionPixelSize(
                R.dimen.controlboard_width);
        int h = context.getResources().getDimensionPixelSize(
                R.dimen.controlboard_height);

        // 设置悬浮按钮的长得宽
        mControlBoard_Params.width = w;
        mControlBoard_Params.height = h;

        mControlBoard.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onTouch event " + event.getAction());
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE
                        || event.getAction() == MotionEvent.ACTION_DOWN) {
                    showWhiteDot();
                }
                return false;
            }
        });

        mWindowManager.addView(mControlBoard, mControlBoard_Params);
        mIsControlBoardAdded = true;
    }

    void initQuickSwitches() {
        mAreQuickSwitchesAdded = false;
        mAllQuickSwitches = QuickSwitchesUtils.loadAllQuickSwitches(mContext);
        mQuickSwitchesAdapter = new QuickSwitchesAdapter(mContext,
                mAllQuickSwitches);
        mQuickSwitchPanel.setAdapter(mQuickSwitchesAdapter);
        mAreQuickSwitchesAdded = true;
    }

    class QuickSwitchesAdapter extends BaseAdapter {

        Context mContext;
        HashMap<String, QuickSwitchBase> mAllQuickSwitches;
        ArrayList<String> mDisplaySwitchList;

        public QuickSwitchesAdapter(Context context,
                HashMap<String, QuickSwitchBase> quickSwitches) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mAllQuickSwitches = quickSwitches;
            mDisplaySwitchList = QuickSwitchesUtils
                    .getDisplaySwitchList(mContext);
        }

        public void refreshDisplaySwitch() {
            mDisplaySwitchList = QuickSwitchesUtils
                    .getDisplaySwitchList(mContext);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mDisplaySwitchList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            QuickSwitchBase switchBase = mAllQuickSwitches
                    .get(mDisplaySwitchList.get(position));
            if (position == 0 && switchBase instanceof QSEmpty) {
                // add a new empty switch for measure the gridview
                QuickSwitchBase newQSEmpty = new QSEmpty(mContext);
                Log.d(TAG, "");
                return newQSEmpty;
            }
            if (switchBase != null) {
                return switchBase;
            }
            return mAllQuickSwitches.get("QSEmpty");
        }
    }

    void createScreenMask(Context context) {
        mIsScreenMaskAdded = false;
        mScreenMask = new ScreenMask(context);
        LayoutParams layoutParams = new WindowManager.LayoutParams();
        // 设置window type
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        layoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        mScreenMask.setColor(150, 0, 0, 0);
        mScreenMask.invalidate();
        mWindowManager.addView(mScreenMask, layoutParams);
        mIsScreenMaskAdded = true;
        if (mAreQuickSwitchesAdded) {
            QSScreenMask qsScreenMask = (QSScreenMask) mAllQuickSwitches
                    .get(QSScreenMask.class.getSimpleName());
            if (qsScreenMask != null) {
                mScreenMask.setQSScreenMask(qsScreenMask);
            }
            mScreenMask.setVisibility(View.GONE);
        }
    }

    void createProgressPanel(Context context) {
        mIsProgressPanelAdded = false;
        mProgressPanel = new ProgressPanel(context);
        mProgressPanel_Params = new WindowManager.LayoutParams();
        // 设置window type
        mProgressPanel_Params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mProgressPanel_Params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        mProgressPanel_Params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mProgressPanel_Params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mProgressPanel_Params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mProgressPanel_Params.gravity = Gravity.TOP;
        mWindowManager.addView(mProgressPanel, mProgressPanel_Params);
        // 设置progresspanel的位置
        int controlboard_h = context.getResources().getDimensionPixelSize(
                R.dimen.controlboard_height);
        int h = context.getResources().getDimensionPixelSize(
                R.dimen.progressbar_height);
        int gap = context.getResources().getDimensionPixelSize(
                R.dimen.controlboard_progresspanel_gap);
        mProgressPanel_Params.y = (mScreenHeight - Utils.getStatusbarHeight(context)) / 2
                - gap - controlboard_h / 2 - h;
        mWindowManager.updateViewLayout(mProgressPanel, mProgressPanel_Params);
        mIsProgressPanelAdded = true;
    }

    public void showWhiteDot() {
        mHandler.sendEmptyMessage(MSG_SHOW_WHITEDOT);
    }

    @Override
    public void catchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        int keycode = event.getKeyCode();
        Log.d(TAG, "catchKeyEvent " + event.getKeyCode());
        if (keycode == KeyEvent.KEYCODE_BACK
                || keycode == KeyEvent.KEYCODE_MENU) {
            showWhiteDot();
        }
    }

    @Override
    public void onHomePressed() {
        // TODO Auto-generated method stub
        showWhiteDot();
    }

    @Override
    public void onToggleRecentApp() {
        // TODO Auto-generated method stub
        showWhiteDot();
    }

    @Override
    public void onHomeLongPressed() {
        // TODO Auto-generated method stub
        showWhiteDot();
    }
}
