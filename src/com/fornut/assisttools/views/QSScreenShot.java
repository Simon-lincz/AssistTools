package com.fornut.assisttools.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;

import com.fornut.assisttools.R;

public class QSScreenShot extends QuickSwitchBase {

    private String TAG = "AssistTools-" + QSScreenShot.class.getSimpleName();

    public QSScreenShot(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setQuickSwitchcustomTitle(R.string.qs_name_screenshot);
    }

    @Override
    public String getQuickSwitchName() {
        // TODO Auto-generated method stub
        return QSScreenShot.class.getSimpleName();
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
        if (isPressed) {
            paint.setAlpha(mAlphaPressed);
        } else {
            paint.setAlpha(mAlphaNormal);
        }
        Path path = new Path();

        // 剪刀
        canvas.drawCircle(w * 0.75f, h * 0.55f, w * 0.05f, paint);
        canvas.drawCircle(w * 0.75f, h * 0.75f, w * 0.05f, paint);
        canvas.drawCircle(w * 0.65f, h * 0.65f, 4, paint);
        canvas.drawLine(w * 0.75f, h * 0.6f, w * 0.35f, h * 0.8f, paint);
        canvas.drawLine(w * 0.75f, h * 0.7f, w * 0.35f, h * 0.5f, paint);

        PathEffect effects = new DashPathEffect(new float[] { w * 0.05f,
                w * 0.05f }, 1);
        paint.setPathEffect(effects);
        // 上
        path.moveTo(w * 0.2f, h * 0.2f);
        path.lineTo(w * 0.75f, h * 0.2f);
        canvas.drawPath(path, paint);
        // 左
        path.moveTo(w * 0.2f, h * 0.2f);
        path.lineTo(w * 0.2f, h * 0.65f);
        canvas.drawPath(path, paint);
        // 右
        path.moveTo(w * 0.75f, h * 0.2f);
        path.lineTo(w * 0.75f, h * 0.5f);
        canvas.drawPath(path, paint);
        // 下
        path.moveTo(w * 0.2f, h * 0.65f);
        path.lineTo(w * 0.65f, h * 0.65f);
        canvas.drawPath(path, paint);
        paint.setPathEffect(null);
        canvas.restore();
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