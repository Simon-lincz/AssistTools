package com.lcz.screenlock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	static boolean DBG = true;
	static String TAG = "ScreenLock-Utils";
	
	Context mContext;
	private DevicePolicyManager policyManager;
	private ComponentName componentName;
	AudioManager mAudioManager;
	private Resources mResources;
	boolean isActive = false;
	
	SharedPreferences mSharedPreferences;
	public static final String SharedPreferences_Name = "config";
	public static final String CONFIG_FIRST_RUN = "app_first_run";
	public static final String CONFIG_PHONE_CALL_SCREENLOCK = "lockscreen_when_makecall";
	public static final String CONFIG_PHONE_CALL_SCREENLOCK_TIMEOUT = "lockscreen_timeout_when_makecall";
	public static final String CONFIG_SHOW_NOTIFICATION_SWITCH = "lockscreen_when_makecall";
	public static final String CONFIG_SHOW_WHITE_DOT = "show_white_dot";
	
	public static final boolean CONFIG_YES = true;
	public static final boolean CONFIG_NO = false;
	public static final int CONFIG_TIMEOUT = 3000;
	
	public Utils(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		// 获取设备管理服务
		policyManager = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);

		// AdminReceiver 继承自 DeviceAdminReceiver
		componentName = new ComponentName(mContext, AdminReceiver.class);
		
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		
		mResources = mContext.getResources();
		
		checkSharePrefence();
		
	}
	
	@SuppressWarnings("deprecation")
	Notification CreateForegroundNotification(){
		return new MainNotification(mContext);
	}
	
	void checkSharePrefence(){
		mSharedPreferences = mContext.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
		if(mSharedPreferences.getBoolean(CONFIG_FIRST_RUN, CONFIG_YES)){
			Editor editor = mSharedPreferences.edit();
			editor.putBoolean(CONFIG_FIRST_RUN, CONFIG_NO);
			editor.putBoolean(CONFIG_PHONE_CALL_SCREENLOCK,CONFIG_YES);
			editor.putBoolean(CONFIG_SHOW_NOTIFICATION_SWITCH, CONFIG_YES);
			editor.putBoolean(CONFIG_SHOW_WHITE_DOT, CONFIG_YES);
			editor.putInt(CONFIG_PHONE_CALL_SCREENLOCK_TIMEOUT, CONFIG_TIMEOUT);
			editor.commit();
		}
	}
	
	public void lockScreenNow() {
		if(policyManager.isAdminActive(componentName)){
			policyManager.lockNow();
		}else{
//			activeManage();
			Toast.makeText(mContext, mResources.getString(R.string.toast_app_no_active), Toast.LENGTH_SHORT).show();
		}
	}
	
	void checkAdminActive(boolean activeNow){
		isActive = policyManager.isAdminActive(componentName);
		if(!isActive && activeNow){
			activeManage();
		}
		isActive = policyManager.isAdminActive(componentName);
	}
	
	void resetPasswd(){
		policyManager.resetPassword("1111", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
	}
	
	void deletePasswd(){
		policyManager.resetPassword("1111", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
	}
	
	public void activeManage() {
		// 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		// 权限列表
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
		// 描述(additional explanation)
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,mResources.getString(R.string.device_admin_explanation));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}
	
	public void removeManage() {
		if(policyManager != null && componentName != null){
			if(DBG)Log.d(TAG, "removeManage");
			policyManager.removeActiveAdmin(componentName);
			checkAdminActive(false);
		}
	}
	
	void setVolumeUP(){
		mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
//		mAudioManager.adjustStreamVolume(mAudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
	}
	
	void setVolumeDown(){
		mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
//		mAudioManager.adjustStreamVolume(mAudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
	}
	
	void GoHomeLauncher(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
//		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}