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
    private float unitSize;
    private float scrollSize;
    private float fullScrollOffset;
    private float scrollOffset;

    public Triangle(View view) {
        super(view);
    }

    @Override
    protected void initPaintTools() {
        triangleHeight = (float) (mIndexSize * Math.sin(60 * PI));
        unitSize = mIndexSize + mDistanceSize;
        if (mMode == GuideView.MODE_SCROLL) {
            marginTop = mIndexSize - triangleHeight;
        }
        focusPath = new Path();
        updatePosition();
        normalPath = new Path();
        Path temporaryPath = new Path();
        refreshPath(temporaryPath, 0);
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
                    refreshPath(focusPathNormal, unitSize);
                } else {
                    refreshPath(focusPathNormal, -unitSize);
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
        refreshPath(focusPath, 0);
    }

    @Override
    protected void scroll(int position, float positionOffset) {
        fullScrollOffset = mScrollCount * positionOffset;
        scrollOffset = fullScrollOffset - currentScrollPosition;
        if (positionOffset == 0) {
            currentScrollPosition = 0;
            updatePosition();
        }
        if (position != currentPosition) {
            if (position < currentPosition) {
                currentScrollPosition = mScrollCount;
            } else {
                currentScrollPosition = 0;
            }
            currentPosition = position;
            updatePosition();
        }
        if (positionOffset > 0) {
            if (lastPositionOffset == 0) {
                if (positionOffset < 0.5f) {
                    isReverse = true;
                } else {
                    isReverse = false;
                }
            } else {
                if (positionOffset < lastPositionOffset
                        && lastPositionOffset - positionOffset > 0.5f) {
                    isReverse = true;
                } else if (positionOffset > lastPositionOffset
                        && positionOffset - lastPositionOffset > 0.5f) {
                    isReverse = false;
                }
            }
        }
        if (positionOffset > 0 && lastPositionOffset > 0) {
            if (positionOffset > lastPositionOffset) {
                if (!isReverse && fullScrollOffset - currentScrollPosition > 0) {
                    isReverse = !isReverse;
                    currentScrollPosition -=1;
                    updatePosition();
                }
                if (fullScrollOffset - currentScrollPosition >= 1) {
                    currentScrollPosition++;
                    updatePosition();
                }
                if (isReverse) {
                    xLeft = (float) (xRight + mIndexSize * Math.cos((180 - scrollOffset * 120) * PI));
                    yLeft = (float) (yRight - mIndexSize * Math.sin((180 - scrollOffset * 120) * PI));
                    xTop = (float) (xRight + mIndexSize * Math.cos((120 - scrollOffset * 120) * PI));
                    yTop = (float) (yRight - mIndexSize * Math.sin((120 - scrollOffset * 120) * PI));
                } else {
                    xTop = (float) (xLeft + mIndexSize * Math.cos((180 - scrollOffset * 120) * PI));
                    yTop = (float) (yLeft - mIndexSize * Math.sin((180 - scrollOffset * 120) * PI));
                    xRight = (float) (xLeft + mIndexSize * Math.cos((120 - scrollOffset * 120) * PI));
                    yRight = (float) (yLeft - mIndexSize * Math.sin((120 - scrollOffset * 120) * PI));
                }
                refreshPath(focusPath, 0);
            } else {
                if (isReverse && fullScrollOffset - currentScrollPosition < 0) {
                    isReverse = !isReverse;
                    currentScrollPosition += 1;
                    updatePosition();
                }
                if (fullScrollOffset - currentScrollPosition <= -1) {
                    currentScrollPosition--;
                    updatePosition();
                }
                if (!isReverse) {
                    xTop = (float) (xLeft + mIndexSize * Math.cos((180 - scrollOffset * 120) * PI));
                    yTop = (float) (yLeft - mIndexSize * Math.sin((180 - scrollOffset * 120) * PI));
                    xRight = (float) (xLeft + mIndexSize * Math.cos((120 - scrollOffset * 120) * PI));
                    yRight = (float) (yLeft - mIndexSize * Math.sin((120 - scrollOffset * 120) * PI));
                } else {
                    xLeft = (float) (xRight + mIndexSize * Math.cos((180 - scrollOffset * 120) * PI));
                    yLeft = (float) (yRight - mIndexSize * Math.sin((180 - scrollOffset * 120) * PI));
                    xTop = (float) (xRight + mIndexSize * Math.cos((120 - scrollOffset * 120) * PI));
                    yTop = (float) (yRight - mIndexSize * Math.sin((120 - scrollOffset * 120) * PI));
                }
                refreshPath(focusPath, 0);
            }
        }
    }

    @Override
    protected void updatePosition() {
        scrollSize = currentScrollPosition * mIndexSize;
        xLeft = currentPosition * unitSize + scrollSize;
        yLeft = triangleHeight + marginTop;
        xTop = mIndexSize / 2 + currentPosition * unitSize + scrollSize;
        yTop = marginTop;
        xRight = mIndexSize + currentPosition * unitSize + scrollSize;
        yRight = triangleHeight + marginTop;
        refreshPath(focusPath, 0);
    }

    private void refreshPath(Path path, float value) {
        path.reset();
        path.moveTo(xLeft + value, yLeft);
        path.lineTo(xTop + value, yTop);
        path.lineTo(xRight + value, yRight);
        path.close();
    }
}
