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

    //单位圆周率
    public static final double PI = Math.PI/180;

    //形状
    public static final int SHAPE_CIRCLE = 11;//圆形
    public static final int SHAPE_SQUARE = 22;//正方形
    public static final int SHAPE_DIAMOND = 33;//菱形
    private static final int SHAPE_TRIANGLE = 44;//三角形

    //角度
    public static final int MODE_CHANGE = 11;//直接切换
    public static final int MODE_ALPHA = 22;//渐变切换
    public static final int MODE_SLIDE = 33;//滑动切换
    public static final int MODE_SCROLL = 44;//滚动切换

    //形状实例
    private GuideShape shape;
    //控件基本参数
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
        //指示器宽度必须大于0
        if (indexSize <= 0) {
            throw new IllegalArgumentException("indexSize must be greater than 0");
        }
        distanceSize = typedArray.getDimension(R.styleable.GuideView_distanceSize, 0f);
        //指示器间距不能小于0
        if (distanceSize < 0) {
            throw new IllegalArgumentException("distanceSize cannot be less than 0");
        }
        indexCount = typedArray.getInt(R.styleable.GuideView_indexCount, 0);
        //指示器数量不能小于0
        if (indexCount < 0) {
            throw new IllegalArgumentException("indexCount cannot be less than 0");
        }
        indexShape = typedArray.getInt(R.styleable.GuideView_guideShape, SHAPE_CIRCLE);
        changeMode = typedArray.getInt(R.styleable.GuideView_changeMode, MODE_CHANGE);
        //如果切换模式为"滚动",为了动效，强制要求指示器间距必须为指示器宽度的整数倍
        if (changeMode == MODE_SCROLL) {
            if (distanceSize % indexSize != 0) {
                throw new IllegalArgumentException("If mode is scrolling," +
                        "The distanceSize width must be an integer multiple of indexSize");
            }
            //滚动模式下的指示器滚动次数
            scrollCount = (int) (distanceSize / indexSize + 1);
        }
        typedArray.recycle();
        initShape();
        //指示器参数初始化
        shape.baseInit(changeMode, indexShape,normalColor, focusColor, indexSize,
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
                    viewHeight = (int) (indexSize / Math.cos(45 *PI));
                }
                break;
            case SHAPE_DIAMOND:
                break;
            case SHAPE_TRIANGLE:
                if (changeMode != MODE_SCROLL) {
                    viewHeight = (int) (indexSize / 2 * Math.tan(60 * PI));
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

    //判断指示器控件是否超出父容器边界
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
                            throw new IllegalArgumentException("guideWidth " +
                                    "Cannot be greater than parentWidth");
                        }
                        if (viewHeight > height) {
                            throw new IllegalArgumentException("guideHeight " +
                                    "Cannot be greater than parentHeight");
                        }
                    }
                });
    }
}
