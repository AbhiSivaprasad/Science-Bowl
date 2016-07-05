package com.abhi.android.sciencebowl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;


public class MainMenuActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_LB_QUESTIONS_ANSWERED = 10001;

    private Button mPlayButton;
    private Button mStatisticsButton;
    private Button mLeaderboardButton;
    private Button mSettingsButton;
    private Button mReviewButton;

    private int mQuestionsCorrect;

    private String mUsername;

    private GoogleApiClient mGoogleApiClient;

    private GameHelper mGameHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mUsername = UserInformation.getUsername();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES).build();

        //Get user settings from Firebase. Ideally should be done during login
        Firebase.setAndroidContext(this);

        Firebase mFirebaseRef = new Firebase("https://science-bowl.firebaseio.com/user-settings/" + mUsername);
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
                mGoogleApiClient.connect();
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

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        String leaderboardIdQuestionsAnswered = getResources().getString(R.string.leaderboard_id_questions_answered);
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                leaderboardIdQuestionsAnswered), RC_LB_QUESTIONS_ANSWERED);
    }

    @Override
    public void onConnectionSuspended(int cause) {}

    @Override
    public void onConnectionFailed(ConnectionResult result) {}

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
