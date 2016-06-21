package com.abhi.android.sciencebowl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Firebase mFirebaseRef;
    private TextView mQuestion;
    private Button mChoiceW;
    private Button mChoiceX;
    private Button mChoiceY;
    private Button mChoiceZ;
    private Button[] mChoiceButtonList; //List of buttons eases setEnabled and correct/incorrectChoice buttons.

    private Button mNextButton;

    private List<Question> mQuestionBank = new ArrayList<Question>();
    private Question mCurrQuestion = null;
    private int mCurrQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        //Takes some time to get data. Executes subsequent code before data is retrieved causing a lag between inflation of
        //XML and the appearance of question/answers. Need to fix this.
        mFirebaseRef = new Firebase("https://science-bowl.firebaseio.com/quizlist");
        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Question q = snapshot.getValue(Question.class);
                    mQuestionBank.add(q);
                }
                updateQuestion(); //load first question
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        //initialize buttons
        mQuestion = (TextView) findViewById(R.id.question);
        mChoiceW = (Button) findViewById(R.id.choiceW);
        mChoiceX = (Button) findViewById(R.id.choiceX);
        mChoiceY = (Button) findViewById(R.id.choiceY);
        mChoiceZ = (Button) findViewById(R.id.choiceZ);

        mChoiceButtonList = new Button[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};
        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setOnClickListener(this);

        //attach listener to NextButton and make it initially invisible
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setVisibility(View.GONE);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrQuestionIndex = (mCurrQuestionIndex + 1) % mQuestionBank.size(); //questions fed in circular loop. Need more sophisticated order.
                updateQuestion();
            }
        });

    }

    //update UI and mCurrQuestion
    private void updateQuestion() {
        mNextButton.setVisibility(View.GONE);
        mCurrQuestion = mQuestionBank.get(mCurrQuestionIndex);
        mQuestion.setText(mCurrQuestion.getQuestion());
        mChoiceW.setText("W) " + mCurrQuestion.getW());
        mChoiceX.setText("X) " + mCurrQuestion.getX());
        mChoiceY.setText("Y) " + mCurrQuestion.getY());
        mChoiceZ.setText("Z) " + mCurrQuestion.getZ());

        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setBackgroundColor(Color.LTGRAY);

        choiceButtonsEnabled(true);
    }

    @Override
    public void onClick(View view) { //clean up this method
        Button selectedChoice = (Button) view;

        char choiceLetter = ((String) selectedChoice.getText()).charAt(0); // Letter comes first in answer choice
        char answer = mCurrQuestion.getCorrect().toUpperCase().charAt(0);

        boolean isCorrect = (answer == choiceLetter);

        Button correctChoice = getChoiceButton(answer);
        correctChoice.setBackgroundColor(Color.GREEN); //correct answer effect always shown

        String result;
        if(isCorrect)
            result = "Correct";
        else {
            result = "Incorrect";
            Button inCorrectChoice = (Button) getChoiceButton(choiceLetter);
            inCorrectChoice.setBackgroundColor(Color.RED); //incorrect answer effect
        }

        Toast t = Toast.makeText(this , result, Toast.LENGTH_SHORT);
        t.show(); //turn this to actual display on screen

        mNextButton.setVisibility(View.VISIBLE);
        choiceButtonsEnabled(false);
    }

    private Button getChoiceButton(char letter)
    {
        switch(letter) {
            case 'W': return mChoiceW;
            case 'X': return mChoiceX;
            case 'Y': return mChoiceY;
            case 'Z': return mChoiceZ;
            default:  return null; //throw an error instead
        }
    }

    private void choiceButtonsEnabled(boolean isEnabled)
    {
        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setEnabled(isEnabled);
    }
}
