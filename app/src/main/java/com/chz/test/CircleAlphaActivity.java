package com.chz.test;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chz.guide.view.GuideView;

public class CircleAlphaActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private GuideView guideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_alpha);
        viewPager = findViewById(R.id.circle_alpha_vp);
        guideView = findViewById(R.id.circle_alpha_guide);
        PagerAdapter adapter = new GuideAdapter(this);
        viewPager.setAdapter(adapter);
        guideView.addOnPageChangeListener(viewPager);
    }
}
