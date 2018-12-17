package com.chz.guide.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.chz.guide.view.GuideView;

/**
 * Created by chz
 * on 2018/11/20
 */
public class Square extends GuideShape {

    private float focusX;
    private float focusY;
    private float shapeRadius;
    private float[] pts;

    public Square(View view) {
        super(view);
    }

    @Override
    protected void initPaintTools() {
        focusX = focusY = shapeRadius = mIndexSize / 2;
        pts = new float[mIndexCount * 2];
        for (int i = 0; i < mIndexCount; i++) {
            pts[i * 2] = shapeRadius + i * (mIndexSize + mDistanceSize);
            pts[i * 2 + 1] = shapeRadius;
        }
        normalPaint.setStrokeWidth(mIndexSize);
        focusPaint.setStrokeWidth(mIndexSize);
        normalPaint.setStrokeCap(Paint.Cap.SQUARE);
        focusPaint.setStrokeCap(Paint.Cap.SQUARE);
        if (mMode == GuideView.MODE_ALPHA) {
            focusPaintNormal.setStrokeWidth(mIndexSize);
            focusPaintNormal.setStrokeCap(Paint.Cap.SQUARE);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPoints(pts, normalPaint);
        canvas.drawPoint(focusX, focusY, focusPaint);
        if (mMode == GuideView.MODE_ALPHA) {
            if (isReverse) {
                    canvas.drawPoint(focusX + mIndexSize + mDistanceSize, focusY, focusPaintNormal);
                } else {
                    canvas.drawPoint(focusX - mIndexSize - mDistanceSize, focusY, focusPaintNormal);
                }
        }
    }


    @Override
    protected void change() {
       updatePosition();
    }

    @Override
    protected void slide(float value) {
        focusX = shapeRadius + value;
    }

    @Override
    protected void scroll(int position, float positionOffset) {

    }

    @Override
    protected void updatePosition() {
        focusX = shapeRadius + currentPosition*(mIndexSize+mDistanceSize);
    }
}
