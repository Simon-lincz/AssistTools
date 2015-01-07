package com.fornut.assisttools.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Home键监听封装
 */
public class SpeicalKeyListener {

    static final String TAG = "AssistTools-SpeicalKeyListener";
    private Context mContext;
    private IntentFilter mFilter;
    private OnSpecialKeyListener mListener;
    private InnerRecevier mRecevier;
    private boolean mIsWatching = false;

    // 回调接口
    public interface OnSpecialKeyListener {
        public void onHomePressed();

        public void onToggleRecentApp();

        public void onHomeLongPressed();
    }

    public SpeicalKeyListener(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    /**
     * 设置监听
     * 
     * @param listener
     */
    public void setOnHomePressedListener(OnSpecialKeyListener listener) {
        mListener = listener;
        mRecevier = new InnerRecevier();
    }

    /**
     * 开始监听，注册广播
     */
    public void startWatch() {
        Log.d(TAG, "startWatch " + (mRecevier != null) + " mIsWatching "
                + mIsWatching);
        if (mRecevier != null && !mIsWatching) {
            mIsWatching = true;
            mContext.registerReceiver(mRecevier, mFilter);
        }
    }

    /**
     * 停止监听，注销广播
     */
    public void stopWatch() {
        Log.d(TAG, "stopWatch " + (mRecevier != null) + " mIsWatching "
                + mIsWatching);
        if (mRecevier != null && mIsWatching) {
            mIsWatching = false;
            mContext.unregisterReceiver(mRecevier);
        }
    }

    /**
     * 广播接收者
     */
    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    Log.e(TAG, "onReceive action:" + action + ",reason: "
                            + reason);
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            // 短按home键
                            mListener.onHomePressed();
                        } else if (reason
                                .equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            // 长按home键
                            mListener.onHomeLongPressed();
                        } else if (reason
                                .equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                            // 触发多任务
                            mListener.onToggleRecentApp();
                        }
                    }
                }
            }
        }
    }
}