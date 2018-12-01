package com.chz.guide.shape;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chz.guide.view.GuideView;


/**
 * Created by chz
 * on 2018/10/31
 */
public abstract class GuideShape implements ViewPager.OnPageChangeListener {

    protected View mView;
    protected ViewPager mViewPager;

    protected int mMode;
    protected int mNormalColor;
    protected int mFocusColor;
    protected float mIndexSize;
    protected float mDistanceSize;
    protected int mIndexCount;

    protected Paint normalPaint;
    protected Paint focusPaint;
    protected Paint focusPaintNormal;

    private float lastPositionOffset;
    private int lastState;
    protected int currentPosition;
    protected boolean isReverse = true;

    public GuideShape(View view) {
        mView = view;
    }

    public void baseInit(int mode, int normalColor, int focusColor
            , float indexSize, float distanceSize, int indexCount) {
        mMode = mode;
        mNormalColor = normalColor;
        mFocusColor = focusColor;
        mIndexSize = indexSize;
        mDistanceSize = distanceSize;
        mIndexCount = indexCount;

        normalPaint = new Paint();
        focusPaint = new Paint();
        normalPaint.setAntiAlias(true);
        focusPaint.setAntiAlias(true);
        normalPaint.setColor(mNormalColor);
        focusPaint.setColor(mFocusColor);
        if (mMode == GuideView.MODE_ALPHA) {
            focusPaintNormal = new Paint();
            focusPaintNormal.setAntiAlias(true);
            focusPaintNormal.setColor(mFocusColor);
            alpha(255, 0);
        }
        initPaintTools();
    }

    protected abstract void initPaintTools();

    public abstract void onDraw(Canvas canvas);

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (mMode) {
            case GuideView.MODE_CHANGE:
                if (positionOffset > 0 && lastPositionOffset > 0) {
                    if (positionOffset > lastPositionOffset
                            && lastPositionOffset <= 0.5f
                            && positionOffset > 0.5f
                            && positionOffset - lastPositionOffset < 0.5f) {
                        currentPosition++;
                        change();
                    } else if (positionOffset < lastPositionOffset
                            && lastPositionOffset >= 0.5f
                            && positionOffset < 0.5f
                            && lastPositionOffset - positionOffset < 0.5f) {
                        currentPosition--;
                        change();
                    }
                }
                break;
            case GuideView.MODE_ALPHA:
                if (positionOffset > 0 && lastPositionOffset == 0) {
                    if (positionOffset < 0.5f) {
                        isReverse = true;
                    } else {
                        isReverse = false;
                    }
                } else if (positionOffset > 0 && lastPositionOffset > 0) {
                    if (positionOffset < lastPositionOffset
                            && lastPositionOffset - positionOffset > 0.5f) {
                        isReverse = true;
                        currentPosition = mViewPager.getCurrentItem();
                        updatePosition();
                    } else if (positionOffset > lastPositionOffset
                            && positionOffset - lastPositionOffset > 0.5f) {
                        isReverse = false;
                        currentPosition = mViewPager.getCurrentItem();
                        updatePosition();
                    }
                }
                if (positionOffset > 0 && lastPositionOffset > 0) {
                    if (positionOffset > lastPositionOffset) {
                        if (isReverse) {
                            alpha((int) (255 - 255 * positionOffset), (int) (255 * positionOffset));
                        } else {
                            alpha((int) (255 * positionOffset), (int) (255 - 255 * positionOffset));
                        }
                    } else {
                        if (!isReverse) {
                            alpha((int) (255 * positionOffset), (int) (255 - 255 * positionOffset));
                        } else {
                            alpha((int) (255 - 255 * positionOffset), (int) (255 * positionOffset));
                        }
                    }
                }
                break;
            case GuideView.MODE_SLIDE:
                slide((position + positionOffset) * (mIndexSize + mDistanceSize));
                break;
        }
        lastPositionOffset = positionOffset;
    }

    public void onPageSelected(int position) {

    }

    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE
                && lastState != ViewPager.SCROLL_STATE_IDLE
                && currentPosition != mViewPager.getCurrentItem()) {
            currentPosition = mViewPager.getCurrentItem();
            updatePosition();
            if (mMode == GuideView.MODE_ALPHA) {
                alpha(255, 0);
            }
        }
        lastState = state;
    }

    protected void invalidate() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mView.invalidate();
        } else {
            mView.postInvalidate();
        }
    }

    public void addOnPageChangeListener(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
    }

    private void alpha(int focusAlpha, int focusAlphaNormal) {
        focusPaint.setAlpha(focusAlpha);
        focusPaintNormal.setAlpha(focusAlphaNormal);
        invalidate();
    }

    public void setcurrentPosition(int position){
        currentPosition = position;
        updatePosition();
    }
    protected abstract void change();

    protected abstract void slide(float value);

    protected abstract void updatePosition();

}
