package com.fornut.assisttools.views;

import com.fornut.assisttools.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class QSNightMode extends QuickSwitchBase {

	private String TAG = "AssistTools-"+QSNightMode.class.getSimpleName();

	public QSNightMode(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setQuickSwitchcustomTitle(R.string.qs_name_nightmode);
	}

	@Override
	public String getQuickSwitchName() {
		// TODO Auto-generated method stub
		return QSNightMode.class.getSimpleName();
	}

	@Override
	public void drawQuickSwitchIcon(Canvas canvas, RectF rectF, Paint paint, boolean isPressed) {
		// TODO Auto-generated method stub
		int w = (int) (rectF.right - rectF.left);
		int h = (int) (rectF.bottom - rectF.top);
		canvas.save();
		canvas.translate(rectF.left, rectF.top);
		float scale = 0.9f; //画完后整体的缩放
		canvas.scale(scale, scale, w / 2, h / 2);

		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);
		paint.setStyle(Paint.Style.STROKE);//只画边框
		paint.setColor(Color.WHITE);
		if (isPressed) {
			paint.setAlpha(mAlphaPressed);
		} else {
			paint.setAlpha(mAlphaNormal);
		}
		float radius = w * 0.3f;
		float[] oval_o = {w * 0.5f, h * 0.5f};
		// 月牙外环
		RectF oval = new RectF(oval_o[0] - radius, oval_o[1] - radius, oval_o[0] + radius, oval_o[1] + radius);
		canvas.drawArc(oval, -45, 180, false, paint);
//		canvas.drawArc(oval, -45, 360, false, paint);
		float ring_angle = 45;
		float radius_1 = (float) Math.abs(radius / Math.sin(Math.toRadians(ring_angle)));
		float x = (float) (oval_o[0] - Math.sqrt((Math.pow(radius / Math.tan(Math.toRadians(ring_angle)), 2)) / 2));
		float[] oval_o_1 = {x, x};
		// 月牙内环
		RectF oval1 = new RectF(oval_o_1[0] - radius_1, oval_o_1[1] - radius_1, oval_o_1[0] + radius_1, oval_o_1[1] + radius_1);
		canvas.drawArc(oval1, 90 - ring_angle - 45, ring_angle * 2, false, paint);

		drawStar(canvas, paint, w * 0.3f, h * 0.3f , w * 0.1f);
		paint.setStrokeWidth(2);
		drawStar(canvas, paint, w * 0.45f, h * 0.45f , w * 0.09f);
		drawStar(canvas, paint, w * 0.55f, h * 0.23f , w * 0.06f);
		drawStar(canvas, paint, w * 0.24f, h * 0.5f , w * 0.08f);
//		drawStar(canvas, paint, w * 0.2f, h * 0.65f , w * 0.04f);
//		drawStar(canvas, paint, w * 0.3f, h * 0.6f , w * 0.04f);
//		drawStar(canvas, paint, w * 0.2f, h * 0.4f , w * 0.04f);

		// 云线
		// 上短下长
//		canvas.drawLine(w * 0.4f, h * 0.82f, w * 0.6f, h * 0.82f, paint);
//		canvas.drawLine(w * 0.2f, h * 0.84f, w * 0.8f, h * 0.84f, paint);
		// 上长下短
//		canvas.drawLine(w * 0.2f, h * 0.82f, w * 0.8f, h * 0.82f, paint);
//		canvas.drawLine(w * 0.6f, h * 0.84f, w * 0.75f, h * 0.84f, paint);
		canvas.restore();
	}

	void drawStar(Canvas canvas, Paint paint,float x, float y, float length) {
		canvas.drawLine(x - length / 2, y, x + length / 2, y, paint);
		canvas.drawLine(x, y - length / 2, x, y + length / 2, paint);
	}

	@Override
	public void onClickCallback() {
		// TODO Auto-generated method stub
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