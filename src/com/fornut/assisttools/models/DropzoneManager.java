package com.fornut.assisttools.models;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.fornut.assisttools.views.ControlBoard;
import com.fornut.assisttools.views.WhiteDot;

/**
 * 悬浮view的创建、控制及动画
 *
 * @author lcz
 */
public class DropzoneManager {

	private static boolean DBG = false;
	private static String TAG = "AssistTools-DropzoneManager";

	private boolean mDropzoneIsAdded = false; // 是否已增加悬浮按钮
	private WhiteDot mWhiteDot;
	private ControlBoard mControlBoard;
	private LayoutParams mWhiteDot_Params;
	private int isClick = 0;
	private WindowManager mWindowManager;

	private static final int MSG_BASE = 0;
	private static final int MSG_SHOW_FLOATBTN = MSG_BASE + 1;
	private static final int MSG_HIDE_FLOATBTN = MSG_BASE + 2;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SHOW_FLOATBTN:
				// float_btn.setVisibility(View.VISIBLE);
				// mControlBoard.setVisibility(View.GONE);
				break;
			case MSG_HIDE_FLOATBTN:
				// float_btn.setVisibility(View.GONE);
				// mControlBoard.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		};
	};

	public DropzoneManager(Context context) {
		// TODO Auto-generated constructor stub
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		createFloatBtn(context);
	}

	/**
	 * 创建悬浮按钮
	 */
	private void createFloatBtn(Context context) {
		if (mDropzoneIsAdded) {
			return;
		}
		mWhiteDot = new WhiteDot(context);
		mWhiteDot_Params = new WindowManager.LayoutParams();
		// 设置window type
		mWhiteDot_Params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		/*
		 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */

		mWhiteDot_Params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		// 设置Window flag
		mWhiteDot_Params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮按钮不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		// 设置悬浮按钮的长得宽
		mWhiteDot_Params.width = 100;
		mWhiteDot_Params.height = 100;
		// 通知view组件重绘
		mWhiteDot.invalidate();

		// 设置悬浮按钮的Touch监听
		mWhiteDot.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;
			int paramX, paramY;

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = mWhiteDot_Params.x;
					paramY = mWhiteDot_Params.y;
					isClick = 0;
					if (DBG)
						Log.d(TAG, "ACTION_DOWN");
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					mWhiteDot_Params.x = paramX + dx;
					mWhiteDot_Params.y = paramY + dy;
					// 更新悬浮按钮位置
					mWindowManager
							.updateViewLayout(mWhiteDot, mWhiteDot_Params);
					isClick++;
					if (DBG)
						Log.d(TAG, "ACTION_MOVE");
					break;
				case MotionEvent.ACTION_UP:
					if (DBG)
						Log.d(TAG, "ACTION_UP");
					break;
				}
				return false;// true 其他的动作都捕捉不到，比如Click，longClick等等
			}
		});

		/*
		 * OnClickListener只发生在up之后
		 */
		mWhiteDot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (DBG)
					Log.d(TAG, "onClick");
				if (isClick < 5) {
					mHandler.sendEmptyMessage(MSG_HIDE_FLOATBTN);
				}
			}
		});

		/*
		 * OnLongClickListene可能发生在down-move LongClick move-up
		 */
		mWhiteDot.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (DBG)
					Log.d(TAG, "onLongClick");
				return false;
			}
		});

		mWindowManager.addView(mWhiteDot, mWhiteDot_Params);
		mDropzoneIsAdded = true;
	}

}
