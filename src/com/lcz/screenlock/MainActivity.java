package com.lcz.screenlock;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener,OnCheckedChangeListener{

	static boolean DBG = false;
	static String TAG = "ScreenLock-Main";
	
	private DevicePolicyManager policyManager;
	private ComponentName componentName;
	ImageButton imbtn_screenlock;
	ToggleButton tb_DeviceAdminActive;
//	Switch sw_DeviceAdminActive;
	
	
	boolean isActive = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 获取设备管理服务
		policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

		// AdminReceiver 继承自 DeviceAdminReceiver
		componentName = new ComponentName(this, AdminReceiver.class);
//		sw_DeviceAdminActive = (Switch) findViewById(R.id.screenlock_switch_id);
//		sw_DeviceAdminActive.setOnCheckedChangeListener(this);
		
		imbtn_screenlock = (ImageButton) findViewById(R.id.screenlock_btn_id);
		imbtn_screenlock.setOnClickListener(this);
		
		tb_DeviceAdminActive = (ToggleButton) findViewById(R.id.screenlock_togglebtn_id);
		tb_DeviceAdminActive.setOnCheckedChangeListener(this);
		//android.intent.action.ASSIST 唤醒 Google Now
//		checkAdminActive(true);
		
	}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setSystemUiVisibility(View rootView) {
            rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
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
		case R.id.screenlock_togglebtn_id:
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
//		sw_DeviceAdminActive.setChecked(working);
		tb_DeviceAdminActive.setChecked(working);
		if(working){
			imbtn_screenlock.setBackgroundResource(R.drawable.ic_lock_power_off_background);
		}else{
			imbtn_screenlock.setBackgroundResource(R.drawable.grayround);
		}
		Intent refreshwidget_intent = new Intent(ScreenLockWidgetProvider.REFRESHWIDGET_ACTION);
		refreshwidget_intent.putExtra(
				ScreenLockWidgetProvider.REFRESHWIDGET_FLAG,
				working ? ScreenLockWidgetProvider.WIDGET_BTN_ENABLE
						: ScreenLockWidgetProvider.WIDGET_BTN_DISABLE);
		sendBroadcast(refreshwidget_intent);
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
		menu.add(0, 0, 0, getResources().getString(R.string.action_about_content));
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 0:
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
