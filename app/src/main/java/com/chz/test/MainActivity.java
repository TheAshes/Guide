package com.chz.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chz.guide.shape.Square;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button circleChange;
    private Button circleAlpha;
    private Button circleSlide;
    private Button circleScroll;
    private Button squareChange;
    private Button squareAlpha;
    private Button squareSlide;
    private Button squareScroll;
    private Button diamondChange;
    private Button diamondAlpha;
    private Button diamondSlide;
    private Button diamondScroll;
    private Button triangleChange;
    private Button triangleAlpha;
    private Button triangleSlide;
    private Button triangleScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setOnClickListener();
    }


    private void initView() {
        circleChange = findViewById(R.id.circle_change);
        circleAlpha = findViewById(R.id.circle_alpha);
        circleSlide = findViewById(R.id.circle_slide);
        circleScroll = findViewById(R.id.circle_scroll);
        squareChange = findViewById(R.id.square_change);
        squareAlpha = findViewById(R.id.square_alpha);
        squareSlide = findViewById(R.id.square_slide);
        squareScroll = findViewById(R.id.square_scroll);
        diamondChange = findViewById(R.id.diamond_change);
        diamondAlpha = findViewById(R.id.diamond_alpha);
        diamondSlide = findViewById(R.id.diamond_slide);
        diamondScroll = findViewById(R.id.diamond_scroll);
        triangleChange = findViewById(R.id.triangle_change);
        triangleAlpha = findViewById(R.id.triangle_alpha);
        triangleSlide = findViewById(R.id.triangle_slide);
        triangleScroll = findViewById(R.id.triangle_scroll);
    }

    private void setOnClickListener() {
        circleChange.setOnClickListener(this);
        circleAlpha.setOnClickListener(this);
        circleSlide.setOnClickListener(this);
        circleScroll.setOnClickListener(this);
        squareChange.setOnClickListener(this);
        squareAlpha.setOnClickListener(this);
        squareSlide.setOnClickListener(this);
        squareScroll.setOnClickListener(this);
        diamondChange.setOnClickListener(this);
        diamondAlpha.setOnClickListener(this);
        diamondSlide.setOnClickListener(this);
        diamondScroll.setOnClickListener(this);
        triangleChange.setOnClickListener(this);
        triangleAlpha.setOnClickListener(this);
        triangleSlide.setOnClickListener(this);
        triangleScroll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circle_change:
                startActivity(new Intent(this, CircleChangeActivity.class));
                break;
            case R.id.circle_alpha:
                startActivity(new Intent(this, CircleAlphaActivity.class));
                break;
            case R.id.circle_slide:
                startActivity(new Intent(this, CircleSlideActivity.class));
                break;
            case R.id.circle_scroll:
                startActivity(new Intent(this, CircleScrollActivity.class));
                break;
            case R.id.square_change:
                startActivity(new Intent(this, SquareChangeActivity.class));
                break;
            case R.id.square_alpha:
                startActivity(new Intent(this, SquareAlphaActivity.class));
                break;
            case R.id.square_slide:
                startActivity(new Intent(this, SquareSlideActivity.class));
                break;
            case R.id.square_scroll:
                startActivity(new Intent(this, SquareScrollActivity.class));
                break;
            case R.id.diamond_change:
                startActivity(new Intent(this, DiamondChangeActivity.class));
                break;
            case R.id.diamond_alpha:
                startActivity(new Intent(this, DiamondAlphaActivity.class));
                break;
            case R.id.diamond_slide:
                startActivity(new Intent(this, DiamondSlideActivity.class));
                break;
            case R.id.diamond_scroll:
                startActivity(new Intent(this, DiamondScrollActivity.class));
                break;
            case R.id.triangle_change:
                startActivity(new Intent(this, TriangleChangeActivity.class));
                break;
            case R.id.triangle_alpha:
                startActivity(new Intent(this, TriangleAlphaActivity.class));
                break;
            case R.id.triangle_slide:
                startActivity(new Intent(this, TriangleSlideActivity.class));
                break;
            case R.id.triangle_scroll:
                startActivity(new Intent(this, TriangleScrollActivity.class));
                break;
        }
    }
}
