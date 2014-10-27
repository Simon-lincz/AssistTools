package com.fornut.assisttools;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AdminReceiver extends DeviceAdminReceiver{

	static boolean DBG = false;
	static String TAG = "ScreenLock-AdminReceiver";
	
	public AdminReceiver() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onEnabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(DBG)Log.d(TAG, "onEnabled");
		Intent refreshwidget_intent = new Intent(ScreenLockWidgetProvider.REFRESHWIDGET_ACTION);
		refreshwidget_intent.putExtra(
				ScreenLockWidgetProvider.REFRESHWIDGET_FLAG,ScreenLockWidgetProvider.WIDGET_BTN_ENABLE);
		context.sendBroadcast(refreshwidget_intent);
		super.onEnabled(context, intent);
	}
	
	@Override
	public void onDisabled(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(DBG)Log.d(TAG, "onDisabled");
		Intent refreshwidget_intent = new Intent(ScreenLockWidgetProvider.REFRESHWIDGET_ACTION);
		refreshwidget_intent.putExtra(
				ScreenLockWidgetProvider.REFRESHWIDGET_FLAG,ScreenLockWidgetProvider.WIDGET_BTN_DISABLE);
		context.sendBroadcast(refreshwidget_intent);
		super.onDisabled(context, intent);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		super.onReceive(context, intent);
	}
	
}
