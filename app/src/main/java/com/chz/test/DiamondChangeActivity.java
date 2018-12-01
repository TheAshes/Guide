package com.chz.test;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chz.guide.view.GuideView;

public class DiamondChangeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private GuideView guideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diamond_change);
        viewPager = findViewById(R.id.diamond_change_vp);
        guideView = findViewById(R.id.diamond_change_guide);
        PagerAdapter adapter = new GuideAdapter(this);
        viewPager.setAdapter(adapter);
        guideView.addOnPageChangeListener(viewPager);
    }
}
