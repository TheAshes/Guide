package com.chz.guide.shape;

import android.graphics.Canvas;
import android.graphics.Path;
import android.view.View;

import com.chz.guide.view.GuideView;

/**
 * Created by chz
 * on 2018/11/24
 */
public class Diamond extends GuideShape {

    private float xLeft, yLeft, xTop, yTop, xRight, yRight, xBottom, yBottom;

    private Path normalPath;
    private Path focusPath;
    private Path focusPathNormal;

    public Diamond(View view) {
        super(view);
    }

    @Override
    protected void initPaintTools() {
        xLeft = 0;
        yLeft = mIndexSize / 2;
        xTop = mIndexSize / 2;
        yTop = 0;
        xRight = mIndexSize;
        yRight = mIndexSize / 2;
        xBottom = mIndexSize / 2;
        yBottom = mIndexSize;
        focusPath = new Path();
        refreshPath(focusPath, 0);
        if (mMode == GuideView.MODE_ALPHA) {
            focusPathNormal = new Path();
            refreshPath(focusPathNormal, mIndexSize + mDistanceSize);
        }
        normalPath = new Path();
        for (int i = 0; i < mIndexCount; i++) {
            normalPath.addPath(focusPath, i * (mIndexSize + mDistanceSize), 0);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPath(normalPath, normalPaint);
        canvas.drawPath(focusPath, focusPaint);
        if (mMode == GuideView.MODE_ALPHA) {
            if (isReverse) {
                refreshPath(focusPathNormal, mIndexSize + mDistanceSize);
            } else {
                refreshPath(focusPathNormal, -(mIndexSize + mDistanceSize));
            }
            canvas.drawPath(focusPathNormal, focusPaintNormal);
        }
    }

    @Override
    protected void change() {
        updatePosition();
    }

    @Override
    protected void slide(float value) {
        xLeft = value;
        xTop = xBottom = mIndexSize / 2 + value;
        xRight = mIndexSize + value;
        refreshPath(focusPath, 0);
    }

    @Override
    protected void scroll(int position, float positionOffset) {

    }

    @Override
    protected void updatePosition() {
        xLeft = currentPosition * (mIndexSize + mDistanceSize);
        xTop = xBottom = mIndexSize / 2 + currentPosition * (mIndexSize + mDistanceSize);
        xRight = mIndexSize + currentPosition * (mIndexSize + mDistanceSize);
        refreshPath(focusPath, 0);
    }

    private void refreshPath(Path path, float value) {
        path.reset();
        path.moveTo(xLeft + value, yLeft);
        path.lineTo(xTop + value, yTop);
        path.lineTo(xRight + value, yRight);
        path.lineTo(xBottom + value, yBottom);
        path.close();
    }
}
