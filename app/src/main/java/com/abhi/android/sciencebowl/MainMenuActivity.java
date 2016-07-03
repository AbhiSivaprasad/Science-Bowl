package com.abhi.android.sciencebowl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainMenuActivity extends AppCompatActivity {

    private Button mPlayButton;
    private Button mStatisticsButton;
    private Button mLeaderboardButton;
    private Button mSettingsButton;
    private Button mReviewButton;

    private int mQuestionsCorrect;

    private String mUid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mUid = UserInformation.getUid();

        //Get user settings from Firebase. Ideally should be done during login
        Firebase.setAndroidContext(this);

        Firebase mFirebaseRef = new Firebase("https://science-bowl.firebaseio.com/user-settings/" + mUid);
        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Settings userSettings = dataSnapshot.getValue(Settings.class);
                UserInformation.setUserSettings(userSettings);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        //Initialize Buttons
        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class); //start MainActivity
                startActivity(intent);
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
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        mReviewButton = (Button) findViewById(R.id.review_button);
        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });
    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQUEST_CODE_MAIN)
        {
            if(data != null){
                mQuestionsCorrect = MainActivity.getAmountOfCorrectQuestions(data);
                writeToFirebaseLeaderboard(mUsername, mQuestionsCorrect);
            }
        }
    } */
}
