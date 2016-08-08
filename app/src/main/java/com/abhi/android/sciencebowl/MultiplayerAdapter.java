package com.abhi.android.sciencebowl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Tanuj on 7/20/16.
 */
public class MultiplayerAdapter extends FragmentPagerAdapter {

    public MultiplayerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PlayOnlineSetupActivity();
            case 1:
                return new ChallengeFragment();
        }
        return getItem(0);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
