package com.fornut.assisttools.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.fornut.assisttools.R;

public class ControlBoard extends View{
	
	static boolean DBG = false;
	static String TAG = "AssistTools-WhiteDot";
	
	Context mContext;

	public ControlBoard(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		mContext = context;
		setBackgroundResource(R.drawable.ic_launcher);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(MeasureSpec.makeMeasureSpec(200, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(200, MeasureSpec.EXACTLY));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint p = new Paint();
		
		// 画圆角矩形
		p.setStyle(Paint.Style.FILL);// 充满
		p.setColor(Color.argb(150, 123, 123, 123));
		p.setAntiAlias(true);// 设置画笔的锯齿效果
//		canvas.drawText("画圆角矩形:", 10, 260, p);
		RectF oval3 = new RectF(0, 0, 150, 150);// 设置个新的长方形
		canvas.drawRoundRect(oval3, 40, 40, p);// 第二个参数是x半径，第三个参数是y半径

		// 画笑脸弧线
		p.setStyle(Paint.Style.STROKE);// 设置空心
		p.setStrokeWidth(6);
		p.setColor(Color.argb(50, 255, 255, 255));
		RectF oval1 = new RectF(20, 20, 130, 130);
		canvas.drawArc(oval1, 0, 360, true, p);// 小弧形
		
		// 画图片，就是贴图
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		canvas.drawBitmap(bitmap, 250, 360, p);
	}
	
	
}
