package com.fornut.assisttools;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.fornut.assisttools.controllers.AssistToolsService;
import com.fornut.assisttools.controllers.CalendarFragment;
import com.fornut.assisttools.controllers.HomeFragment;
import com.fornut.assisttools.controllers.ProfileFragment;
import com.fornut.assisttools.controllers.SettingsFragment;
import com.fornut.assisttools.views.residemenu.ResideMenu;
import com.fornut.assisttools.views.residemenu.ResideMenuItem;

public class MainActivity extends FragmentActivity implements
        View.OnClickListener {

    private ResideMenu mResideMenu;
    private ResideMenuItem mItemHome;
    private ResideMenuItem mItemProfile;
    private ResideMenuItem mItemCalendar;
    private ResideMenuItem mItemSettings;

    private AssistToolsService mService;
    private boolean mIsServiceConnected = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpMenu();
        if (savedInstanceState == null)
            changeFragment(new HomeFragment());

        Intent service = new Intent(this, AssistToolsService.class);
        startService(service);
        bindService(service, mServiceConnection, Service.START_STICKY
                | Service.BIND_AUTO_CREATE);
    }

    private void setUpMenu() {

        // attach to current activity;
        mResideMenu = new ResideMenu(this);
        // resideMenu.setBackground(R.drawable.menu_background);
        mResideMenu.setBackground(getWallpaper());
        mResideMenu.attachToActivity(this);
        mResideMenu.setMenuListener(menuListener);
        // valid scale factor is between 0.0f and 1.0f. leftmenu'width is
        // 150dip.
        mResideMenu.setScaleValue(0.6f);

        // create menu items;
        mItemHome = new ResideMenuItem(this, R.drawable.icon_home, "Home");
        mItemProfile = new ResideMenuItem(this, R.drawable.icon_profile,
                "Profile");
        mItemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar,
                "Calendar");
        mItemSettings = new ResideMenuItem(this, R.drawable.icon_settings,
                "Settings");

        mItemHome.setOnClickListener(this);
        mItemProfile.setOnClickListener(this);
        mItemCalendar.setOnClickListener(this);
        mItemSettings.setOnClickListener(this);

        mResideMenu.addMenuItem(mItemHome, ResideMenu.DIRECTION_RIGHT);
        mResideMenu.addMenuItem(mItemProfile, ResideMenu.DIRECTION_RIGHT);
        mResideMenu.addMenuItem(mItemCalendar, ResideMenu.DIRECTION_RIGHT);
        mResideMenu.addMenuItem(mItemSettings, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mResideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                    }
                });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mResideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                    }
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mResideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == mItemHome) {
            changeFragment(new HomeFragment());
        } else if (view == mItemProfile) {
            changeFragment(new ProfileFragment());
        } else if (view == mItemCalendar) {
            changeFragment(new CalendarFragment());
        } else if (view == mItemSettings) {
            changeFragment(new SettingsFragment());
        }

        // resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            // Toast.makeText(mContext, "Menu is opened!",
            // Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            // Toast.makeText(mContext, "Menu is closed!",
            // Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment) {
        mResideMenu.clearIgnoredViewList();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu
    public ResideMenu getResideMenu() {
        return mResideMenu;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mIsServiceConnected) {
            unbindService(mServiceConnection);
            mService = null;
            mIsServiceConnected = false;
        }
        super.onDestroy();
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mIsServiceConnected = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 返回一个MsgService对象
            mService = ((AssistToolsService.LocalBinder) service).getService();
            mIsServiceConnected = true;
        }
    };
}