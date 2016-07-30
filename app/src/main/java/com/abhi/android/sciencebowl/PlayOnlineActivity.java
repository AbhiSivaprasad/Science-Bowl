package com.abhi.android.sciencebowl;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class PlayOnlineActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = new PlayOnlineFragment();
            Bundle args = new Bundle();
            args.putString(PlayOnlineFragment.UID_KEY,getIntent().getStringExtra(PlayOnlineFragment.UID_KEY));
            args.putString(PlayOnlineFragment.NAME_KEY,getIntent().getStringExtra(PlayOnlineFragment.NAME_KEY));
            args.putString(PlayOnlineFragment.GAME_KEY,getIntent().getStringExtra(PlayOnlineFragment.GAME_KEY));
            fragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
