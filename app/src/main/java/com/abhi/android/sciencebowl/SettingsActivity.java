package com.abhi.android.sciencebowl;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private SeekBar mDifficultySeekBar;
    private TextView mDifficultySeekBarHint;
    private Button[] mSubjectButtonList;
    private List<Subject> mSubjectsSelected;
    private Button bLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeVariables();

        //get current settings
        Settings currentUserSettings = UserInformation.getUserSettings();
        int currentDifficulty = (currentUserSettings == null)
                ? 0 : currentUserSettings.getDifficulty(); //integer from 0 to 5
        mSubjectsSelected = (currentUserSettings == null || currentUserSettings.getSubjects() == null)
                ? new ArrayList<Subject>() : currentUserSettings.getSubjects();

        //set seekbar to current setting
        mDifficultySeekBar.setProgress(currentDifficulty);
        mDifficultySeekBarHint.setText("Difficulty: " + currentDifficulty);

        mDifficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDifficultySeekBarHint.setText("Difficulty: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //set subject buttons to current settings
        for(Subject subject: mSubjectsSelected)
        {
            Button subjectButton = (Button) findViewById(subjectEnumToId(subject));
            setSelectedState(subjectButton, true);
        }

        //set listeners on subject buttons
        for(Button subjectButton : mSubjectButtonList)
            subjectButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Settings newUserSettings = new Settings();
        newUserSettings.setSubjects(mSubjectsSelected);
        newUserSettings.setDifficulty(mDifficultySeekBar.getProgress());

        UserInformation.setUserSettings(newUserSettings);
        Updater up = new Updater();
        up.execute(newUserSettings);

    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        Subject subject = subjectIdToEnum(clickedButton.getId());
        setSelectedState(v, toggleSelectedButton(subject));
    }

    private boolean toggleSelectedButton(Subject subject) {
        if (mSubjectsSelected.contains(subject)) {
            mSubjectsSelected.remove(subject);
            return false;
        }
        else {
            mSubjectsSelected.add(subject);
            return true;
        }
    }

    private void setSelectedState(View v, boolean isSelected) {
        int setColor;

        if(isSelected) {setColor = ContextCompat.getColor(this, R.color.selectedButton);}
        else {setColor = ContextCompat.getColor(this, R.color.unselectedButton);}

        v.setBackgroundColor(setColor);
    }

    private int subjectEnumToId(Subject subject) {
        switch (subject) {
            case EARTH:
                return R.id.earth_science_button;
            case BIOLOGY:
                return R.id.biology_button;
            case PHYSICS:
                return R.id.physics_button;
            case CHEMISTRY:
                return R.id.chemistry_button;
            case ENERGY:
                return R.id.energy_button;
            case MATH:
                return R.id.math_button;
            case ASTRO:
                return R.id.astro_button;
            default:  // unreachable because enum
                throw new IllegalArgumentException("Invalid subject: " + subject.toString());
        }
    }

    private Subject subjectIdToEnum(int subjectId) {
        switch (subjectId) {
            case R.id.earth_science_button:
                return Subject.EARTH;
            case R.id.biology_button:
                return Subject.BIOLOGY;
            case R.id.physics_button:
                return Subject.PHYSICS;
            case R.id.chemistry_button:
                return Subject.CHEMISTRY;
            case R.id.energy_button:
                return Subject.ENERGY;
            case R.id.math_button:
                return Subject.MATH;
            case R.id.astro_button:
                return Subject.ASTRO;
            default:
                throw new IllegalArgumentException("Invalid subject id: " + Integer.toString(subjectId));
        }
    }

    private void initializeVariables() {
        mDifficultySeekBar = (SeekBar) findViewById(R.id.difficulty_seekbar);
        mDifficultySeekBarHint = (TextView) findViewById(R.id.difficulty_seekbar_hint);

        Button mEarthButton = (Button) findViewById(R.id.earth_science_button);
        Button mBiologyButton = (Button) findViewById(R.id.biology_button);
        Button mPhysicsButton = (Button) findViewById(R.id.physics_button);
        Button mChemistryButton = (Button) findViewById(R.id.chemistry_button);
        Button mEnergyButton = (Button) findViewById(R.id.energy_button);
        Button mMathButton = (Button) findViewById(R.id.math_button);
        Button mAstroButton = (Button) findViewById(R.id.astro_button);

        Button bLink = (Button) findViewById(R.id.bLink);
        bLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,LinkActivity.class);
                startActivity(i);
            }
        });

        mSubjectButtonList = new Button[]
                {mEarthButton, mBiologyButton, mPhysicsButton, mChemistryButton, mEnergyButton, mMathButton,mAstroButton};
    }

    private class Updater extends AsyncTask<Settings,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Settings... sets) {
            Firebase.setAndroidContext(SettingsActivity.this);
            Firebase mFirebaseRef =
                    new Firebase("https://science-bowl.firebaseio.com/user-settings/" + UserInformation.getUid());
            mFirebaseRef.setValue(sets[0]);
            return null;
        }
    }
}
