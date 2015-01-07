package com.fornut.assisttools.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.fornut.assisttools.MainActivity;
import com.fornut.assisttools.R;
import com.fornut.assisttools.R.id;
import com.fornut.assisttools.R.layout;
import com.fornut.assisttools.models.DevicePolicyManagerUtils;
import com.fornut.assisttools.views.residemenu.ResideMenu;

public class HomeFragment extends Fragment implements OnClickListener,
        OnCheckedChangeListener {

    static boolean DBG = false;
    static String TAG = "AssistTools-HomeFragment";

    private static final int ACTIVE_DEVICEPOLICYMANAGER_REQUESTCODE = 1;

    ImageButton mImbtn_screenlock;
    ToggleButton mTb_DeviceAdminActive;

    private View parentView;

    // 延时检测，不然启动activity的时候，会闪
    private static final int CHECKADMINACTIVE_TIMEOUT = 200;

    private static final int MSG_BASE = -1;
    private static final int MSG_CHECKADMINACTIVE = MSG_BASE + 1;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_CHECKADMINACTIVE:
                refreshView(DevicePolicyManagerUtils.getInstance(
                        getActivity().getApplicationContext())
                        .checkAdminActive());
                break;

            default:
                break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home_fragment, container, false);
        init();
        return parentView;
    }

    private void init() {
        mImbtn_screenlock = (ImageButton) parentView
                .findViewById(R.id.screenlock_btn_id);
        mImbtn_screenlock.setOnClickListener(this);

        mTb_DeviceAdminActive = (ToggleButton) parentView
                .findViewById(R.id.screenlock_togglebtn_id);
        mTb_DeviceAdminActive.setOnCheckedChangeListener(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        mHandler.sendEmptyMessageDelayed(MSG_CHECKADMINACTIVE,
                CHECKADMINACTIVE_TIMEOUT);
        super.onResume();
    }

    void refreshView(boolean working) {
        // sw_DeviceAdminActive.setChecked(working);
        mTb_DeviceAdminActive.setChecked(working);
        if (working) {
            mImbtn_screenlock
                    .setBackgroundResource(R.drawable.ic_lock_power_off_background);
        } else {
            mImbtn_screenlock.setBackgroundResource(R.drawable.grayround);
        }
        Intent refreshwidget_intent = new Intent(
                ScreenLockWidgetProvider.REFRESHWIDGET_ACTION);
        refreshwidget_intent.putExtra(
                ScreenLockWidgetProvider.REFRESHWIDGET_FLAG,
                working ? ScreenLockWidgetProvider.WIDGET_BTN_ENABLE
                        : ScreenLockWidgetProvider.WIDGET_BTN_DISABLE);
        getActivity().sendBroadcast(refreshwidget_intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        switch (buttonView.getId()) {
        case R.id.screenlock_togglebtn_id:
            if (isChecked) {
                DevicePolicyManagerUtils.getInstance(getActivity())
                        .activeManage(getActivity(),
                                ACTIVE_DEVICEPOLICYMANAGER_REQUESTCODE);
            } else {
                DevicePolicyManagerUtils.getInstance(
                        getActivity().getApplicationContext()).removeManage();
                refreshView(false);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.screenlock_btn_id:
            if (DevicePolicyManagerUtils.getInstance(
                    getActivity().getApplicationContext()).checkAdminActive()) {
                DevicePolicyManagerUtils.getInstance(
                        getActivity().getApplicationContext()).lockScreenNow();
            } else {
                DevicePolicyManagerUtils.getInstance(
                        getActivity().getApplicationContext()).activeManage(
                        getActivity(), ACTIVE_DEVICEPOLICYMANAGER_REQUESTCODE);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
        case ACTIVE_DEVICEPOLICYMANAGER_REQUESTCODE:
            if (resultCode == Activity.RESULT_OK) {
                refreshView(true);
            } else {
                refreshView(false);
            }
            break;

        default:
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
