package com.abhi.android.sciencebowl;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ReviewActivity extends FragmentActivity {

    private List<QuestionUserAnswerPair> mReviewQuestionBank;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mViewPager = (ViewPager) findViewById(R.id.activity_review_activity_view_pager);
        mReviewQuestionBank = UserInformation.getReviewQuestionBank();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return ReviewFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return mReviewQuestionBank.size();
            }
        });

    }

}
