package com.lcz.screenlock;


import android.app.Activity;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ScreenLockWidgetProvider extends AppWidgetProvider{

	static boolean DBG = true;
	static String TAG = "ScreenLock-WidgetProvider";
	
	private Context mContext;
	public static final String LOCKSCREEN_FLAG = "screenlock_flag";
	public static final boolean LOCKSCREEN_NOW = true;
	public static final String LOCKSCREEN_ACTION = "com.lcz.screenlock.LOCKNOW";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		if(DBG)Log.d(TAG, "onUpdate...");
		mContext = context;
		final int N = appWidgetIds.length;
		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			Intent intent = new Intent(context, MainActivity.class);
			intent.putExtra(LOCKSCREEN_FLAG, LOCKSCREEN_NOW);
			
			Intent intent2 = new Intent(LOCKSCREEN_ACTION);
			
			/*PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);*/
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);
			
			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			// 用于监听widget上面的一个view的click
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.screenlock_widgetlayout);
			views.setOnClickPendingIntent(R.id.screenlock_widget_btn_id, pendingIntent);
			
//			Intent intent2 = new Intent("com.lcz.screenlock.LOCKNOW");
//			views.setOnClickFillInIntent(R.id.screenlock_widget_btn_id, intent2);
			
			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		if(DBG)Log.d(TAG, "onReceive...");
		if(intent.getAction().equals(LOCKSCREEN_ACTION)){
			lockScreenNow(context);
		}
	}
	
	private void lockScreenNow(Context context) {
		// 获取设备管理服务
		DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		// AdminReceiver 继承自 DeviceAdminReceiver
		ComponentName componentName = new ComponentName(context, AdminReceiver.class);
		boolean isActive = policyManager.isAdminActive(componentName);
		if(isActive){
			policyManager.lockNow();
		}else{
			Toast.makeText(context, "no active the app.", Toast.LENGTH_LONG).show();
		}
	}
}
