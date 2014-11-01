package com.fornut.assisttools.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class QSEmpty extends QuickSwitchBase {

	private String TAG = "AssistTools-"+QSEmpty.class.getSimpleName();

	public QSEmpty(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setQuickSwitchcustomTitle("");
	}

	@Override
	public String getQuickSwitchName() {
		// TODO Auto-generated method stub
		return QSEmpty.class.getSimpleName();
	}

	@Override
	public void drawQuickSwitchIcon(Canvas canvas, RectF rectF, Paint paint, boolean isPressed) {
		// TODO Auto-generated method stub
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
