package com.abhi.android.sciencebowl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class StatisticsActivity extends FragmentActivity {
    private StatisticsSubjectPagerAdapter mAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subject_statistics_pager);

        mAdapter = new StatisticsSubjectPagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.subject_statistics_pager);
        mPager.setAdapter(mAdapter);

        // set click listeners for statistics subject buttons to set pager to appropriate fragment
        LinearLayout subjectButtons = (LinearLayout)findViewById(R.id.statistics_buttons_layout);
        for(int ii = 0, count = subjectButtons.getChildCount(); ii < count; ii++) {
            final int index = ii;
            Button subjectButton = (Button)subjectButtons.getChildAt(ii);
            subjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPager.setCurrentItem(index, true);  // smooth scroll to fragment for subject
                }
            });
        }
    }
}
