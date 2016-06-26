package com.abhi.android.sciencebowl;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private SeekBar mDifficultySeekBar;
    private TextView mDifficultySeekBarHint;

    Settings newUserSettings;

    private Button mEarthButton;
    private Button mBiologyButton;
    private Button mPhysicsButton;
    private Button mMathButton;
    private Button mChemistryButton;
    private Button mEnergyButton;

    private Button[] mSubjectButtonList;

    private boolean[] mIsSubjectButtonSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeVariables();

        //get current settings
        Settings currUserSettings = UserInformation.getUserSettings();
        int currDifficulty = currUserSettings.getDifficulty(); //integer from 0 to 5
        String currSubjects = currUserSettings.getSubject();

        newUserSettings =
                new Settings(currUserSettings.getSubject(), currUserSettings.getDifficulty());

        //set seekbar to current setting
        mDifficultySeekBar.setProgress(currDifficulty);
        mDifficultySeekBarHint.setText("Difficulty: " + currDifficulty);

        mDifficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDifficultySeekBarHint.setText("Difficulty: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //set subject buttons to current settings
        for(int i = 0; i < currSubjects.length(); i++)
        {
            char digit = currSubjects.charAt(i);
            if(digit == '1') {
                setSelectedState(mSubjectButtonList[i], true);
                mIsSubjectButtonSelected[i] = true;
            }
        }

        //set listeners on subject buttons
        for(Button subjectButton : mSubjectButtonList)
            subjectButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        String newSubject = "";
        for(boolean isButtonSelected : mIsSubjectButtonSelected)
        {
            if(isButtonSelected)
                newSubject += "1";
            else
                newSubject += "0";
        }
        newUserSettings.setSubject(newSubject);
        newUserSettings.setDifficulty(mDifficultySeekBar.getProgress());

        UserInformation.setUserSettings(newUserSettings);

        Firebase.setAndroidContext(this);
        Firebase mFirebaseRef =
                new Firebase("https://science-bowl.firebaseio.com/user-settings/" + UserInformation.getUsername());
        mFirebaseRef.setValue(newUserSettings);
    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;

        int clickedButtonIndex = getSubjectButtonIndex(clickedButton.getText().toString());
        mIsSubjectButtonSelected[clickedButtonIndex]
                = !mIsSubjectButtonSelected[clickedButtonIndex];

        setSelectedState(v, mIsSubjectButtonSelected[clickedButtonIndex]);
    }

    private void setSelectedState(View v, boolean isSelected) {
        if(isSelected)
            v.setBackgroundColor(Color.GRAY);
        else
            v.setBackgroundColor(Color.LTGRAY);
    }

    private int getSubjectButtonIndex(String buttonName)
    {
        if(buttonName == getString(R.string.earth_science_button))  return 0;
        else if(buttonName == getString(R.string.biology_button))   return 1;
        else if(buttonName == getString(R.string.physics_button))   return 2;
        else if(buttonName == getString(R.string.chemistry_button)) return 3;
        else if(buttonName == getString(R.string.energy_button))    return 4;
        else if(buttonName == getString(R.string.math_button))      return 5;
        else throw new IllegalArgumentException();
    }

    private void initializeVariables() {
        mDifficultySeekBar = (SeekBar) findViewById(R.id.difficulty_seekbar);
        mDifficultySeekBarHint = (TextView) findViewById(R.id.difficulty_seekbar_hint);

        mEarthButton = (Button) findViewById(R.id.earth_science_button);
        mBiologyButton = (Button) findViewById(R.id.biology_button);
        mPhysicsButton = (Button) findViewById(R.id.physics_button);
        mChemistryButton = (Button) findViewById(R.id.chemistry_button);
        mEnergyButton = (Button) findViewById(R.id.energy_button);
        mMathButton = (Button) findViewById(R.id.math_button);

        mSubjectButtonList = new Button[]
                {mEarthButton, mBiologyButton, mPhysicsButton, mChemistryButton, mEnergyButton, mMathButton};

        mIsSubjectButtonSelected =
                new boolean[getResources().getInteger(R.integer.number_of_subjects)];
    }
}
