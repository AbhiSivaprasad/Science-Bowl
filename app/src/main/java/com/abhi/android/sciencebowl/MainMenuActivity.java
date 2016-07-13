package com.abhi.android.sciencebowl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.firebase.auth.FirebaseAuth;


public class MainMenuActivity extends AppCompatActivity implements GameHelper.GameHelperListener {
    public static final String EXTRA_SCORE = "com.abhi.android.sciencebowl.EXTRA_SCORE";

    private static final int RC_MAIN = 10000;
    private static final int RC_LB_QUESTIONS_ANSWERED = 10001;

    private static final String TAG = "MAIN_MENU";

    private Button mPlayButton;
    private Button mStatisticsButton;
    private Button mPlayOnlineButton;
    private Button mLeaderboardButton;
    private Button mSettingsButton;
    private Button mReviewButton;
    private Button mSignOutButton;

    private String mUid;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GameHelper mGameHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mUid = UserInformation.getUid();

        mGameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        mGameHelper.setup(this);

        //Get user settings from Firebase. Ideally should be done during login
        Firebase.setAndroidContext(this);

        Firebase mFirebaseRef = new Firebase(getString(R.string.BASE_URI)+getString(R.string.DIR_SETTINGS) + "/" + mUid);
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
                Intent intent = new Intent(MainMenuActivity.this, PlayActivity.class); //start PlayActivity
                startActivityForResult(intent, RC_MAIN);
            }
        });

        mLeaderboardButton = (Button) findViewById(R.id.leaderboard_button);
        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mGameHelper.isSignedIn())
                    startLeaderboard(getString(R.string.leaderboard_id_questions_answered));
                else if(mGameHelper.isConnecting())
                    Toast.makeText(MainMenuActivity.this, "Connecting... Please try again shortly.", Toast.LENGTH_SHORT).show();
                else
                    mGameHelper.beginUserInitiatedSignIn();
            }
        });

        mStatisticsButton = (Button) findViewById(R.id.statistics_button);
        mStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeaderboard(getString(R.string.leaderboard_id_questions_answered), UserInformation.getQuestionsCorrect());
            }
        });

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

        mSignOutButton = (Button) findViewById(R.id.signOut_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameHelper.signOut();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        mPlayOnlineButton = (Button) findViewById(R.id.play_online_button);
        mPlayOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, PlayOnlineSetupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mGameHelper.onStart(this);
    }

    @Override
    public void onStop() {
        mGameHelper.onStop();
        super.onStop();
    }

    @Override
    public void onSignInSucceeded() {
        // if score was cached last time (due to lack of connection), try to submit score to leaderboard.
        // set value of isScoreCached to the opposite of the success result of submission.
        // if score was submitted successfully, mark score as no longer cached, and vice versa.
        if(UserInformation.getIsScoreCached()) {
            UserInformation.setIsScoreCached(
                    !setLeaderboard(
                            getString(R.string.leaderboard_id_questions_answered),
                            UserInformation.getCachedScore()
                    )
            );
        }
    }

    @Override
    public void onSignInFailed() {
        mGameHelper.beginUserInitiatedSignIn();
    }

    private void startLeaderboard(String leaderboardId) {
        if (mGameHelper.isSignedIn())
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGameHelper.getApiClient(),
                leaderboardId), RC_LB_QUESTIONS_ANSWERED);
    }

    private boolean setLeaderboard(String leaderboardId, int value) {
        if (mGameHelper.isSignedIn()) {
            Games.Leaderboards.submitScore(mGameHelper.getApiClient(), leaderboardId, value);
            Toast.makeText(this,
                    getString(R.string.toast_leaderboard_questions_answered_submitted, value),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        UserInformation.setIsScoreCached(true);
        UserInformation.setCachedScore(value);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_MAIN) {
                setLeaderboard(getString(R.string.leaderboard_id_questions_answered), data.getIntExtra(EXTRA_SCORE, -1));
            }
        }
        else System.out.println("RESULT BAD: " + Integer.toString(resultCode) + ": " + Integer.toString(requestCode));
    }
}
