package com.lcz.screenlock;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class WhiteDot {
	
	
	static boolean DBG = true;
	static String TAG = "ScreenLock-WhiteDot";
	

	Context mContext;
	LayoutInflater mLayoutInflater;
	private WindowManager wm;
	ControlBoard mControlBoard;
	
	private boolean floatbtn_isAdded = false; // �Ƿ�������������ť
	private Button float_btn;
	private Drawable float_btn_bg_drawable;
	private LayoutParams params_btn;
	private int isClick = 0;
	
	
	private static final int MSG_BASE = 0;
	private static final int MSG_SHOW_FLOATBTN = MSG_BASE + 1;
	private static final int MSG_HIDE_FLOATBTN = MSG_BASE + 2;
	
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SHOW_FLOATBTN:
//				float_btn.setVisibility(View.VISIBLE);
				mControlBoard.setVisibility(View.GONE);
				break;
			case MSG_HIDE_FLOATBTN:
//				float_btn.setVisibility(View.GONE);
				mControlBoard.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		};
	};
	
	
	public WhiteDot() {
		// TODO Auto-generated constructor stub
		this(null);
	}

	public WhiteDot(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		wm = (WindowManager) mContext.getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		createFloatBtn(mContext);
		mControlBoard = new ControlBoard(mContext,new ControlBoard.ControlBoard_Interface() {
			
			@Override
			public void setVisibility(int visibility) {
				// TODO Auto-generated method stub
				if(visibility == View.GONE || visibility == View.INVISIBLE){
					float_btn.setVisibility(View.VISIBLE);
				}else if(visibility == View.VISIBLE){
					float_btn.setVisibility(View.INVISIBLE);
				}
			}
		});
		mControlBoard.setVisibility(View.GONE);
	}

	/**
	 * ����������ť
	 */
	private void createFloatBtn(Context context) {
		if (floatbtn_isAdded) {
			return;
		}
		float_btn = new Button(context.getApplicationContext());
		float_btn.setBackgroundResource(R.drawable.float_btn_bg_iphone);
		float_btn_bg_drawable = float_btn.getBackground();
		
		params_btn = new WindowManager.LayoutParams();

		// ����window type
		params_btn.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		/*
		 * �������Ϊparams.type = WindowManager.LayoutParams.TYPE_PHONE; ��ô���ȼ��ή��һЩ,
		 * ������֪ͨ�����ɼ�
		 */

		params_btn.format = PixelFormat.RGBA_8888; // ����ͼƬ��ʽ��Ч��Ϊ����͸��

		// ����Window flag
		params_btn.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * �����flags���Ե�Ч����ͬ���������� ������ť���ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		// ����������ť�ĳ��ÿ�
		params_btn.width = 100;
		params_btn.height = 100;

		// ����������ť��Touch����
		float_btn.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;
			int paramX, paramY;

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = params_btn.x;
					paramY = params_btn.y;
					isClick = 0;
					if(DBG)Log.d(TAG, "ACTION_DOWN");
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					params_btn.x = paramX + dx;
					params_btn.y = paramY + dy;
					// ����������ťλ��
					wm.updateViewLayout(float_btn, params_btn);
					isClick++;
					if(DBG)Log.d(TAG, "ACTION_MOVE");
					break;
				case MotionEvent.ACTION_UP:
					if(DBG)Log.d(TAG, "ACTION_UP");
					break;
				}
				return false;//true �����Ķ�������׽����������Click��longClick�ȵ�
			}
		});
		
		/*
		 * OnClickListenerֻ������up֮��
		 */
		float_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(DBG)Log.d(TAG, "onClick");
				if(isClick < 5){
					mHandler.sendEmptyMessage(MSG_HIDE_FLOATBTN);
				}
			}
		});
		
		/*
		 * OnLongClickListene���ܷ�����down-move LongClick move-up
		 */
		float_btn.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(DBG)Log.d(TAG, "onLongClick");
				return false;
			}
		});
		
		wm.addView(float_btn, params_btn);
		floatbtn_isAdded = true;
	}
	
	
	/**
	 * @author lcz
	 * @param resid
	 */
	void setFloatBtn_BackgroundResource(int resid){
		if(float_btn != null){
			float_btn.setBackgroundResource(resid);
		}
	}
	
	/**
	 * @author lcz
	 * @param alpha [0-255]
	 */
	void setFloatBtn_bg_alpha(int alpha){
		if(float_btn_bg_drawable != null){
			float_btn_bg_drawable.setAlpha(alpha);
		}
	}
}
