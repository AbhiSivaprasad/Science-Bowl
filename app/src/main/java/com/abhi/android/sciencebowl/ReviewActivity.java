package com.abhi.android.sciencebowl;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private TextView mQuestionButton;
    private Button mChoiceW;
    private Button mChoiceX;
    private Button mChoiceY;
    private Button mChoiceZ;
    private Button[] mChoiceButtonList;

    private List<QuestionAnswer> mReviewQuestionBank;

    private Button mNextButton;
    private Button mPrevButton;

    private int mCurrentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initializeVariables();

        for(Button choiceButton : mChoiceButtonList) {
            choiceButton.setBackgroundColor(Color.TRANSPARENT);
            choiceButton.setEnabled(false);
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentQuestionIndex++;
                goToNextQuestion();
                if(mCurrentQuestionIndex == mReviewQuestionBank.size() - 1) {
                    mNextButton.setVisibility(View.GONE);
                }
            }
        });

        mPrevButton.setVisibility(View.GONE);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentQuestionIndex--;
                if(mCurrentQuestionIndex == 0) {
                    mPrevButton.setVisibility(View.GONE);
                }
                goToNextQuestion();
            }
        });

        if(mReviewQuestionBank.size() != 0) {goToNextQuestion(); }
        else {
            mQuestionButton.setText("No Questions to Review");
            mNextButton.setVisibility(View.GONE);
            mPrevButton.setVisibility(View.GONE);
        }
    }

    //similar method in mainactivity. try to merge.
    private void goToNextQuestion() {

        if (mCurrentQuestionIndex != 0 && mPrevButton.getVisibility() == View.GONE)
            mPrevButton.setVisibility(View.VISIBLE);

        if (mCurrentQuestionIndex < mReviewQuestionBank.size())
            mNextButton.setVisibility(View.VISIBLE);

        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setTextColor(Color.BLACK);

        QuestionAnswer mCurrQuestionAnswer = mReviewQuestionBank.get(mCurrentQuestionIndex);

        Question mCurrQuestion = mCurrQuestionAnswer.getQuestion();
        mQuestionButton.setText(mCurrQuestion.getQuestion());
        mChoiceW.setText("W) " + mCurrQuestion.getW());
        mChoiceX.setText("X) " + mCurrQuestion.getX());
        mChoiceY.setText("Y) " + mCurrQuestion.getY());
        mChoiceZ.setText("Z) " + mCurrQuestion.getZ());

        char answer = mCurrQuestion.getCorrect().toUpperCase().charAt(0);
        char userAnswer = mCurrQuestionAnswer.getAnswer();

        Button correctChoice = MainActivity.getChoiceButton(answer, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);
        Button incorrectChoice = MainActivity.getChoiceButton(userAnswer, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);

        correctChoice.setTextColor(Color.GREEN);
        incorrectChoice.setTextColor(Color.RED);
    }

    private void initializeVariables()
    {
        mReviewQuestionBank = UserInformation.getReviewQuestionBank();

        mQuestionButton = (TextView) findViewById(R.id.question);
        mChoiceW = (Button) findViewById(R.id.choiceW);
        mChoiceX = (Button) findViewById(R.id.choiceX);
        mChoiceY = (Button) findViewById(R.id.choiceY);
        mChoiceZ = (Button) findViewById(R.id.choiceZ);
        mChoiceButtonList = new Button[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
    }
}
