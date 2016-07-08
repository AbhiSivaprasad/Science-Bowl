package com.abhi.android.sciencebowl;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;


public class MainMenuActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GameHelper.GameHelperListener {
    public static final String EXTRA_SCORE = "com.abhi.android.sciencebowl.EXTRA_SCORE";

    private static final int RC_MAIN = 10000;
    private static final int RC_LB_QUESTIONS_ANSWERED = 10001;
    private static final int RC_RESOLVE_CONNECTION = 10002;

    private static final String TAG = "MAIN_MENU";

    private Button mPlayButton;
    private Button mStatisticsButton;
    private Button mLeaderboardButton;
    private Button mSettingsButton;
    private Button mReviewButton;
    private Button mSignInLeaderboardButton;
    private Button mSignOutLeaderboardButton;
    private Button mSignOutButton;

    private String mUid;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GameHelper mGameHelper;
    private GoogleApiClient mGoogleApiClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mUid = UserInformation.getUid();

//        mGameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
//        mGameHelper.setup(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES).build();

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
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class); //start MainActivity
                startActivityForResult(intent, RC_MAIN);
            }
        });

        mLeaderboardButton = (Button) findViewById(R.id.leaderboard_button);
        mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!mGameHelper.isSignedIn()) {
//                    System.out.println("NOT SIGNED IN!");
//                    mGameHelper.beginUserInitiatedSignIn();
//                } else {
//                    System.out.println("SIGNED IN!");
//                    startLeaderboard(getString(R.string.leaderboard_id_questions_answered));
//                }
                startLeaderboard(getString(R.string.leaderboard_id_questions_answered));
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

        mSignInLeaderboardButton = (Button) findViewById(R.id.sign_in_leaderboard_button);
        mSignInLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.connect();
            }
        });

        mSignOutLeaderboardButton = (Button) findViewById(R.id.sign_out_leaderboard_button);
        mSignOutLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Games.signOut(mGoogleApiClient);
                mSignInLeaderboardButton.setVisibility(View.VISIBLE);
                mSignOutLeaderboardButton.setVisibility(View.GONE);
            }
        });

        mSignOutButton = (Button) findViewById(R.id.signOut_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
//        mGameHelper.onStart(this);
    }

    @Override
    public void onStop() {
//        mGameHelper.onStop();
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onSignInFailed() {
        System.out.println("sign in failed");
    }

    @Override
    public void onSignInSucceeded() {
        System.out.println("sign in good");
        startLeaderboard(getString(R.string.leaderboard_id_questions_answered));
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        System.out.println("YAY CONNECTED");
        mSignInLeaderboardButton.setVisibility(View.GONE);
        mSignOutLeaderboardButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("CONNECTION FAILED");
        System.out.println(connectionResult.hasResolution());
        if(connectionResult.hasResolution())
            try {
                connectionResult.startResolutionForResult(this, RC_RESOLVE_CONNECTION);
            }catch(IntentSender.SendIntentException e) {
                System.out.println("error caught");
            }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("suspended");
    }

    private void startLeaderboard(String leaderboardId) {
//        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGameHelper.getApiClient(),
//                leaderboardId), RC_LB_QUESTIONS_ANSWERED);
        if (mGoogleApiClient.isConnected())
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                leaderboardId), RC_LB_QUESTIONS_ANSWERED);
        else
            System.out.println("NOT CONNECTED CAN'T START LEADERBOARD");
    }

    private void setLeaderboard(String leaderboardId, int value) {
        String toastText;
//        if (mGameHelper.isSignedIn()) {
//            Games.Leaderboards.submitScore(mGameHelper.getApiClient(), leaderboardId, value);
//            toastText = getString(R.string.toast_leaderboard_questions_answered_submitted, value);
//        }
        if (mGoogleApiClient.isConnected()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, leaderboardId, value);
            toastText = getString(R.string.toast_leaderboard_questions_answered_submitted, value);
        }
        else
            toastText = getString(R.string.toast_leaderboard_questions_answered_error, value);
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_MAIN) {
                System.out.println(Integer.toString(data.getIntExtra(EXTRA_SCORE, -1)) + " MAINMENU");
                setLeaderboard(getString(R.string.leaderboard_id_questions_answered), data.getIntExtra(EXTRA_SCORE, -1));
            } else if (requestCode == RC_RESOLVE_CONNECTION)
                System.out.println("resolve connection");
            mGoogleApiClient.connect();
        }
        else System.out.println("RESULT BAD: " + Integer.toString(resultCode) + ": " + Integer.toString(requestCode));
    }
}
