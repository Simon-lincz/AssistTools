package com.fornut.assisttools.controllers;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.fornut.assisttools.models.DropzoneManager;

public class AssistToolsService extends Service {

	private static final String TAG = "AssistTools-AssistToolsService";
	private IBinder binder  = new AssistToolsService.LocalBinder();
	private DropzoneManager mDropzoneManager;

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		mDropzoneManager = new DropzoneManager(this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		return START_STICKY;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (mDropzoneManager != null) {
			mDropzoneManager.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	// 定义内容类继承Binder
	public class LocalBinder extends Binder {
		// 返回本地服务
		public AssistToolsService getService() {
			return AssistToolsService.this;
		}
	}

}
