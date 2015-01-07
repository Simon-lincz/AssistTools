package com.fornut.assisttools.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fornut.assisttools.controllers.AssistToolsService;

public class MainReceiver extends BroadcastReceiver {

    static boolean DBG = true;
    static String TAG = "AssistTools-MainReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (DBG) {
            Log.d(TAG, "action:" + intent.getAction());
        }
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent service = new Intent(context, AssistToolsService.class);
            context.startService(service);
        } else if (intent.getAction().equals(
                "com.fornut.assisttools.action.test")) {
            Intent service = new Intent(context, AssistToolsService.class);
            context.startService(service);
        }
    }
}
