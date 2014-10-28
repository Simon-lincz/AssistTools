package com.fornut.assisttools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.fornut.assisttools.controllers.ScreenLockWidgetProvider;
import com.fornut.assisttools.models.DevicePolicyManagerUtils;
import com.fornut.assisttools.views.WhiteDot;

public class MainActivity extends Activity implements OnClickListener,OnCheckedChangeListener{

	static boolean DBG = false;
	static String TAG = "ScreenLock-Main";
	
	ImageButton imbtn_screenlock;
	ToggleButton tb_DeviceAdminActive;
	ToggleButton tb_PhoneCallLockActive;

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
	
	boolean isActive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imbtn_screenlock = (ImageButton) findViewById(R.id.screenlock_btn_id);
		imbtn_screenlock.setOnClickListener(this);
		
		tb_DeviceAdminActive = (ToggleButton) findViewById(R.id.screenlock_togglebtn_id);
		tb_DeviceAdminActive.setOnCheckedChangeListener(this);
//		checkAdminActive(true);
		
		tb_PhoneCallLockActive = (ToggleButton) findViewById(R.id.screenlock_togglebtn_phone_id);
		tb_PhoneCallLockActive.setOnCheckedChangeListener(this);
		
		init();
		
		checkSharePrefence();
	}

	 private void init() {  
	        RelativeLayout layout= (RelativeLayout) findViewById(R.id.root);  
	        /*/
	        final CustomView view=new CustomView(this);  
	        view.setMinimumHeight(1000);  
	        view.setMinimumWidth(600);  
	        //通知view组件重绘    
	        view.invalidate();  
	        layout.addView(view);  
	        /*/
	        final WhiteDot view=new WhiteDot(this);  
	        view.setMinimumHeight(200);  
	        view.setMinimumWidth(200); 
	        //通知view组件重绘    
	        view.invalidate();  
	        layout.addView(view);  
	        //*/
	    }

	void checkSharePrefence(){
		mSharedPreferences = getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
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
	
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setSystemUiVisibility(View rootView) {
            rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		refreshView(DevicePolicyManagerUtils.getInstance(this).checkAdminActive());
		Intent intent = getIntent();
		if(intent.getBooleanExtra(ScreenLockWidgetProvider.LOCKSCREEN_FLAG, false)){
			DevicePolicyManagerUtils.getInstance(this).lockScreenNow();
		}
		super.onStart();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
		// TODO Auto-generated method stub
		switch (compoundButton.getId()) {
		case R.id.screenlock_togglebtn_id:
			if(ischeck){
				DevicePolicyManagerUtils.getInstance(this).activeManage(this);
			}else{
				DevicePolicyManagerUtils.getInstance(this).removeManage();
				refreshView(false);
			}
			break;
		case R.id.screenlock_togglebtn_phone_id:
			Editor editor = mSharedPreferences.edit();
			if(ischeck){
				editor.putBoolean(CONFIG_PHONE_CALL_SCREENLOCK,CONFIG_YES);
			}else{
				editor.putBoolean(CONFIG_PHONE_CALL_SCREENLOCK,CONFIG_NO);
			}
			editor.commit();
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
			if (DevicePolicyManagerUtils.getInstance(this).checkAdminActive()) {
				Log.d("lincz1", "lock");
				DevicePolicyManagerUtils.getInstance(this).lockScreenNow();
			} else {
				DevicePolicyManagerUtils.getInstance(this).activeManage(this);
			}

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
		menu.add(0, 0, 0, getResources().getString(R.string.action_about_content));
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
