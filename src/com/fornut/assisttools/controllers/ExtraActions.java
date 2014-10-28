package com.fornut.assisttools.controllers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.fornut.assisttools.R;
import com.fornut.assisttools.models.DevicePolicyManagerUtils;

public class ExtraActions extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lockScreenNow(this);
		finish();
	}
	
	private void lockScreenNow(Context context) {
		if (DevicePolicyManagerUtils.getInstance(this).checkAdminActive()) {
			DevicePolicyManagerUtils.getInstance(this).lockScreenNow();
		} else {
			Toast.makeText(context, context.getResources().getString(R.string.toast_app_no_active), Toast.LENGTH_LONG).show();
		}
	}
}
