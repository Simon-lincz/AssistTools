package com.fornut.assisttools.misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;

public class SharedPreferenceUtils {

    static SharedPreferences mSharedPreferences;
    public static final String SHAREDPREFERENCE_NAME = "config";
    public static final String PACKAGE_VERSION = "version_code";
    public static final String DISPLAY_LIST = "display_switch_list";
    public static final String WHITEDOT_LAST_POSITION = "wd_position";

    private static SharedPreferenceUtils sInstance;

    private static int mCurVersionCode = -1;

    static public SharedPreferenceUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferenceUtils(context);
        }
        return sInstance;
    }

    public SharedPreferenceUtils(Context context) {
        // TODO Auto-generated constructor stub
        mSharedPreferences = context.getSharedPreferences(
                SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        try {
            mCurVersionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mCurVersionCode = -1;
        }
    }

    public void saveDisplaySwitchList(String switchlist) {
        Editor editor = mSharedPreferences.edit();
        editor.putString(DISPLAY_LIST, switchlist);
        editor.commit();
    }

    public String getDisplaySwitchList() {
        String list = mSharedPreferences.getString(DISPLAY_LIST, null);
        if (isVersionUpgrade()) {
            return null;
        }
        return list;
    }

    public boolean isVersionUpgrade() {
        int lastVersionCode = mSharedPreferences.getInt(PACKAGE_VERSION, 0);
        if (lastVersionCode != mCurVersionCode) {
            return true;
        }
        return false;
    }

    public void saveWhiteDotPosition(String xy) {
        Editor editor = mSharedPreferences.edit();
        editor.putString(WHITEDOT_LAST_POSITION, xy);
        editor.commit();
    }

    public String getWhiteDotPosition() {
        return mSharedPreferences.getString(WHITEDOT_LAST_POSITION, null);
    }
}