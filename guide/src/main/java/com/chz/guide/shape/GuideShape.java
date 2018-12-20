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

    //单位圆周率
    protected static final double PI = Math.PI / 180;
    //三角形单位滚动角度
    protected static final int TRIANGLE_UNIT_ANGLE = 120;
    //正方形单位滚动角度
    protected static final int SQUARE_UNIT_ANGLE = 90;

    private View mView;
    private ViewPager mViewPager;

    protected int mMode;
    protected int mShape;
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
    protected float unitSize;
    protected float unitAngle;
    protected float scrollSize;
    protected float fullScrollOffset;
    protected float sweepAngle;
    private int alphaOffset;

    public GuideShape(View view) {
        mView = view;
    }

    public void baseInit(int mode, int shape, int normalColor, int focusColor
            , float indexSize, float distanceSize, int indexCount, int scrollCount) {
        mMode = mode;
        mShape = shape;
        mNormalColor = normalColor;
        mFocusColor = focusColor;
        mIndexSize = indexSize;
        mDistanceSize = distanceSize;
        mIndexCount = indexCount;
        mScrollCount = scrollCount;
        unitSize = mIndexSize + distanceSize;

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


    //核心方法,通过switch区分切换模式
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
                        if (positionOffset < 0.5f && lastPositionOffset > 0.5f
                                && lastPositionOffset - positionOffset > 0.5f) {
                            isReverse = true;
                            currentPosition = mViewPager.getCurrentItem();
                            updatePosition();
                        } else if (positionOffset > 0.5f && lastPositionOffset < 0.5f
                                && positionOffset - lastPositionOffset > 0.5f) {
                            isReverse = false;
                            currentPosition = mViewPager.getCurrentItem();
                            updatePosition();
                        }
                    }
                }
                if (positionOffset > 0 && lastPositionOffset > 0) {
                    alphaOffset = (int) (255 * positionOffset);
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
                slide((position + positionOffset) * unitSize);
                invalidateView();
                break;
            case GuideView.MODE_SCROLL:
                if (mShape == GuideView.SHAPE_CIRCLE
                        || mShape == GuideView.SHAPE_DIAMOND) {
                    slide((position + positionOffset) * unitSize);
                } else {
                    fullScrollOffset = mScrollCount * positionOffset;
                    sweepAngle = unitAngle * (fullScrollOffset - (int) fullScrollOffset);
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
                            if (positionOffset < 0.5f && lastPositionOffset > 0.5f
                                    && lastPositionOffset - positionOffset > 0.5f) {
                                isReverse = true;
                            } else if (positionOffset > 0.5f && lastPositionOffset < 0.5f
                                    && positionOffset - lastPositionOffset > 0.5f) {
                                isReverse = false;
                            }
                        }
                    }
                    if (positionOffset > 0 && lastPositionOffset > 0) {
                        if (positionOffset > lastPositionOffset) {
                            if (!isReverse && fullScrollOffset - currentScrollPosition >= 0) {
                                isReverse = !isReverse;
                                currentScrollPosition -= 1;
                                updatePosition();
                            }
                            if (fullScrollOffset - currentScrollPosition >= 1) {
                                currentScrollPosition++;
                                updatePosition();
                            }
                            scroll();
                        } else {
                            if (isReverse && fullScrollOffset - currentScrollPosition < 0) {
                                isReverse = !isReverse;
                                currentScrollPosition += 1;
                                updatePosition();
                            }
                            if (fullScrollOffset - currentScrollPosition < -1) {
                                currentScrollPosition--;
                                updatePosition();
                            }
                            scroll();
                        }
                    }
                }
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

    private void alpha(int focusAlpha, int focusAlphaNormal) {
        focusPaint.setAlpha(focusAlpha);
        focusPaintNormal.setAlpha(focusAlphaNormal);
    }

    protected abstract void slide(float value);

    protected abstract void scroll();

    protected abstract void updatePosition();

}
