package com.lcz.screenlock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;

public class ControlBoard {
	
	static boolean DBG = true;
	static String TAG = "ScreenLock-WhiteDot";
	ControlBoard_Interface mInterface;

	Context mContext;
	LayoutInflater mLayoutInflater;
	private WindowManager mWindowManager;
	private Resources mResources;
	Utils mUtils;
	
	private boolean controlboard_isAdded = false; // 是否已增加
	private ViewGroup control_board;
	private Drawable control_board_bg_drawable;
	int controlbutton_ids[] = {R.id.control_board_WNbtn,R.id.control_board_Nbtn,R.id.control_board_ENbtn,
					   R.id.control_board_Wbtn,R.id.control_board_centerbtn,R.id.control_board_Ebtn,
					   R.id.control_board_WSbtn,R.id.control_board_Sbtn,R.id.control_board_ESbtn};
	ControlButtons mControlButtons[] = new ControlButtons[9];
	private LayoutParams params_board;
	
	interface ControlBoard_Interface {
		void setVisibility(int visibility);
	}
	
	class ControlButtons{
		ImageButton imageButton;
		boolean btn_empty = true;
		public ControlButtons(ImageButton button) {
			// TODO Auto-generated constructor stub
			imageButton = button;
		}
		
		void setBackgroundResource(int resid){
			btn_empty = false;
			imageButton.setBackgroundResource(resid);
		}
		
		@SuppressLint("NewApi")
		void setBackgroundDrawable(Drawable background){
			btn_empty = false;
			imageButton.setBackground(background);
		}
		
		void setVisibility(int visibility){
			imageButton.setVisibility(visibility);
		}
	}
	
	public ControlBoard(Context context,ControlBoard_Interface board_Interface) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		mResources = mContext.getResources();
		mInterface = board_Interface;
		mUtils = new Utils(mContext);
		createControlBoard(context);
	}

	/**
	 * 创建悬浮控制板
	 */
	private void createControlBoard(Context context) {
		if (controlboard_isAdded) {
			return;
		}
		control_board = (ViewGroup) mLayoutInflater.inflate(R.layout.control_board, null);
		control_board_bg_drawable = control_board.getBackground();
		control_board_bg_drawable.setAlpha(mResources.getInteger(R.integer.control_board_bg_alpha));
		
		params_board = new WindowManager.LayoutParams();

		// 设置window type
		params_board.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		
		/*
		 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */
		params_board.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
//		params_board.alpha = 0.5f;//透明度

		// 设置Window flag
		params_board.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		// 设置悬浮控制板的长得宽
		params_board.width = mResources.getInteger(R.integer.control_board_width);
		params_board.height = mResources.getInteger(R.integer.control_board_height);
		
		mWindowManager.addView(control_board, params_board);
		controlboard_isAdded = true;
		
		getControlButtons();
		initControlButtons();
		updateControlButtons();
	}
	
	/**
	 * 
	 */
	void getControlButtons(){
		for(int i = 0;i<controlbutton_ids.length;i++){
			mControlButtons[i] = new ControlButtons((ImageButton)control_board.findViewById(controlbutton_ids[i]));
		}
	}
	
	/**
	 *init the control button icon
	 *add special func
	 */
	void initControlButtons(){
		mControlButtons[1].setBackgroundResource(R.drawable.ic_control_board_lock);
		mControlButtons[1].imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mUtils.lockScreenNow();
			}
		});
		mControlButtons[3].setBackgroundResource(R.drawable.ic_control_board_volume_down);
		mControlButtons[3].imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mUtils.setVolumeDown();
			}
		});
		mControlButtons[4].setBackgroundResource(R.drawable.ic_control_board_center_btn_bg);
		mControlButtons[4].imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setVisibility(View.INVISIBLE);
			}
		});
		mControlButtons[5].setBackgroundResource(R.drawable.ic_control_board_volume_up);
		mControlButtons[5].imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mUtils.setVolumeUP();
			}
		});
		mControlButtons[7].setBackgroundResource(R.drawable.ic_control_board_home);
		mControlButtons[7].imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mUtils.GoHomeLauncher();
			}
		});
	}

	/**
	 * check the btn_empty flag,and set button visibility
	 */
	void updateControlButtons(){
		for(int i = 0;i<controlbutton_ids.length;i++){
			if(mControlButtons[i].btn_empty){
				mControlButtons[i].setVisibility(View.INVISIBLE);
				//mControlButtons[i].setVisibility(View.GONE);/gone是直接在layout里面去掉，会影响整体布局，尤其是relativelayout
			}else{
				mControlButtons[i].setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * @author lcz
	 * @param alpha [0-255]
	 */
	void setControlBoard_bg_alpha(int alpha){
		if(control_board_bg_drawable != null){
			control_board_bg_drawable.setAlpha(alpha);
		}
	}
	
	/**
	 * @author lcz
	 * @param visibility [View.GONE,View.VISIBLE]
	 */
	void setVisibility(int visibility){
		mInterface.setVisibility(visibility);
		control_board.setVisibility(visibility);
	}
}
