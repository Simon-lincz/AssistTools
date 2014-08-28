package com.lcz.screenlock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MainService extends Service{

	static boolean DBG = true;
	static String TAG = "ScreenLock-MainService";
	
	public static final String SERVICE_ACTION = "com.lcz.screenlock.MainService";
	
	IMainActivity mCallback;
	
	WhiteDot mWhiteDot;
	
	Utils mUtils;
	
	public void onCreate() {
		mUtils = new Utils(MainService.this);
		mUtils.checkSharePrefence();
		mWhiteDot = new WhiteDot(MainService.this.getApplicationContext(),mUtils);
		mUtils.CreateForegroundNotification();
	};
	
	IMainService.Stub mBinder = new IMainService.Stub() {

		@Override
		public void registerCallback(IMainActivity callback)
				throws RemoteException {
			// TODO Auto-generated method stub
			mCallback = callback;
		}
		
		@Override
		public void stopService() throws RemoteException {
			// TODO Auto-generated method stub
			MainService.this.stopSelf();
		}

		@Override
		public void setVolumeUP() throws RemoteException {
			// TODO Auto-generated method stub
			mUtils.setVolumeUP();
		}

		@Override
		public void GoHomeLauncher() throws RemoteException {
			// TODO Auto-generated method stub
			mUtils.GoHomeLauncher();
		}

		@Override
		public void setVolumeDown() throws RemoteException {
			// TODO Auto-generated method stub
			mUtils.setVolumeDown();
		}

		@Override
		public void activeManage() throws RemoteException {
			// TODO Auto-generated method stub
			mUtils.activeManage();
		}

		@Override
		public void lockScreenNow() throws RemoteException {
			// TODO Auto-generated method stub
			mUtils.lockScreenNow();
		}

		@Override
		public void checkAdminActive(boolean activeNow) throws RemoteException {
			// TODO Auto-generated method stub
			mUtils.checkAdminActive(activeNow);
		}

		@Override
		public void removeManage() throws RemoteException {
			// TODO Auto-generated method stub
			mUtils.removeManage();
		}

	};
	
	
	public void onDestroy() {
		if(DBG)Log.d(TAG, "onDestroy");
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public interface DeviceAdminStatus{
		void DeviceisActive(int isActive);
	}
	
}
