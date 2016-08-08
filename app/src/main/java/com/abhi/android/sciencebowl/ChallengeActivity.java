package com.abhi.android.sciencebowl;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChallengeActivity extends FragmentActivity {
    private MultiplayerAdapter mAdapter;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        mAdapter = new MultiplayerAdapter(getSupportFragmentManager());
        vp = (ViewPager) findViewById(R.id.vpChallenge);
        vp.setAdapter(mAdapter);
    }
}
