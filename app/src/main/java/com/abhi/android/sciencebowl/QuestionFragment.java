package com.abhi.android.sciencebowl;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by abhi on 7/7/2016.
 */
public abstract class QuestionFragment extends Fragment {
    protected TextView mQuestionButton;
    protected Button mChoiceW;
    protected Button mChoiceX;
    protected Button mChoiceY;
    protected Button mChoiceZ;
    protected Button[] mChoiceButtonList;

    protected TextView getChoiceButton(Choice choice) {
        switch(choice) {
            case W: return mChoiceW;
            case X: return mChoiceX;
            case Y: return mChoiceY;
            case Z: return mChoiceZ;
            default:  throw new IllegalArgumentException("Invalid letter choice: " + choice.toString());
        }
    }

    protected void updateQuestionWidgets(Question question) {
        mQuestionButton.setText(question.getQuestion());
        mChoiceW.setText("W) " + question.getW());
        mChoiceX.setText("X) " + question.getX());
        mChoiceY.setText("Y) " + question.getY());
        mChoiceZ.setText("Z) " + question.getZ());

        setAllChoiceButtonsTextColor(Color.BLACK);
    }

    protected void setChoiceButtonColor (Choice choice, int color) {
        TextView choiceButton = getChoiceButton(choice);
        choiceButton.setTextColor(color);
    }

    protected void setChoiceButtonsEnabled(boolean isEnabled)
    {
        for(TextView choiceButton : mChoiceButtonList)
            choiceButton.setEnabled(isEnabled);
    }

    protected void setAllChoiceButtonsTextColor(int color) {
        for(TextView choiceButton : mChoiceButtonList)
            choiceButton.setTextColor(color);
    }
}
