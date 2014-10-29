package com.fornut.assisttools.views;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fornut.assisttools.R;

public class WhiteDot extends View{
	
	static boolean DBG = false;
	static String TAG = "AssistTools-WhiteDot";
	
	Context mContext;
	Resources mResources;
	//裁剪中间到环形，不过有锯齿，暂时去掉
//	private Path mPath;//for canvas clip

	private boolean mIsPressed = false;

	private int mWidth =  0, mHeight = 0;
	private int mRingRadius = 0, mRingWidth = 0;
	private int mXRadiusCorner = 0, mYRadiusCorner = 0;
	private int mAlphaPressed = 0, mAlphaNormal = 0;

	private Bitmap mCustomBackgroundBitmap;
	private boolean mEnableCustomBackground = false;

	public WhiteDot(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		mContext = context;
		mResources = getResources();

		//裁剪中间到环形，不过有锯齿，暂时去掉
//		mPath = new Path();

		refreshLayoutParams();
	}

	void refreshLayoutParams() {
		mWidth = mResources.getDimensionPixelSize(R.dimen.whitedot_width);
		mHeight = mResources.getDimensionPixelSize(R.dimen.whitedot_height);
		mRingRadius = mResources.getDimensionPixelSize(R.dimen.whitedot_ringRadius);
		mRingWidth = mResources.getDimensionPixelSize(R.dimen.whitedot_ringWidth);
		mXRadiusCorner = mResources.getDimensionPixelSize(R.dimen.whitedot_xRadiusCorner);
		mYRadiusCorner = mResources.getDimensionPixelSize(R.dimen.whitedot_yRadiusCorner);
		mAlphaNormal = mResources.getInteger(R.integer.whitedot_alpha_normal);
		mAlphaPressed = mResources.getInteger(R.integer.whitedot_alpha_pressed);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d(TAG, "onMeasure");
		setMeasuredDimension(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mIsPressed = true;
			invalidate();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_UP:
			mIsPressed = false;
			invalidate();
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDraw");
		super.onDraw(canvas);
		Paint p = new Paint();

		if (mEnableCustomBackground) {
			if (mIsPressed) {
				p.setAlpha(mAlphaPressed);
			} else {
				p.setAlpha(mAlphaNormal);
			}
			canvas.drawBitmap(mCustomBackgroundBitmap, 0, 0, p);
			return;
		}

		//裁剪中间到环形，不过有锯齿，暂时去掉
//		canvas.save();
//		canvas.translate(0, 0);
//
//		mPath.reset();
//		mPath.addCircle(mWidth / 2, mHeight / 2, mRingRadius + mRingWidth, Path.Direction.CCW);
//		canvas.clipPath(mPath, Region.Op.DIFFERENCE);
//		mPath.reset();
//		mPath.addCircle(mWidth / 2, mHeight / 2, mRingRadius, Path.Direction.CCW);
//		canvas.clipPath(mPath,Region.Op.UNION);

		// 画圆角矩形
		p.setStyle(Paint.Style.FILL);// 充满
		p.setColor(mResources.getColor(R.color.whitedot_background));
		if (mIsPressed) {
			p.setAlpha(mAlphaPressed);
		} else {
			p.setAlpha(mAlphaNormal);
		}
		p.setAntiAlias(true);// 设置画笔的锯齿效果
		RectF rectF = new RectF(0, 0, mWidth, mHeight);// 设置个新的长方形
		canvas.drawRoundRect(rectF, mXRadiusCorner, mYRadiusCorner, p);// 第二个参数是x半径，第三个参数是y半径

		//裁剪中间到环形，不过有锯齿，暂时去掉
//        canvas.restore();

		// 画圆圆环
		p.setStyle(Paint.Style.STROKE);// 设置空心
		p.setStrokeWidth(mRingWidth);
		p.setColor(Color.argb(100, 255, 255, 255));
		int left = mWidth / 2 - mRingRadius - mRingWidth / 2;
		int top = mHeight / 2 - mRingRadius - mRingWidth / 2;
		RectF oval1 = new RectF(left, top, mWidth - left, mHeight - top);
		canvas.drawArc(oval1, 0, 360, true, p);
	}

	/**
	 * set WhiteDot Background to default when bitmap is null
	 * @param bitmap
	 */
	void setWhiteDotBackground(Bitmap bitmap) {
		if (bitmap == null) {
			mEnableCustomBackground = false;
			mCustomBackgroundBitmap = null;
			return;
		}
		mEnableCustomBackground = true;
		//需要判断并在必要时进行裁剪
		mCustomBackgroundBitmap = bitmap;
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "onConfigurationChanged");
		refreshLayoutParams();
	}
	
}
