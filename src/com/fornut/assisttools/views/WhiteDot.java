package com.fornut.assisttools.views;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
	private int mBackground_strokeWidth = 0;

	private RectF mWhiteDotBackground_Rect;
	private RectF mWhiteDotRing_Rect;

	private Bitmap mCustomBackgroundBitmap;
	private boolean mEnableCustomBackground = false;
	private Rect mCustomBackgroundBitmap_Rect;

	private Paint mPaint;

	private ValueAnimator mMoveAnimation;
	private int mAnimationDuration = 300;
	private int mMoveDeltaX = 0, mMoveDeltaY = 0;

	public WhiteDot(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		mContext = context;
		mResources = getResources();
		mPaint = new Paint();

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
		mAnimationDuration = mResources.getInteger(R.integer.whitedot_animation_duration);
		mBackground_strokeWidth = mResources.getDimensionPixelSize(R.dimen.background_FrameStrokeWidth);
		mWhiteDotBackground_Rect = new RectF(mBackground_strokeWidth, mBackground_strokeWidth,
				mWidth - mBackground_strokeWidth, mHeight - mBackground_strokeWidth);
		int left = mWidth / 2 - mRingRadius - mRingWidth / 2;
		int top = mHeight / 2 - mRingRadius - mRingWidth / 2;
		mWhiteDotRing_Rect = new RectF(left, top, mWidth - left, mHeight - top);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
		canvas.save();
		canvas.translate(getTranslationX(), getTranslationY());

		mPaint.setStrokeWidth(mBackground_strokeWidth);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(mResources.getColor(R.color.background_FrameStroke));
		mPaint.setAntiAlias(true);
		canvas.drawRoundRect(mWhiteDotBackground_Rect, mXRadiusCorner, mYRadiusCorner, mPaint);// 第二个参数是x半径，第三个参数是y半径

		if (mEnableCustomBackground && mCustomBackgroundBitmap != null) {
			if (mIsPressed) {
				mPaint.setAlpha(mAlphaPressed);
			} else {
				mPaint.setAlpha(mAlphaNormal);
			}
			canvas.drawBitmap(mCustomBackgroundBitmap, mCustomBackgroundBitmap_Rect , mWhiteDotBackground_Rect, mPaint);
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
		mPaint.setStyle(Paint.Style.FILL);// 充满
		mPaint.setColor(mResources.getColor(R.color.whitedot_background));
		if (mIsPressed) {
			mPaint.setAlpha(mAlphaPressed);
		} else {
			mPaint.setAlpha(mAlphaNormal);
		}
		mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
		canvas.drawRoundRect(mWhiteDotBackground_Rect, mXRadiusCorner, mYRadiusCorner, mPaint);// 第二个参数是x半径，第三个参数是y半径

		//裁剪中间到环形，不过有锯齿，暂时去掉
//        canvas.restore();

		// 画圆圆环
		mPaint.setStyle(Paint.Style.STROKE);// 设置空心
		mPaint.setStrokeWidth(mRingWidth);
		mPaint.setColor(Color.argb(100, 255, 255, 255));
		canvas.drawArc(mWhiteDotRing_Rect, 0, 360, true, mPaint);

		canvas.restore();
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
		mCustomBackgroundBitmap_Rect = new Rect(0, 0, mCustomBackgroundBitmap.getWidth(),
				mCustomBackgroundBitmap.getHeight());
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "onConfigurationChanged");
		refreshLayoutParams();
	}

	public void showMoveAnimation(int ox, int oy, int tx, int ty) {
		if (mMoveAnimation == null) {
			mMoveAnimation = ValueAnimator.ofFloat(-0.07f, 0);
			mMoveAnimation.setDuration(mAnimationDuration);
		}
		mMoveDeltaX = ox - tx;
		mMoveDeltaY = oy - ty;
		Log.d(TAG, "showMoveAnimation " + mMoveDeltaX + " " + mMoveDeltaY);
		if (Math.max(Math.abs(mMoveDeltaX), Math.abs(mMoveDeltaY)) < 100) {
			return;
		}
		mMoveAnimation.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float f = (Float) animation.getAnimatedValue();
				Log.d(TAG, "onAnimationUpdate " + f);
				WhiteDot.this.setTranslationX(f * mMoveDeltaX);
				WhiteDot.this.setTranslationY(f * mMoveDeltaY);
			}
		});
		mMoveAnimation.start();
	}
}
