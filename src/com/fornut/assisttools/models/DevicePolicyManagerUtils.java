package com.fornut.assisttools.models;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.fornut.assisttools.R;

public class DevicePolicyManagerUtils {

    private Context mContext;
    private DevicePolicyManager mPolicyManager;
    private ComponentName mComponentName;
    private static DevicePolicyManagerUtils sInstance;

    static public DevicePolicyManagerUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DevicePolicyManagerUtils(context);
        }
        return sInstance;
    }

    public DevicePolicyManagerUtils(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mPolicyManager = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(context, AdminReceiver.class);
    }

    public boolean checkAdminActive() {
        return mPolicyManager.isAdminActive(mComponentName);
    }

    public void activeManage(Activity activity, int requestCode) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, activity
                .getResources().getString(R.string.device_admin_explanation));
        activity.startActivityForResult(intent, requestCode);
    }

    public void removeManage() {
        if (mPolicyManager != null && mComponentName != null) {
            mPolicyManager.removeActiveAdmin(mComponentName);
        }
    }

    public boolean lockScreenNow() {
        if (checkAdminActive()) {
            mPolicyManager.lockNow();
            DropzoneManager.getInstance(mContext).showWhiteDot();
            return true;
        } else {
            // checkAdminActive(true);
            return false;
        }
    }

    public void resetPasswd() {
        mPolicyManager.resetPassword("1111",
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
    }

    public void deletePasswd() {
        mPolicyManager.resetPassword("1111",
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
    }

}
