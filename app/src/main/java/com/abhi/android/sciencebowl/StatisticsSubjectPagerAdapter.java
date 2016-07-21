package com.abhi.android.sciencebowl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StatisticsSubjectPagerAdapter extends FragmentPagerAdapter {
    public StatisticsSubjectPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Subject.SUBJECTS[position].toString();
    }

    @Override
    public Fragment getItem(int position) {
        return StatisticsSubjectFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return Subject.SIZE;
    }
}
