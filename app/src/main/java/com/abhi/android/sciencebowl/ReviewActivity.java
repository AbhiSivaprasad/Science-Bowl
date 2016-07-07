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
    private TextView mChoiceW;
    private TextView mChoiceX;
    private TextView mChoiceY;
    private TextView mChoiceZ;
    private TextView[] mChoiceButtonList;

    private List<QuestionUserAnswerPair> mReviewQuestionBank;

    private Button mNextButton;
    private Button mPrevButton;

    private int mCurrentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initializeVariables();

        for(TextView choiceButton : mChoiceButtonList) {
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

        for(TextView choiceButton : mChoiceButtonList)
            choiceButton.setTextColor(Color.BLACK);

        QuestionUserAnswerPair mCurrQuestionAnswer = mReviewQuestionBank.get(mCurrentQuestionIndex);

        Question mCurrQuestion = mCurrQuestionAnswer.getQuestion();
        mQuestionButton.setText(mCurrQuestion.getQuestion());
        mChoiceW.setText("W) " + mCurrQuestion.getW());
        mChoiceX.setText("X) " + mCurrQuestion.getX());
        mChoiceY.setText("Y) " + mCurrQuestion.getY());
        mChoiceZ.setText("Z) " + mCurrQuestion.getZ());

        Choice answer = mCurrQuestion.getCorrect();
        Choice userAnswer = mCurrQuestionAnswer.getAnswer();

        TextView correctChoice = MainActivity.getChoiceButton(answer, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);
        TextView incorrectChoice = MainActivity.getChoiceButton(userAnswer, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);

        correctChoice.setTextColor(Color.GREEN);
        incorrectChoice.setTextColor(Color.RED);
    }

    private void initializeVariables()
    {
        mReviewQuestionBank = UserInformation.getReviewQuestionBank();

        mQuestionButton = (TextView) findViewById(R.id.question);
        mChoiceW = (TextView) findViewById(R.id.choiceW);
        mChoiceX = (TextView) findViewById(R.id.choiceX);
        mChoiceY = (TextView) findViewById(R.id.choiceY);
        mChoiceZ = (TextView) findViewById(R.id.choiceZ);
        mChoiceButtonList = new TextView[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
    }
}
