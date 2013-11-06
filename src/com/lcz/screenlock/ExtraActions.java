package com.lcz.screenlock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class ExtraActions extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lockScreenNow(getApplicationContext());
		finish();
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
			Toast.makeText(context, context.getResources().getString(R.string.toast_app_no_active), Toast.LENGTH_LONG).show();
		}
	}
}
