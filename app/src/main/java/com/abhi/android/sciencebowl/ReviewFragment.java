package com.abhi.android.sciencebowl;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends QuestionFragment {
    private static final String ARG_QUESTION = "QUESTION";

    private QuestionUserAnswerPair currentQuestionAnswerPair;
    private Question currentQuestion;

    private List<QuestionUserAnswerPair> mReviewQuestionBank;
    private int mCurrentQuestionIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewQuestionBank = new ArrayList<QuestionUserAnswerPair>(UserInformation.getReviewQuestionBank());

        mCurrentQuestionIndex = getArguments().getInt(ARG_QUESTION);

        currentQuestionAnswerPair = mReviewQuestionBank.get(mCurrentQuestionIndex);
        currentQuestion = currentQuestionAnswerPair.getQuestion();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        mQuestionButton = (TextView) view.findViewById(R.id.question);
        mChoiceW = (Button) view.findViewById(R.id.choiceW);
        mChoiceX = (Button) view.findViewById(R.id.choiceX);
        mChoiceY = (Button) view.findViewById(R.id.choiceY);
        mChoiceZ = (Button) view.findViewById(R.id.choiceZ);
        mChoiceButtonList = new Button[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};
        
        displayQuestion();

        return view;
    }

    public static ReviewFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION, index);

        ReviewFragment rf = new ReviewFragment();
        rf.setArguments(args);
        return rf;
    }

    private void displayQuestion() {

        //Buttons cannot be pressed during review
        setChoiceButtonsEnabled(false);

        //Initialize text color of choice buttons
        //setAllChoiceButtonsTextColor(Color.BLACK);
        
        updateQuestionWidgets(currentQuestion);

        Choice answer = currentQuestion.getCorrect();
        setChoiceButtonColor(answer, Color.GREEN);

        Choice userAnswer = currentQuestionAnswerPair.getAnswer();
        setChoiceButtonColor(userAnswer, Color.RED);
    }

}
