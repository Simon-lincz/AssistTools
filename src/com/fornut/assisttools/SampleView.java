package com.fornut.assisttools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.View;

public class SampleView extends View {
    private Paint mPaint;
    private Path mPath;
	private Path p;

    public SampleView(Context context) {
        super(context);
        setFocusable(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);
        mPaint.setTextSize(16);
        mPaint.setTextAlign(Paint.Align.RIGHT);

        mPath = new Path();
        p = new Path();
    }

    private void drawScene(Canvas canvas) {
        canvas.clipRect(0, 0, 100, 100);
        
        canvas.drawColor(Color.WHITE);

        mPaint.setColor(Color.RED);
        canvas.drawLine(0, 0, 100, 100, mPaint);

        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(30, 70, 30, mPaint);

        mPaint.setColor(Color.BLUE);
        canvas.drawText("Clipping", 100, 30, mPaint);
    }

    //DIFFERENCE是第一次不同于第二次的部分显示出来 
    //REPLACE是显示第二次的 
    //REVERSE_DIFFERENCE 是第二次不同于第一次的部分显示 
    //INTERSECT交集显示 
    //UNION全部显示 
    //XOR补集 就是AB全集的减去交集剩余部分显示
    
    @Override protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);

        canvas.save();
        canvas.translate(10, 10);
        drawScene(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(160, 10);
        canvas.clipRect(10, 10, 90, 90); //这个范围里面的显示出来
        canvas.clipRect(30, 30, 70, 70, Region.Op.DIFFERENCE); //这个范围里面的隐藏掉
        drawScene(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(10, 160);
        /*/
        //保留中间的圆形
        mPath.reset();
        canvas.clipPath(mPath); // makes the clip empty
        mPath.addCircle(50, 50, 40, Path.Direction.CCW);
        canvas.clipPath(mPath, Region.Op.REPLACE);
        /*/ 
        //图中抠出一个圆环
        mPath.reset();
        mPath.addCircle(50, 50, 40, Path.Direction.CCW);
        canvas.clipPath(mPath, Region.Op.DIFFERENCE);
        p.reset();
        p.addCircle(50, 50, 30, Path.Direction.CCW);
        canvas.clipPath(p,Region.Op.UNION);
        //*/
        drawScene(canvas);
        canvas.restore();
        
        //用于填充上面抠出圆环的地方
        canvas.save();
        canvas.translate(10, 160);
        mPaint.setStyle(Paint.Style.STROKE);// 设置空心
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.argb(150, 255, 0, 255));
        RectF oval1 = new RectF(15, 15, 85, 85);
        canvas.drawArc(oval1, 0, 360, true, mPaint);// 小弧形
        canvas.restore();
        
        canvas.save();
        canvas.translate(160, 160);
        canvas.clipRect(0, 0, 60, 60);
        canvas.clipRect(40, 40, 100, 100, Region.Op.UNION);
        drawScene(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(10, 310);
        /*/
        canvas.clipRect(0, 0, 60, 60);
        canvas.clipRect(40, 40, 100, 100, Region.Op.XOR);
        /*/ 
        //环形
        mPath.reset();
        p.reset();
        mPath.addCircle(50, 50, 40, Path.Direction.CCW);
        canvas.clipPath(mPath);
        p.addCircle(50, 50, 30, Path.Direction.CCW);
        canvas.clipPath(p, Region.Op.XOR);
        //*/
        drawScene(canvas);
        canvas.restore();
        
        canvas.save();
        canvas.translate(160, 310);
        canvas.clipRect(0, 0, 60, 60);
        canvas.clipRect(40, 40, 100, 100, Region.Op.REVERSE_DIFFERENCE);
        drawScene(canvas);
        canvas.restore();
    }
}