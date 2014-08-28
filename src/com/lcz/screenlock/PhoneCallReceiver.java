package com.lcz.screenlock;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneCallReceiver extends BroadcastReceiver {

	static boolean DBG = true;
	static String TAG = "ScreenLock-PhoneCallReceiver";
	
	Context mContext;
	
	// 获取电话管理服务
	TelephonyManager mTelephonyManager;
	
	// 获取设备管理服务
	DevicePolicyManager mDevicePolicyManager;
	// AdminReceiver 继承自 DeviceAdminReceiver
	ComponentName mComponentName;
	
	SharedPreferences mSharedPreferences;
	
	static final int MSG_BASE = 0;
	static final int MSG_LOCKNOW = MSG_BASE + 1;
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_LOCKNOW:
				lockScreenNow(mContext);
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(DBG)Log.d(TAG, "action:" + intent.getAction());
		
		if(mContext == null){
			mContext = context;
		}
		
		// 获取电话管理服务
		if(mTelephonyManager == null){
			mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		}
		
		// 获取设备管理服务
		if(mDevicePolicyManager == null){
			mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		}

		// AdminReceiver 继承自 DeviceAdminReceiver
		if(mComponentName == null){
			mComponentName = new ComponentName(context, AdminReceiver.class);
		}
		
		// AdminReceiver 继承自 DeviceAdminReceiver
		if(mSharedPreferences == null){
			mSharedPreferences = context.getSharedPreferences(Utils.SharedPreferences_Name, Context.MODE_PRIVATE);
		}
		
		if(DBG)Log.d(TAG, "mTelephonyManager.getCallState:" + mTelephonyManager.getCallState());
		switch (mTelephonyManager.getCallState()) {
		case TelephonyManager.CALL_STATE_RINGING:

			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			if(mSharedPreferences.getBoolean(Utils.CONFIG_PHONE_CALL_SCREENLOCK, false))mHandler.sendEmptyMessageDelayed(MSG_LOCKNOW, 3000);
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			
			break;
		default:
			break;
		}
	}

	private void lockScreenNow(Context context) {
		if(context == null) 
			return;
		
		// 获取设备管理服务
		boolean isActive = mDevicePolicyManager.isAdminActive(mComponentName);
		if(isActive){
			mDevicePolicyManager.lockNow();
		}else{
			Toast.makeText(context, context.getResources().getString(R.string.toast_app_no_active), Toast.LENGTH_LONG).show();
		}
	}
	
	
//	MyPhoneStateListener phoneListener = new MyPhoneStateListener();
//	telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
	
//	class MyPhoneStateListener extends PhoneStateListener {
//		public void onCallStateChanged(int state, String incomingNumber) {
//			switch (state) {
//			case TelephonyManager.CALL_STATE_IDLE://空闲
//				Log.d(TAG, "IDLE");
//				break;
//			case TelephonyManager.CALL_STATE_OFFHOOK://摘机
//				Log.d(TAG, "OFFHOOK");
//				break;
//			case TelephonyManager.CALL_STATE_RINGING:
//				Log.d(TAG, "RINGING");
//				break;
//			}
//		}
//	}
}
