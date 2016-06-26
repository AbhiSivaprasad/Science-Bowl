package com.abhi.android.sciencebowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar mDifficultySeekBar;
    private TextView mDifficultySeekBarHint;

    Settings newUserSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeVariables();

        //set widgets to current settings
        Settings currUserSettings = UserInformation.getUserSettings();
        int currDifficulty = currUserSettings.getDifficulty(); //integer from 0 to 5

        newUserSettings =
                new Settings(currUserSettings.getSubject(), currUserSettings.getDifficulty());

        mDifficultySeekBar.setProgress(currDifficulty);
        mDifficultySeekBarHint.setText("Difficulty: " + currDifficulty);

        mDifficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDifficultySeekBarHint.setText("Difficulty: " + progress);
                newUserSettings.setDifficulty(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        UserInformation.setUserSettings(newUserSettings);

        Firebase.setAndroidContext(this);
        Firebase mFirebaseRef =
                new Firebase("https://science-bowl.firebaseio.com/user-settings/" + UserInformation.getUsername());
        mFirebaseRef.setValue(newUserSettings);
    }

    private void initializeVariables()
    {
        mDifficultySeekBar = (SeekBar) findViewById(R.id.difficulty_seekbar);
        mDifficultySeekBarHint = (TextView) findViewById(R.id.difficulty_seekbar_hint);
    }
}
