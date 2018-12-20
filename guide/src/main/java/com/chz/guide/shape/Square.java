package com.chz.guide.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.chz.guide.view.GuideView;

/**
 * Created by chz
 * on 2018/11/20
 */
public class Square extends GuideShape {

    private Path scrollPath;
    private float xLB, xLT, xRT, xRB;
    private float yLB, yLT, yRT, yRB;
    private float focusX;
    private float focusY;
    private float shapeRadius;
    private float[] pts;
    private float diagonalRadius;
    private float marginTop;

    public Square(View view) {
        super(view);
    }

    @Override
    protected void initPaintTools() {
        if (mMode == GuideView.MODE_SCROLL) {
            scrollPath = new Path();
            diagonalRadius = (float) (mIndexSize / Math.cos(45 * PI));
            marginTop = diagonalRadius - mIndexSize;
            unitAngle = SQUARE_UNIT_ANGLE;
            updatePath(scrollPath, 0);
        }
        focusX = focusY = shapeRadius = mIndexSize / 2;
        pts = new float[mIndexCount * 2];
        for (int i = 0; i < mIndexCount; i++) {
            pts[i * 2] = shapeRadius + i * unitSize;
            pts[i * 2 + 1] = shapeRadius + marginTop;
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
        switch (mMode) {
            case GuideView.MODE_ALPHA:
                canvas.drawPoint(focusX, focusY, focusPaint);
                if (isReverse) {
                    canvas.drawPoint(focusX + mIndexSize + mDistanceSize, focusY, focusPaintNormal);
                } else {
                    canvas.drawPoint(focusX - mIndexSize - mDistanceSize, focusY, focusPaintNormal);
                }
                break;
            case GuideView.MODE_SCROLL:
                canvas.drawPath(scrollPath, focusPaint);
                break;
            default:
                canvas.drawPoint(focusX, focusY, focusPaint);
                break;
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
    protected void scroll() {
        if (isReverse) {
            xLB = (float) (xRB + mIndexSize * Math.cos((180 - sweepAngle) * PI));
            yLB = (float) (yRB - mIndexSize * Math.sin((180 - sweepAngle) * PI));
            xLT = (float) (xRB + diagonalRadius * Math.cos((135 - sweepAngle) * PI));
            yLT = (float) (yRB - diagonalRadius * Math.sin((135 - sweepAngle) * PI));
            xRT = (float) (xRB + mIndexSize * Math.cos((90 - sweepAngle) * PI));
            yRT = (float) (yRB - mIndexSize * Math.sin((90 - sweepAngle) * PI));
        } else {
            xLT = (float) (xLB + mIndexSize * Math.cos((180 - sweepAngle) * PI));
            yLT = (float) (yLB - mIndexSize * Math.sin((180 - sweepAngle) * PI));
            xRT = (float) (xLB + diagonalRadius * Math.cos((135 - sweepAngle) * PI));
            yRT = (float) (yLB - diagonalRadius * Math.sin((135 - sweepAngle) * PI));
            xRB = (float) (xLB + mIndexSize * Math.cos((90 - sweepAngle) * PI));
            yRB = (float) (yLB - mIndexSize * Math.sin((90 - sweepAngle) * PI));
        }
        updatePath(scrollPath, 0);
    }

    @Override
    protected void updatePosition() {
        if (mMode == GuideView.MODE_SCROLL) {
            scrollSize = currentPosition * unitSize + currentScrollPosition * mIndexSize;
            xLB = scrollSize;
            yLB = diagonalRadius;
            xLT = scrollSize;
            yLT = marginTop;
            xRT = mIndexSize + scrollSize;
            yRT = marginTop;
            xRB = mIndexSize + scrollSize;
            yRB = diagonalRadius;
            updatePath(scrollPath, 0);
        } else {
            focusX = shapeRadius + currentPosition * unitSize;
        }
    }

    private void updatePath(Path path, float value) {
        path.rewind();
        path.moveTo(xLB + value, yLB);
        path.lineTo(xLT + value, yLT);
        path.lineTo(xRT + value, yRT);
        path.lineTo(xRB + value, yRB);
        path.close();
    }
}
