package com.lcz.screenlock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class MainNotification extends Notification{

	Context mContext;
	private MainActivityServiceConnect mServiceConnect;
	

	public MainNotification(Context context) {
		// TODO Auto-generated constructor stub
		this(1,"2",0);
		mContext = context;
		NotificationManager nm = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = null;
		notif = new Notification(R.drawable.ic_launcher,mContext.getString(R.string.app_name),System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class), Intent.FLAG_ACTIVITY_NEW_TASK);
		notif.setLatestEventInfo(mContext, mContext.getString(R.string.action_about),
				mContext.getString(R.string.action_about_content), contentIntent );
		notif.flags = Notification.FLAG_FOREGROUND_SERVICE;
		nm.notify(1, notif);
		Intent conn = new Intent(mContext, MainService.class);
		mServiceConnect = new MainActivityServiceConnect();
		mContext.bindService(conn, mServiceConnect, Service.BIND_NOT_FOREGROUND);
	}
	
	public MainNotification(int id,String str,long l){
		super(R.drawable.ic_launcher, "test", 0);
	}
	
	class MainActivityServiceConnect implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.d("sfasdf","onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}
	}
}
