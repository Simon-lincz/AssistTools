package com.fornut.assisttools.views;

import com.fornut.assisttools.R;
import com.fornut.assisttools.models.KeySimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class QSRecentKey extends QuickSwitchBase {

    private String TAG = "AssistTools-" + QSRecentKey.class.getSimpleName();

    public QSRecentKey(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setQuickSwitchcustomTitle(R.string.qs_name_recentkey);
    }

    @Override
    public String getQuickSwitchName() {
        // TODO Auto-generated method stub
        return QSRecentKey.class.getSimpleName();
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
        // 最外面卡片底部横线
        canvas.drawLine(w * 0.2f, h * 0.8f, w * 0.6f, h * 0.8f, paint);
        // 最外面卡片顶部横线
        canvas.drawLine(w * 0.2f, h * 0.5f, w * 0.6f, h * 0.5f, paint);
        // 最外面卡片左竖线
        canvas.drawLine(w * 0.2f, h * 0.5f, w * 0.2f, h * 0.8f, paint);
        // 最外面卡片右竖线
        canvas.drawLine(w * 0.6f, h * 0.5f, w * 0.6f, h * 0.8f, paint);

        // 中间卡片底部横线
        canvas.drawLine(w * 0.6f, h * 0.65f, w * 0.7f, h * 0.65f, paint);
        // 中间卡片顶部横线
        canvas.drawLine(w * 0.3f, h * 0.35f, w * 0.7f, h * 0.35f, paint);
        // 中间卡片左竖线
        canvas.drawLine(w * 0.3f, h * 0.35f, w * 0.3f, h * 0.5f, paint);
        // 中间卡片右竖线
        canvas.drawLine(w * 0.7f, h * 0.35f, w * 0.7f, h * 0.65f, paint);

        // 最后面卡片底部横线
        canvas.drawLine(w * 0.7f, h * 0.5f, w * 0.8f, h * 0.5f, paint);
        // 最后面卡片顶部横线
        canvas.drawLine(w * 0.4f, h * 0.2f, w * 0.8f, h * 0.2f, paint);
        // 最后面卡片左竖线
        canvas.drawLine(w * 0.4f, h * 0.2f, w * 0.4f, h * 0.35f, paint);
        // 最后面卡片右竖线
        canvas.drawLine(w * 0.8f, h * 0.2f, w * 0.8f, h * 0.5f, paint);

        canvas.restore();
    }

    @Override
    public void onClickCallback() {
        // TODO Auto-generated method stub
        KeySimulator.getInstance(mContext).toggleRecents();
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