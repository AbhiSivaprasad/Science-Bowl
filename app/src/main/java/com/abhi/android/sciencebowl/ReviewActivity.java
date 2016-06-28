package com.abhi.android.sciencebowl;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private TextView mQuestion;
    private Button mChoiceW;
    private Button mChoiceX;
    private Button mChoiceY;
    private Button mChoiceZ;
    private Button[] mChoiceButtonList;

    private List<QuestionAnswer> mReviewQuestionBank;

    private Button mNextButton;

    private int mCurrQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //initialize buttons
        mQuestion = (TextView) findViewById(R.id.question);
        mChoiceW = (Button) findViewById(R.id.choiceW);
        mChoiceX = (Button) findViewById(R.id.choiceX);
        mChoiceY = (Button) findViewById(R.id.choiceY);
        mChoiceZ = (Button) findViewById(R.id.choiceZ);

        mChoiceButtonList = new Button[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};
        for(Button choiceButton : mChoiceButtonList) {
            choiceButton.setBackgroundColor(Color.TRANSPARENT);
            choiceButton.setEnabled(false);
        }

        mReviewQuestionBank = UserInformation.getReviewQuestionBank();

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrQuestionIndex = mCurrQuestionIndex + 1;

                updateQuestionWidgets();
            }
        });

        updateQuestionWidgets();

    }

    //similar method in mainactivity. try to merge.
    private void updateQuestionWidgets() {
        if(mCurrQuestionIndex == mReviewQuestionBank.size()) {
            mNextButton.setVisibility(View.GONE);

            mQuestion.setText("No More Questions to Review");
            for(Button choiceButton : mChoiceButtonList)
                choiceButton.setText("");

            return;
        }

        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setTextColor(Color.BLACK);

        QuestionAnswer mCurrQuestionAnswer = mReviewQuestionBank.get(mCurrQuestionIndex);

        Question mCurrQuestion = mCurrQuestionAnswer.getQuestion();
        mQuestion.setText(mCurrQuestion.getQuestion());
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
}
