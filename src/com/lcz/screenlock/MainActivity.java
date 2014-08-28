package com.lcz.screenlock;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
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

	static boolean DBG = true;
	static String TAG = "ScreenLock-MainActivity";
	
	ImageButton imbtn_screenlock;
	ToggleButton tb_DeviceAdminActive;
	ToggleButton tb_PhoneCallLockActive;
	MainActivityServiceConnect mServiceConnect;
	boolean isActive = false;
	
	IMainService mService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imbtn_screenlock = (ImageButton) findViewById(R.id.screenlock_btn_id);
		imbtn_screenlock.setOnClickListener(this);
		
		tb_DeviceAdminActive = (ToggleButton) findViewById(R.id.screenlock_togglebtn_id);
		tb_DeviceAdminActive.setOnCheckedChangeListener(this);
		
		tb_PhoneCallLockActive = (ToggleButton) findViewById(R.id.screenlock_togglebtn_phone_id);
		tb_PhoneCallLockActive.setOnCheckedChangeListener(this);
		
		mServiceConnect = new MainActivityServiceConnect();
		Intent conn = new Intent(this, MainService.class);
		startService(conn);
		bindService(conn, mServiceConnect, Service.BIND_NOT_FOREGROUND);
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
		try {
			if(mService != null)
				mService.checkAdminActive(false);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(DBG)Log.d(TAG, "onStart");
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(DBG)Log.d(TAG, "onDestroy");
		unbindService(mServiceConnect);
//		try {
//			mService.stopService();
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		super.onDestroy();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
		// TODO Auto-generated method stub
		switch (compoundButton.getId()) {
		case R.id.screenlock_togglebtn_id:
			if(ischeck){
//				activeManage();
			}else{
/*				removeManage();
*/			}
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
			break;

		default:
			break;
		}
	}
	
	void refreshView(boolean working){
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
	
	IMainActivity mCallback = new  IMainActivity.Stub(){

		@Override
		public void refreshView(boolean working) throws RemoteException {
			// TODO Auto-generated method stub
			
		}
	};
	
	class MainActivityServiceConnect implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			if (DBG)
				Log.d(TAG, "onServiceConnected");
			mService = IMainService.Stub.asInterface(service);
			try {
				mService.registerCallback(mCallback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			if (DBG)
				Log.d(TAG, "onServiceDisconnected");
		}
	}
}
