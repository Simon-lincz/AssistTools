package com.lcz.screenlock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Switch;

public class MainActivity extends Activity implements OnClickListener,OnCheckedChangeListener{

	static boolean DBG = true;
	static String TAG = "ScreenLock-Main";
	
	private DevicePolicyManager policyManager;
	private ComponentName componentName;
	private AppWidgetManager appWidgetManager;
	ComponentName widgetprovider;
	ImageButton imbtn_screenlock;
	Switch sw_DeviceAdminActive;
	
	boolean isActive = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 获取设备管理服务
		policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

		// AdminReceiver 继承自 DeviceAdminReceiver
		componentName = new ComponentName(this, AdminReceiver.class);
		
		appWidgetManager = AppWidgetManager.getInstance(this);
		widgetprovider = new ComponentName(this, ScreenLockWidgetProvider.class);
		
		sw_DeviceAdminActive = (Switch) findViewById(R.id.screenlock_switch_id);
		sw_DeviceAdminActive.setOnCheckedChangeListener(this);
		
		imbtn_screenlock = (ImageButton) findViewById(R.id.screenlock_btn_id);
		imbtn_screenlock.setOnClickListener(this);
		
//		checkAdminActive(true);
		
	}

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		checkAdminActive(false);
		Intent intent = getIntent();
		if(intent.getBooleanExtra(ScreenLockWidgetProvider.LOCKSCREEN_FLAG, false)){
			lockScreenNow();
		}
		
		if(DBG)Log.d(TAG, "onStart");
		super.onStart();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
		// TODO Auto-generated method stub
		switch (compoundButton.getId()) {
		case R.id.screenlock_switch_id:
			if(ischeck){
				activeManage();
			}else{
				removeManage();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.screenlock_btn_id:
			checkAdminActive(true);
			lockScreenNow();
			break;

		default:
			break;
		}
	}
	
	void refreshView(boolean working){
		sw_DeviceAdminActive.setChecked(working);
		if(working){
			imbtn_screenlock.setBackgroundResource(R.drawable.ic_lock_power_off_background);
		}else{
			imbtn_screenlock.setBackgroundResource(R.drawable.grayround);
		}
		RemoteViews views;
		if(working){
			 views = new RemoteViews(this.getPackageName(),
				R.layout.screenlock_widgetlayout);
		}else{
			 views = new RemoteViews(this.getPackageName(),
					R.layout.test_screenlock_widgetlayout);
		}
		Intent intent2 = new Intent(ScreenLockWidgetProvider.LOCKSCREEN_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);
		views.setOnClickPendingIntent(R.id.screenlock_widget_btn_id, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetManager.getAppWidgetIds(widgetprovider), views);
	}
	
	//===============================================================================================
	void checkAdminActive(boolean activeNow){
		isActive = policyManager.isAdminActive(componentName);
		if(!isActive && activeNow){
			activeManage();
		}
		refreshView(isActive);
		isActive = policyManager.isAdminActive(componentName);
	}
	
	void resetPasswd(){
		policyManager.resetPassword("1111", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
	}
	
	void deletePasswd(){
		policyManager.resetPassword("1111", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
	}
	
	private void lockScreenNow() {
		if(isActive){
			policyManager.lockNow();
			finish();
		}else{
			//checkAdminActive(true);
		}
	}

	private void activeManage() {
		if(DBG)Log.d(TAG, "activeManage");
		// 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		// 权限列表
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
		// 描述(additional explanation)
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,this.getResources().getString(R.string.device_admin_explanation));
		startActivityForResult(intent, 0);
	}
	
	private void removeManage() {
		if(policyManager != null && componentName != null){
			if(DBG)Log.d(TAG, "removeManage");
			policyManager.removeActiveAdmin(componentName);
			checkAdminActive(false);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 0:
			if(resultCode == RESULT_OK){
				refreshView(true);
			}else{
				refreshView(false);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	//===============================================================================================
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_about:
			AlertDialog dialog;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.action_about_content).setCancelable(true);
			dialog = builder.create();
			dialog.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
