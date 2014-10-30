package com.fornut.assisttools.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;

import com.fornut.assisttools.R;
import com.fornut.assisttools.models.DevicePolicyManagerUtils;

public class QSScreenLock extends QuickSwitchBase {

	private String TAG = "AssistTools-QSScreenLock";

	public QSScreenLock(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setQuickSwitchcustomTitle(R.string.qs_name_screenlock);
	}

	@Override
	public String getQuickSwitchName() {
		// TODO Auto-generated method stub
		return QSScreenLock.class.getSimpleName();
	}

	@Override
	public void drawQuickSwitchIcon(Canvas canvas, RectF rectF, Paint paint, boolean isPressed) {
		// TODO Auto-generated method stub
		Log.d(TAG, "drawQuickSwitchIcon " + rectF.toString());
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
        RectF oval = new RectF(w * 0.35f, h * 0.1f, w * 0.65f, h * 0.4f);
        canvas.drawArc(oval, 180, 180, false, paint);

        canvas.drawLine(w * 0.65f, h * 0.25f, w * 0.65f , h * 0.4f, paint);

        RectF rect = new RectF(w * 0.25f, h * 0.4f, w * 0.75f, h * 0.85f);
        canvas.drawRect(rect, paint);

        paint.setStyle(Paint.Style.FILL);//填充
        RectF keyHoleRing = new RectF(w * 0.45f, h * 0.5f, w * 0.55f, h * 0.6f);
        canvas.drawArc(keyHoleRing, 0, 360, false, paint);

        canvas.drawLine(w * 0.5f, h * 0.6f, w * 0.5f , h * 0.7f, paint);

		canvas.restore();
	}

	@Override
	public void onClickCallback() {
		// TODO Auto-generated method stub
		if (!DevicePolicyManagerUtils.getInstance(mContext).lockScreenNow()) {
			Toast.makeText(mContext, getResources().getString(R.string.toast_app_no_active),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLongClickCallback() {
		// TODO Auto-generated method stub

	}
}
