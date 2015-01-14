package com.fornut.assisttools.controllers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.fornut.assisttools.MainActivity;
import com.fornut.assisttools.R;
import com.fornut.assisttools.models.DevicePolicyManagerUtils;

public class ScreenLockWidgetProvider extends AppWidgetProvider {

    static boolean DBG = false;
    static String TAG = "AssistTools-WidgetProvider";

    private static Context mContext;
    private static AppWidgetManager mAppWidgetManager;
    int[] mAppWidgetIds;
    public static final String LOCKSCREEN_FLAG = "screenlock_flag";
    public static final boolean LOCKSCREEN_NOW = true;
    public static final String LOCKSCREEN_ACTION = "com.fornut.assisttools.LOCKNOW";
    public static final String REFRESHWIDGET_ACTION = "com.fornut.assisttools.REFRESHWIDGET";
    public static final String REFRESHWIDGET_FLAG = "refreshwidget_flag";
    public static final boolean WIDGET_BTN_DISABLE = false;
    public static final boolean WIDGET_BTN_ENABLE = true;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        // TODO Auto-generated method stub
        mContext = context;
        mAppWidgetManager = appWidgetManager;
        mAppWidgetIds = appWidgetIds;
        boolean isActive = DevicePolicyManagerUtils.getInstance(context)
                .checkAdminActive();
        refreshWidget(context, appWidgetManager, mAppWidgetIds, isActive);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);
        mContext = context;
        String action = intent.getAction();
        if (DBG)
            Log.d(TAG, "onReceive..." + action);
        if (action.equals(LOCKSCREEN_ACTION)) {
            lockScreenNow(context);
        } else if (action.equals(REFRESHWIDGET_ACTION)) {
            boolean isActive = intent
                    .getBooleanExtra(REFRESHWIDGET_FLAG, false);
            refreshWidget(mContext, mAppWidgetManager, mAppWidgetIds, isActive);
        }
    }

    void refreshWidget(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds, boolean isActive) {
        if (appWidgetManager == null) {
            mAppWidgetManager = AppWidgetManager.getInstance(mContext);
            appWidgetManager = mAppWidgetManager;
        }
        if (appWidgetIds == null) {
            mAppWidgetIds = mAppWidgetManager
                    .getAppWidgetIds(new ComponentName(mContext,
                            ScreenLockWidgetProvider.class));
            appWidgetIds = mAppWidgetIds;
        }
        final int N = appWidgetIds.length;
        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(LOCKSCREEN_FLAG, LOCKSCREEN_NOW);

            Intent intent2 = new Intent(LOCKSCREEN_ACTION);

            /*
             * PendingIntent pendingIntent = PendingIntent.getActivity(context,
             * 0, intent, 0);
             */
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent2, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views;
            if (isActive) {
                views = new RemoteViews(mContext.getPackageName(),
                        R.layout.screenlock_widgetlayout);
            } else {
                views = new RemoteViews(mContext.getPackageName(),
                        R.layout.test_screenlock_widgetlayout);
            }
            views.setOnClickPendingIntent(R.id.screenlock_widget_btn_id,
                    pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void lockScreenNow(Context context) {
        if (DevicePolicyManagerUtils.getInstance(context).checkAdminActive()) {
            DevicePolicyManagerUtils.getInstance(context).lockScreenNow();
        } else {
            Toast.makeText(
                    context,
                    context.getResources().getString(
                            R.string.toast_app_no_active), Toast.LENGTH_LONG)
                    .show();
        }
    }

}
