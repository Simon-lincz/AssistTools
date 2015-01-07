package com.fornut.assisttools.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.fornut.assisttools.R;

public class QSScreenMask extends QuickSwitchBase {

    private String TAG = "AssistTools-" + QSScreenMask.class.getSimpleName();

    private ScreenMaskListener mListener;
    private boolean mScreenMaskEnable = false;

    public QSScreenMask(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setQuickSwitchcustomTitle(R.string.qs_name_screenmask);
    }

    @Override
    public String getQuickSwitchName() {
        // TODO Auto-generated method stub
        return QSScreenMask.class.getSimpleName();
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
        float radius = w * 0.15f;
        float distanceBetweenTwoGlass = w * 0.4f;// 圆心距离
        float[] oval_o = { w * 0.3f, h * 0.65f };

        // 左镜片
        RectF oval = new RectF(oval_o[0] - radius, oval_o[1] - radius,
                oval_o[0] + radius, oval_o[1] + radius);
        canvas.drawArc(oval, 0, 360, false, paint);
        float[] tmp_point = { oval.right, oval_o[1] };

        // 右镜片
        oval.offset(distanceBetweenTwoGlass, 0);
        canvas.drawArc(oval, 0, 360, false, paint);

        // 左镜脚
        canvas.drawLine(oval_o[0] - radius, oval_o[1], oval_o[0], w * 0.2f,
                paint);
        // 右镜脚
        canvas.drawLine(oval.right, oval_o[1], oval.centerX(), w * 0.2f, paint);

        // 鼻梁
        float ring_angle = 40;
        float tmp = oval.left - tmp_point[0];
        float radius_1 = (float) Math.abs(tmp / 2f
                / Math.sin(Math.toRadians(ring_angle)));
        float[] oval_o_1 = {
                tmp_point[0] + tmp / 2,
                (float) (oval_o[1] + Math.sqrt(Math.pow(radius_1, 2)
                        - Math.pow(tmp / 2, 2))) };
        RectF oval1 = new RectF(oval_o_1[0] - radius_1, oval_o_1[1] - radius_1,
                oval_o_1[0] + radius_1, oval_o_1[1] + radius_1);

        canvas.drawArc(oval1, -90 - ring_angle, ring_angle * 2, false, paint);

        if (mScreenMaskEnable) {// 镜片反光
            paint.setStrokeWidth(4);
            float startAngle = -70, sweepAngle = 60;
            float scale_1 = 0.7f; // 画完后整体的缩放
            canvas.save();
            canvas.scale(scale_1, scale_1, oval.centerX(), oval.centerY());
            canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
            canvas.restore();

            canvas.save();
            oval.offset(-1 * distanceBetweenTwoGlass, 0);
            canvas.scale(scale_1, scale_1, oval.centerX(), oval.centerY());
            canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
            canvas.restore();
        }

        canvas.restore();
    }

    @Override
    public void onClickCallback() {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.switchStatus();
        }
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

    public void setScreenMaskEnable(boolean enable) {
        mScreenMaskEnable = enable;
        invalidate();
    }

    public void setScreenMaskListener(ScreenMaskListener l) {
        mListener = l;
    }

    public interface ScreenMaskListener {
        public void switchStatus();
    }
}