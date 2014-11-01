package com.fornut.assisttools.misc;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

	SharedPreferences mSharedPreferences;
	public static final String SharedPreferences_Name = "config";

	private static SharedPreferenceUtils sInstance;

	static public SharedPreferenceUtils getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SharedPreferenceUtils(context);
		}
		return sInstance;
	}

	public SharedPreferenceUtils(Context context) {
		// TODO Auto-generated constructor stub
		mSharedPreferences = context.getSharedPreferences(
				SharedPreferences_Name, Context.MODE_PRIVATE);
	}
}