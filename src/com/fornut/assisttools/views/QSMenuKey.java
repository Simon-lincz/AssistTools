package com.fornut.assisttools.views;

import com.fornut.assisttools.R;
import com.fornut.assisttools.models.KeySimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class QSMenuKey extends QuickSwitchBase {

	private String TAG = "AssistTools-"+QSMenuKey.class.getSimpleName();

	public QSMenuKey(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setQuickSwitchcustomTitle(R.string.qs_name_menukey);
	}

	@Override
	public String getQuickSwitchName() {
		// TODO Auto-generated method stub
		return QSMenuKey.class.getSimpleName();
	}

	@Override
	public void drawQuickSwitchIcon(Canvas canvas, RectF rectF, Paint paint, boolean isPressed) {
		// TODO Auto-generated method stub
		int w = (int) (rectF.right - rectF.left);
		int h = (int) (rectF.bottom - rectF.top);
		canvas.save();
		canvas.translate(rectF.left, rectF.top);
		float scale = 0.9f; // 画完后整体的缩放
		canvas.scale(scale, scale, w / 2, h / 2);

		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);
		paint.setStyle(Paint.Style.STROKE);// 只画边框
		paint.setColor(Color.WHITE);
		if (isPressed) {
			paint.setAlpha(mAlphaPressed);
		} else {
			paint.setAlpha(mAlphaNormal);
		}
		canvas.drawLine(w * 0.25f, h * 0.2f, w * 0.75f, h * 0.2f, paint);
		canvas.drawLine(w * 0.25f, h * 0.2f, w * 0.25f, h * 0.8f, paint);
		canvas.drawLine(w * 0.75f, h * 0.2f, w * 0.75f, h * 0.8f, paint);

		canvas.drawLine(w * 0.45f, h * 0.35f, w * 0.67f, h * 0.35f, paint);
		canvas.drawLine(w * 0.45f, h * 0.5f, w * 0.67f, h * 0.5f, paint);
		canvas.drawLine(w * 0.45f, h * 0.65f, w * 0.67f, h * 0.65f, paint);

		paint.setStrokeWidth(6);
		canvas.drawPoint(w * 0.35f , h * 0.35f, paint);
		canvas.drawPoint(w * 0.35f , h * 0.5f, paint);
		canvas.drawPoint(w * 0.35f , h * 0.65f, paint);
		canvas.restore();
	}

	@Override
	public void onClickCallback() {
		// TODO Auto-generated method stub
		KeySimulator.getInstance(mContext).showMenu();
	}

	@Override
	public void onLongClickCallback() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isQuickSwitchSupported() {
		// TODO Auto-generated method stub
		return true;
	}
}