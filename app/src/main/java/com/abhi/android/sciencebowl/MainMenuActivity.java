package com.abhi.android.sciencebowl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

/**
 * Created by Abhinand on 6/21/2016.
 */
public class MainMenuActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_MAIN = 0;
    private static final String EXTRA_USERNAME =
            "com.abhi.android.sb.username";


    private Button mPlayButton;
    private Button mStatisticsButton;
    private Button mLeaderboardButton;
    private Button mSettingsButton;

    private int mQuestionsCorrect;

    private String mUsername;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mUsername = getIntent().getStringExtra(EXTRA_USERNAME);

        //Initialize Buttons
        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = MainActivity.newIntent(MainMenuActivity.this);
                startActivityForResult(i, REQUEST_CODE_MAIN);
            }
        });

        mLeaderboardButton = (Button) findViewById(R.id.leaderboard_button);
        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        mStatisticsButton = (Button) findViewById(R.id.statistics_button);
        mSettingsButton = (Button) findViewById(R.id.settings_button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQUEST_CODE_MAIN)
        {
            if(data != null){
                mQuestionsCorrect = MainActivity.getQuestionsCorrect(data);
                writeToFirebaseLeaderboard(mUsername, mQuestionsCorrect);
            }
        }
    }

    //starting up MainMenuActivity from LoginActivity
    public static Intent newIntent (Context packageContext, String mUsername)
    {
        Intent i = new Intent(packageContext, MainMenuActivity.class);
        i.putExtra(EXTRA_USERNAME, mUsername);
        return i;
    }

    private void writeToFirebaseLeaderboard(String userName, int toWrite)
    {
        Firebase mFirebaseRef = new Firebase("https://science-bowl.firebaseio.com/leaderboard");
        mFirebaseRef.child(userName).setValue(toWrite);
    }
}
