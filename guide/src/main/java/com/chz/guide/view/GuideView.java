package com.chz.guide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.chz.guide.R;
import com.chz.guide.shape.Circle;
import com.chz.guide.shape.Diamond;
import com.chz.guide.shape.GuideShape;
import com.chz.guide.shape.Square;
import com.chz.guide.shape.Triangle;

/**
 * Created by chz
 * on 2018/10/31
 */
public class GuideView extends View {

    public static final int SHAPE_CIRCLE = 11;
    public static final int SHAPE_SQUARE = 22;
    public static final int SHAPE_DIAMOND = 33;
    private static final int SHAPE_TRIANGLE = 44;

    public static final int MODE_CHANGE = 11;
    public static final int MODE_ALPHA = 22;
    public static final int MODE_SLIDE = 33;
    public static final int MODE_SCROLL = 44;

    private GuideShape shape;
    private int viewWidth;
    private int viewHeight;
    private int indexShape;
    private int changeMode;
    private float indexSize;
    private float distanceSize;
    private int indexCount;
    private int scrollCount;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().
                obtainStyledAttributes(attrs, R.styleable.GuideView, defStyleAttr, 0);

        int normalColor = typedArray.getColor(R.styleable.GuideView_normalColor, Color.BLACK);
        int focusColor = typedArray.getColor(R.styleable.GuideView_focusColor, Color.BLACK);
        indexSize = typedArray.getDimension(R.styleable.GuideView_indexSize, 0f);
        if (indexSize <= 0) {
            throw new IllegalArgumentException("indexSize Cannot be less than 0");
        }
        distanceSize = typedArray.getDimension(R.styleable.GuideView_distanceSize, 0f);
        if (distanceSize < 0) {
            throw new IllegalArgumentException("distanceSize Cannot be less than 0");
        }
        indexCount = typedArray.getInt(R.styleable.GuideView_indexCount, 0);
        if (indexCount <= 0) {
            throw new IllegalArgumentException("indexCount Cannot be less than 0");
        }
        indexShape = typedArray.getInt(R.styleable.GuideView_guideShape, SHAPE_CIRCLE);
        changeMode = typedArray.getInt(R.styleable.GuideView_changeMode, MODE_CHANGE);
        if (changeMode == MODE_SCROLL) {
            if (distanceSize % indexSize != 0) {
                throw new IllegalArgumentException("If mode is scrolling," +
                        "The distanceSize width must be an integer multiple of indexSize");
            }
            scrollCount = (int) (distanceSize / indexSize + 1);
        }
        typedArray.recycle();
        initShape();
        shape.baseInit(changeMode, normalColor, focusColor, indexSize,
                distanceSize, indexCount, scrollCount);
    }

    private void initShape() {
        switch (indexShape) {
            case SHAPE_CIRCLE:
                shape = new Circle(this);
                break;
            case SHAPE_SQUARE:
                shape = new Square(this);
                break;
            case SHAPE_DIAMOND:
                shape = new Diamond(this);
                break;
            case SHAPE_TRIANGLE:
                shape = new Triangle(this);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = (int) (indexCount * (indexSize + distanceSize) - distanceSize);
        viewHeight = (int) indexSize;
        switch (indexShape) {
            case SHAPE_CIRCLE:
                break;
            case SHAPE_SQUARE:
                if (changeMode == MODE_SCROLL) {
                    viewHeight = (int) (indexSize / Math.cos(45 * Math.PI / 180));
                }
                break;
            case SHAPE_DIAMOND:
                break;
            case SHAPE_TRIANGLE:
                if (changeMode != MODE_SCROLL) {
                    viewHeight = (int) (indexSize / 2 * Math.tan(60 * Math.PI / 180));
                }
                break;
            default:
                break;
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shape.onDraw(canvas);
    }

    public void addOnPageChangeListener(ViewPager viewPager) {
        shape.addOnPageChangeListener(viewPager);
        checkViewSize();
    }

    public void updatePosition(int position) {
        if (position < 0 || position >= indexCount) {
            throw new IndexOutOfBoundsException("position indexOutOfBoundsException");
        }
        shape.setCurrentPosition(position);
    }

    private void checkViewSize() {
        final ViewGroup viewGroup = (ViewGroup) getParent();
        viewGroup.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int width = viewGroup.getWidth();
                        int height = viewGroup.getHeight();
                        if (viewWidth > width) {
                            throw new IllegalArgumentException("guideWidth Cannot be greater than parentWidth");
                        }
                        if (viewHeight > height) {
                            throw new IllegalArgumentException("guideHeight Cannot be greater than parentHeight");
                        }
                    }
                });
    }
}
