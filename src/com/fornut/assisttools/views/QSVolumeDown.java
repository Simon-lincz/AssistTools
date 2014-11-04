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
import com.fornut.assisttools.models.KeySimulator;

public class QSVolumeDown extends QuickSwitchBase {

	private String TAG = "AssistTools-" + QSVolumeDown.class.getSimpleName();

	public QSVolumeDown(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setQuickSwitchcustomTitle(R.string.qs_name_volumedown);
	}

	@Override
	public String getQuickSwitchName() {
		// TODO Auto-generated method stub
		return QSVolumeDown.class.getSimpleName();
	}

	@Override
	public void drawQuickSwitchIcon(Canvas canvas, RectF rectF, Paint paint,
			boolean isPressed) {
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

		// 磁铁竖线1
		canvas.drawLine(w * 0.2f, h * 0.4f, w * 0.2f, h * 0.6f, paint);
		// 磁铁竖线2
		canvas.drawLine(w * 0.3f, h * 0.4f, w * 0.3f, h * 0.6f, paint);
		// 磁铁横线1
		canvas.drawLine(w * 0.2f, h * 0.4f, w * 0.3f, h * 0.4f, paint);
		// 磁铁横线2
		canvas.drawLine(w * 0.2f, h * 0.6f, w * 0.3f, h * 0.6f, paint);
		// 喇叭口上斜线
		canvas.drawLine(w * 0.3f, h * 0.4f, w * 0.55f, h * 0.2f, paint);
		// 喇叭口下斜线
		canvas.drawLine(w * 0.3f, h * 0.6f, w * 0.55f, h * 0.8f, paint);
		// 喇叭口竖线
		canvas.drawLine(w * 0.55f, h * 0.2f, w * 0.55f, h * 0.8f, paint);
		// UP的横线
		canvas.drawLine(w * 0.65f, h * 0.5f, w * 0.8f, h * 0.5f, paint);
		// UP的竖线
//		canvas.drawLine(w * 0.8f, h * 0.4f, w * 0.8f, h * 0.6f, paint);

		canvas.restore();
	}

	@Override
	public void onClickCallback() {
		// TODO Auto-generated method stub
		KeySimulator.getInstance(mContext).setVolumeDown();
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
