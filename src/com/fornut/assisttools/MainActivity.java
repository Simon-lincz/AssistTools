package com.fornut.assisttools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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

import com.fornut.assisttools.controllers.AssistToolsService;
import com.fornut.assisttools.controllers.ScreenLockWidgetProvider;
import com.fornut.assisttools.models.DevicePolicyManagerUtils;
import com.fornut.assisttools.views.WhiteDot;

public class MainActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	static boolean DBG = false;
	static String TAG = "AssistTools-MainActivity";

	private static final int ACTIVE_DEVICEPOLICYMANAGER_REQUESTCODE = 1;

	ImageButton imbtn_screenlock;
	ToggleButton tb_DeviceAdminActive;
	ToggleButton tb_PhoneCallLockActive;

	private AssistToolsService mService;
	private boolean mIsServiceConnected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();

		Intent service = new Intent(this, AssistToolsService.class);
		startService(service);
		bindService(service, mServiceConnection, Service.START_STICKY | Service.BIND_AUTO_CREATE);
	}

	private void init() {
		imbtn_screenlock = (ImageButton) findViewById(R.id.screenlock_btn_id);
		imbtn_screenlock.setOnClickListener(this);

		tb_DeviceAdminActive = (ToggleButton) findViewById(R.id.screenlock_togglebtn_id);
		tb_DeviceAdminActive.setOnCheckedChangeListener(this);
		// checkAdminActive(true);

		tb_PhoneCallLockActive = (ToggleButton) findViewById(R.id.screenlock_togglebtn_phone_id);
		tb_PhoneCallLockActive.setOnCheckedChangeListener(this);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.root);
		layout.addView(new SampleView(this));
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
		refreshView(DevicePolicyManagerUtils.getInstance(this)
				.checkAdminActive());
		Intent intent = getIntent();
		if (intent.getBooleanExtra(ScreenLockWidgetProvider.LOCKSCREEN_FLAG,
				false)) {
			DevicePolicyManagerUtils.getInstance(this).lockScreenNow();
		}
		super.onStart();
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
		// TODO Auto-generated method stub
		switch (compoundButton.getId()) {
		case R.id.screenlock_togglebtn_id:
			if (ischeck) {
				DevicePolicyManagerUtils.getInstance(this).activeManage(this,ACTIVE_DEVICEPOLICYMANAGER_REQUESTCODE);
			} else {
				DevicePolicyManagerUtils.getInstance(this).removeManage();
				refreshView(false);
			}
			break;
		case R.id.screenlock_togglebtn_phone_id:
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
				DevicePolicyManagerUtils.getInstance(this).lockScreenNow();
			} else {
				DevicePolicyManagerUtils.getInstance(this).activeManage(this,ACTIVE_DEVICEPOLICYMANAGER_REQUESTCODE);
			}

			break;

		default:
			break;
		}
	}

	void refreshView(boolean working) {
		// sw_DeviceAdminActive.setChecked(working);
		tb_DeviceAdminActive.setChecked(working);
		if (working) {
			imbtn_screenlock
					.setBackgroundResource(R.drawable.ic_lock_power_off_background);
		} else {
			imbtn_screenlock.setBackgroundResource(R.drawable.grayround);
		}
		Intent refreshwidget_intent = new Intent(
				ScreenLockWidgetProvider.REFRESHWIDGET_ACTION);
		refreshwidget_intent.putExtra(
				ScreenLockWidgetProvider.REFRESHWIDGET_FLAG,
				working ? ScreenLockWidgetProvider.WIDGET_BTN_ENABLE
						: ScreenLockWidgetProvider.WIDGET_BTN_DISABLE);
		sendBroadcast(refreshwidget_intent);
	}

	// ===============================================================================================

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mIsServiceConnected) {
			unbindService(mServiceConnection);
			mService = null;
			mIsServiceConnected = false;
		}
		super.onDestroy();
	}

	ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
			mIsServiceConnected = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// 返回一个MsgService对象
			mService = ((AssistToolsService.LocalBinder)service).getService();
			mIsServiceConnected = true;
		}
	};

	// ===============================================================================================

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case ACTIVE_DEVICEPOLICYMANAGER_REQUESTCODE:
			if (resultCode == RESULT_OK) {
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

	// ===============================================================================================

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(0, 0, 0,
				getResources().getString(R.string.action_about_content));
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.add(0, 0, 0,
				getResources().getString(R.string.action_about_content));
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 0:
			AlertDialog dialog;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.action_about_content).setCancelable(
					true);
			dialog = builder.create();
			dialog.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
