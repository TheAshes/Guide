package com.chz.test;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chz.guide.view.GuideView;

public class TriangleScrollActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private GuideView guideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle_scroll);
        viewPager = findViewById(R.id.triangle_scroll_vp);
        guideView = findViewById(R.id.triangle_scroll_guide);
        PagerAdapter adapter = new GuideAdapter(this);
        viewPager.setAdapter(adapter);
        guideView.addOnPageChangeListener(viewPager);
    }
}
