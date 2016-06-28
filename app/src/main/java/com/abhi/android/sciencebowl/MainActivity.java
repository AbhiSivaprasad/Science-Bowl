package com.abhi.android.sciencebowl;

import android.content.Context;
import android.content.Intent;
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
    private static final String EXTRA_STATISTICS_STORED =
            "com.abhi.android.sb.statistics_stored";

    private static TextView mQuestion;
    private static Button mChoiceW;
    private static Button mChoiceX;
    private static Button mChoiceY;
    private static Button mChoiceZ;
    private static Button[] mChoiceButtonList;

    private static Button mNextButton;

    private static List<Question> mQuestionBank = new ArrayList<Question>();
    private static Question mCurrQuestion = null;
    private static int mCurrQuestionIndex = 0;

    //scoring vars preserve over screen rotation
    private int mQuestionsCorrect = 0;
    private List<QuestionAnswer> mReviewQuestionsBank = new ArrayList<QuestionAnswer>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInformation.setReviewQuestionBank(mReviewQuestionsBank);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //use setting to pull questions of correct difficulty and subject from Firebase
        Settings userSetting = UserInformation.getUserSettings();


        Firebase.setAndroidContext(this);
        //Takes some time to get data. Executes subsequent code before data is retrieved causing a lag between inflation of
        //XML and the appearance of question/answers. Need to fix this.
        Firebase mFirebaseRef = new Firebase("https://science-bowl.firebaseio.com/quizlist");
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
        for(Button choiceButton : mChoiceButtonList) {
            choiceButton.setBackgroundColor(Color.TRANSPARENT);
            choiceButton.setOnClickListener(this);
        }

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
    private static void updateQuestion() {
        mNextButton.setVisibility(View.GONE);
        updateQuestionWidgets();

        choiceButtonsEnabled(true);
    }

    public static void updateQuestionWidgets() {
        mCurrQuestion = mQuestionBank.get(mCurrQuestionIndex);
        mQuestion.setText(mCurrQuestion.getQuestion());
        mChoiceW.setText("W) " + mCurrQuestion.getW());
        mChoiceX.setText("X) " + mCurrQuestion.getX());
        mChoiceY.setText("Y) " + mCurrQuestion.getY());
        mChoiceZ.setText("Z) " + mCurrQuestion.getZ());

        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(View view) { //clean up this method
        Button selectedChoice = (Button) view;

        char choiceLetter = ((String) selectedChoice.getText()).charAt(0); // Letter comes first in answer choice
        char answer = mCurrQuestion.getCorrect().toUpperCase().charAt(0);

        boolean isCorrect = (answer == choiceLetter);

        Button correctChoice = getChoiceButton(answer, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);
        correctChoice.setTextColor(Color.GREEN); //correct answer effect always shown

        String result;
        if(isCorrect) {
            result = "Correct";
            mQuestionsCorrect++;
        }
        else {
            result = "Incorrect";

            mReviewQuestionsBank.add(new QuestionAnswer(mCurrQuestion, choiceLetter));

            Button incorrectChoice = getChoiceButton(choiceLetter, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);
            incorrectChoice.setTextColor(Color.RED); //incorrect answer effect
        }

        Toast t = Toast.makeText(this , result, Toast.LENGTH_SHORT);
        t.show(); //turn this to actual display on screen

        choiceButtonsEnabled(false);

        //stop at last question
        //need different end condition. Index will not be incremented constantly by 1.
        if(mCurrQuestionIndex != mQuestionBank.size() - 1)
            mNextButton.setVisibility(View.VISIBLE);
        else {
            sendBackStatistics(mQuestionsCorrect);
        }

    }

    public static Button getChoiceButton(char letter, Button mChoiceW, Button mChoiceX, Button mChoiceY, Button mChoiceZ)
    {
        switch(letter) {
            case 'W': return mChoiceW;
            case 'X': return mChoiceX;
            case 'Y': return mChoiceY;
            case 'Z': return mChoiceZ;
            default:  return null; //throw an error instead
        }
    }

    private static void choiceButtonsEnabled(boolean isEnabled)
    {
        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setEnabled(isEnabled);
    }

    public static int getQuestionsCorrect(Intent data) {
        return data.getIntExtra(EXTRA_STATISTICS_STORED, 0);
    }

    private void sendBackStatistics (int questionsCorrect)
    {
        Intent data = new Intent();
        data.putExtra(EXTRA_STATISTICS_STORED, questionsCorrect);
        setResult(RESULT_OK, data);
    }
}
