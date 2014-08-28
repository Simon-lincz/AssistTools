package com.lcz.screenlock;

import com.lcz.screenlock.IMainActivity;


interface IMainService {
	void registerCallback(IMainActivity callback);
	void stopService();
	
	
	void GoHomeLauncher();
	void setVolumeUP();
	void setVolumeDown();
	
	void checkAdminActive(boolean activeNow);
	void activeManage();
	void removeManage();
	void lockScreenNow();
	
	
}