package com.abhi.android.sciencebowl;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ReviewActivity extends QuestionActivity {

    private List<QuestionUserAnswerPair> mReviewQuestionBank;

    private Button mNextButton;
    private Button mPrevButton;

    private int mCurrentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initializeVariables();

        //Buttons cannot be pressed during review
        setChoiceButtonsEnabled(false);

        //Initialize text color of choice buttons
        setAllChoiceButtonsTextColor(Color.BLACK);

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

    private void goToNextQuestion() {
        if (mCurrentQuestionIndex != 0 && mPrevButton.getVisibility() == View.GONE)
            mPrevButton.setVisibility(View.VISIBLE);

        if (mCurrentQuestionIndex < mReviewQuestionBank.size())
            mNextButton.setVisibility(View.VISIBLE);

        QuestionUserAnswerPair mCurrQuestionAnswer = mReviewQuestionBank.get(mCurrentQuestionIndex);
        Question mCurrQuestion = mCurrQuestionAnswer.getQuestion();
        updateQuestionWidgets(mCurrQuestion);

        Choice answer = mCurrQuestion.getCorrect();
        setChoiceButtonColor(answer, Color.GREEN);

        Choice userAnswer = mCurrQuestionAnswer.getAnswer();
        setChoiceButtonColor(userAnswer, Color.RED);
    }

    @Override
    protected void initializeVariables()
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
