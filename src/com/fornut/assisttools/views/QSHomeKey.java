package com.fornut.assisttools.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.fornut.assisttools.R;
import com.fornut.assisttools.models.DropzoneManager;
import com.fornut.assisttools.models.KeySimulator;

public class QSHomeKey extends QuickSwitchBase {

    private String TAG = "AssistTools-" + QSHomeKey.class.getSimpleName();

    public QSHomeKey(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setQuickSwitchcustomTitle(R.string.qs_name_homekey);
    }

    @Override
    public String getQuickSwitchName() {
        // TODO Auto-generated method stub
        return QSHomeKey.class.getSimpleName();
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

        // 屋顶左斜线
        canvas.drawLine(w * 0.5f, h * 0.2f, w * 0.2f, h * 0.5f, paint);
        // 屋檐左横线
        canvas.drawLine(w * 0.2f, h * 0.5f, w * 0.3f, h * 0.5f, paint);
        // 左墙壁
        canvas.drawLine(w * 0.3f, h * 0.5f, w * 0.3f, h * 0.8f, paint);
        // 地板
        canvas.drawLine(w * 0.3f, h * 0.8f, w * 0.7f, h * 0.8f, paint);
        // 右墙壁
        canvas.drawLine(w * 0.7f, h * 0.8f, w * 0.7f, h * 0.5f, paint);
        // 屋檐右横线
        canvas.drawLine(w * 0.7f, h * 0.5f, w * 0.8f, h * 0.5f, paint);
        // // 屋顶右斜线
        // canvas.drawLine(w * 0.8f, h * 0.5f, w * 0.5f, h * 0.2f, paint);
        // 屋顶右下斜线
        canvas.drawLine(w * 0.8f, h * 0.5f, w * 0.7f, h * 0.4f, paint);
        // 烟囱右竖线
        canvas.drawLine(w * 0.7f, h * 0.4f, w * 0.7f, h * 0.25f, paint);
        // 烟囱横线
        canvas.drawLine(w * 0.7f, h * 0.25f, w * 0.65f, h * 0.25f, paint);
        // 烟囱左竖线
        canvas.drawLine(w * 0.65f, h * 0.25f, w * 0.65f, h * 0.35f, paint);
        // 屋顶右上斜线
        canvas.drawLine(w * 0.65f, h * 0.35f, w * 0.5f, h * 0.2f, paint);

        canvas.restore();
    }

    @Override
    public void onClickCallback() {
        // TODO Auto-generated method stub
        KeySimulator.getInstance(mContext).goHomeLauncher();
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
