package com.abhi.android.sciencebowl;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class StatisticsActivity extends FragmentActivity {
    private StatisticsSubjectPagerAdapter mAdapter;
    private ViewPager mPager;
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subject_statistics_pager);
        mAdapter = new StatisticsSubjectPagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.subject_statistics_pager);
        mPager.setAdapter(mAdapter);
        TabLayout tl = (TabLayout) findViewById(R.id.tLayout);
        tl.setTabMode(TabLayout.MODE_SCROLLABLE);
        for(int i =0;i<Subject.SIZE;i++){
            tl.addTab(tl.newTab().setText("LOL"));
        }
        tl.setupWithViewPager(mPager);
    }
}
