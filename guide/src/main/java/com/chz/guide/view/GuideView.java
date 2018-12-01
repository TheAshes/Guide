package com.chz.guide.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

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

    /*
    shape
    */
    public final static int SHAPE_CIRCLE = 11;
    public final static int SHAPE_SQUARE = 22;
    public final static int SHAPE_DIAMOND = 33;
    private final static int SHAPE_TRIANGLE = 44;

    /*
    mode
    */
    public final static int MODE_CHANGE = 11;
    public final static int MODE_ALPHA = 22;
    public final static int MODE_SLIDE = 33;

    /*
    Attributes
     */
    private GuideShape shape;

    private int indexShape;
    private int changeMode;
    private int normalColor;
    private int focusColor;
    private float indexSize;
    private float distanceSize;
    private int indexCount;
    private int screenWidth;
    private int screenHeight;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenSize(context);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GuideView, defStyleAttr, 0);

        normalColor = typedArray.getColor(R.styleable.GuideView_normalColor, Color.BLACK);
        focusColor = typedArray.getColor(R.styleable.GuideView_focusColor, Color.BLACK);
        indexSize = typedArray.getDimension(R.styleable.GuideView_indexSize, 0f);
        if (indexSize <= 0) {
            throw new IllegalArgumentException("indexSize Cannot be less than 0");
        }
        distanceSize = typedArray.getDimension(R.styleable.GuideView_distanceSize, 0f);
        if (distanceSize <= 0) {
            throw new IllegalArgumentException("distanceSize Cannot be less than 0");
        }
        indexCount = typedArray.getInt(R.styleable.GuideView_indexCount, 0);
        if (indexCount <= 0) {
            throw new IllegalArgumentException("indexCount Cannot be less than 0");
        }
        if (indexCount * (indexSize + distanceSize) - distanceSize > screenWidth) {
            throw new IllegalArgumentException("guideSize Cannot be greater than screenWidth");
        }
        indexShape = typedArray.getInt(R.styleable.GuideView_guideShape, SHAPE_CIRCLE);
        changeMode = typedArray.getInt(R.styleable.GuideView_changeMode, MODE_CHANGE);
        typedArray.recycle();
        initShape();
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
        shape.baseInit(changeMode, normalColor, focusColor, indexSize, distanceSize, indexCount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (indexCount * (indexSize + distanceSize) - distanceSize);
        switch (indexShape) {
            case SHAPE_TRIANGLE:
                int height = (int) (indexSize / 2 * Math.tan(60 * Math.PI / 180));
                setMeasuredDimension(width, height);
                break;
            default:
                setMeasuredDimension(width, (int) indexSize);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shape.onDraw(canvas);
    }

    public void addOnPageChangeListener(ViewPager viewPager) {
        shape.addOnPageChangeListener(viewPager);
    }

    private void getScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }
    public void updatePosition(int position){
        if (position<0||position>=indexCount){
            throw new IndexOutOfBoundsException("position indexOutOfBoundsException");
        }
        shape.setcurrentPosition(position);
    }
}
