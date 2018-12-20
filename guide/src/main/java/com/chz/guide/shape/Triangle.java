package com.chz.guide.shape;

import android.graphics.Canvas;
import android.graphics.Path;
import android.view.View;

import com.chz.guide.view.GuideView;

/**
 * Created by chz
 * on 2018/11/24
 */
public class Triangle extends GuideShape {

    private Path normalPath;
    private Path focusPath;
    private Path focusPathNormal;
    private float xLeft, yLeft, xTop, yTop, xRight, yRight;
    private float triangleHeight;
    private float marginTop;

    public Triangle(View view) {
        super(view);
    }

    @Override
    protected void initPaintTools() {
        triangleHeight = (float) (mIndexSize * Math.sin(60 * PI));
        if (mMode == GuideView.MODE_SCROLL) {
            marginTop = mIndexSize - triangleHeight;
            unitAngle = TRIANGLE_UNIT_ANGLE;
        }
        focusPath = new Path();
        updatePosition();
        normalPath = new Path();
        Path temporaryPath = new Path();
        updatePath(temporaryPath, 0);
        for (int i = 0; i < mIndexCount; i++) {
            normalPath.addPath(temporaryPath, i * unitSize, 0);
        }
        if (mMode == GuideView.MODE_ALPHA) {
            focusPathNormal = new Path();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPath(normalPath, normalPaint);
        canvas.drawPath(focusPath, focusPaint);
        switch (mMode) {
            case GuideView.MODE_ALPHA:
                if (isReverse) {
                    updatePath(focusPathNormal, unitSize);
                } else {
                    updatePath(focusPathNormal, -unitSize);
                }
                canvas.drawPath(focusPathNormal, focusPaintNormal);
                break;
        }
    }

    @Override
    protected void change() {
        updatePosition();
    }

    @Override
    protected void slide(float value) {
        xLeft = value;
        xTop = mIndexSize / 2 + value;
        xRight = mIndexSize + value;
        updatePath(focusPath, 0);
    }

    @Override
    protected void scroll() {
        if (isReverse) {
            xLeft = (float) (xRight + mIndexSize * Math.cos((180 - sweepAngle) * PI));
            yLeft = (float) (yRight - mIndexSize * Math.sin((180 - sweepAngle) * PI));
            xTop = (float) (xRight + mIndexSize * Math.cos((120 - sweepAngle) * PI));
            yTop = (float) (yRight - mIndexSize * Math.sin((120 - sweepAngle) * PI));
        } else {
            xTop = (float) (xLeft + mIndexSize * Math.cos((180 - sweepAngle) * PI));
            yTop = (float) (yLeft - mIndexSize * Math.sin((180 - sweepAngle) * PI));
            xRight = (float) (xLeft + mIndexSize * Math.cos((120 - sweepAngle) * PI));
            yRight = (float) (yLeft - mIndexSize * Math.sin((120 - sweepAngle) * PI));
        }
        updatePath(focusPath, 0);
    }

    @Override
    protected void updatePosition() {
        scrollSize = currentPosition * unitSize + currentScrollPosition * mIndexSize;
        xLeft = scrollSize;
        yLeft = triangleHeight + marginTop;
        xTop = mIndexSize / 2 + scrollSize;
        yTop = marginTop;
        xRight = mIndexSize + scrollSize;
        yRight = triangleHeight + marginTop;
        updatePath(focusPath, 0);
    }

    private void updatePath(Path path, float value) {
        path.rewind();
        path.moveTo(xLeft + value, yLeft);
        path.lineTo(xTop + value, yTop);
        path.lineTo(xRight + value, yRight);
        path.close();
    }
}

