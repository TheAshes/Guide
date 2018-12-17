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

    protected static final double PI = Math.PI / 180;

    private View mView;
    private ViewPager mViewPager;

    protected int mMode;
    protected int mNormalColor;
    protected int mFocusColor;
    protected float mIndexSize;
    protected float mDistanceSize;
    protected int mIndexCount;
    protected int mScrollCount;

    protected Paint normalPaint;
    protected Paint focusPaint;
    protected Paint focusPaintNormal;

    private int lastState = ViewPager.SCROLL_STATE_IDLE;
    protected boolean isReverse = true;
    protected float lastPositionOffset;
    protected int currentPosition;
    protected int currentScrollPosition;
    private int alphaOffset;

    public GuideShape(View view) {
        mView = view;
    }

    public void baseInit(int mode, int normalColor, int focusColor
            , float indexSize, float distanceSize, int indexCount, int scrollCount) {
        mMode = mode;
        mNormalColor = normalColor;
        mFocusColor = focusColor;
        mIndexSize = indexSize;
        mDistanceSize = distanceSize;
        mIndexCount = indexCount;
        mScrollCount = scrollCount;

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
            focusPaintNormal.setAlpha(0);
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
                        invalidateView();
                    } else if (positionOffset < lastPositionOffset
                            && lastPositionOffset >= 0.5f
                            && positionOffset < 0.5f
                            && lastPositionOffset - positionOffset < 0.5f) {
                        currentPosition--;
                        change();
                        invalidateView();
                    }
                }
                break;
            case GuideView.MODE_ALPHA:
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
                            currentPosition = mViewPager.getCurrentItem();
                            updatePosition();
                        } else if (positionOffset > lastPositionOffset
                                && positionOffset - lastPositionOffset > 0.5f) {
                            isReverse = false;
                            currentPosition = mViewPager.getCurrentItem();
                            updatePosition();
                        }
                    }
                }
                if (positionOffset > 0 && lastPositionOffset > 0) {
                    alphaOffset = (int) (255*positionOffset);
                    if (positionOffset > lastPositionOffset) {
                        if (isReverse) {
                            alpha(255 - alphaOffset, alphaOffset);
                        } else {
                            alpha(alphaOffset, 255 - alphaOffset);
                        }
                    } else {
                        if (!isReverse) {
                            alpha(alphaOffset, 255 - alphaOffset);
                        } else {
                            alpha(255 - alphaOffset, alphaOffset);
                        }
                    }
                }
                invalidateView();
                break;
            case GuideView.MODE_SLIDE:
                slide((position + positionOffset) * (mIndexSize + mDistanceSize));
                invalidateView();
                break;
            case GuideView.MODE_SCROLL:
                scroll(position, positionOffset);
                invalidateView();
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
            if (mMode == GuideView.MODE_ALPHA) {
                alpha(255, 0);
            }
            updatePosition();
            invalidateView();
        }
        lastState = state;
    }

    private void invalidateView() {
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

    public void setCurrentPosition(int position) {
        currentPosition = position;
        updatePosition();
        invalidateView();
    }

    protected abstract void change();

    protected abstract void slide(float value);

    private void alpha(int focusAlpha, int focusAlphaNormal) {
        focusPaint.setAlpha(focusAlpha);
        focusPaintNormal.setAlpha(focusAlphaNormal);
        invalidateView();
    }

    protected abstract void scroll(int position, float positionOffset);

    protected abstract void updatePosition();

}
