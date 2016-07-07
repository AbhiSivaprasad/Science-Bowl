package com.abhi.android.sciencebowl;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RandomQuestion.QuestionInterface {

    private static TextView mQuestionButton;
    private static TextView mChoiceW;
    private static TextView mChoiceX;
    private static TextView mChoiceY;
    private static TextView mChoiceZ;
    private static TextView[] mChoiceButtonList;

    private static Button mNextButton;

    private static Question mCurrentQuestion;
    private static int mCurrentQuestionIndex;

    //scoring vars preserve over screen rotation
    private int mQuestionsCorrect;
    private List<QuestionUserAnswerPair> mReviewQuestionsBank;
    private RandomQuestion rq;

    private GoogleApiClient mGoogleApiClient;

    private Settings userSetting;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInformation.setReviewQuestionBank(mReviewQuestionsBank); //should this be here
        UserInformation.setCurrentQuestionIndex(mCurrentQuestionIndex + 1); //set to next question to be displayed
        UserInformation.setQuestionsCorrect(mQuestionsCorrect); //set statistics

        // fetch leaderboard ID from res/strings.xml and submit to Google Play Games Leaderboard
        String leaderboardIdQuestionsAnswered = getResources().getString(R.string.leaderboard_id_questions_answered);
        if (mGoogleApiClient.isConnected())
            Games.Leaderboards.submitScore(mGoogleApiClient, leaderboardIdQuestionsAnswered, mQuestionsCorrect);

        writeToFirebaseLeaderboard(UserInformation.getUid(), mQuestionsCorrect);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReviewQuestionsBank = UserInformation.getReviewQuestionBank();
        mCurrentQuestionIndex = UserInformation.getCurrentQuestionIndex();
        mQuestionsCorrect = UserInformation.getQuestionsCorrect();
        userSetting = UserInformation.getUserSettings();
        rq = new RandomQuestion(this, userSetting);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES).build();

        mQuestionButton = (TextView) findViewById(R.id.question);
        mChoiceW = (TextView) findViewById(R.id.choiceW);
        mChoiceX = (TextView) findViewById(R.id.choiceX);
        mChoiceY = (TextView) findViewById(R.id.choiceY);
        mChoiceZ = (TextView) findViewById(R.id.choiceZ);
        mChoiceButtonList = new TextView[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};

        mNextButton = (Button) findViewById(R.id.next_button);

        rq.next();

        for(TextView choiceButton : mChoiceButtonList) {
            choiceButton.setBackgroundColor(Color.TRANSPARENT);
            choiceButton.setOnClickListener(this);
        }

        //attach listener to NextButton and make it initially invisible
        mNextButton.setVisibility(View.INVISIBLE);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextButton.setVisibility(View.GONE);
                rq.next();
            }
        });
    }



    public static void updateQuestionWidgets(Question question) {
        mCurrentQuestion = question;
        mQuestionButton.setText(mCurrentQuestion.getQuestion());
        mChoiceW.setText("W) " + mCurrentQuestion.getW());
        mChoiceX.setText("X) " + mCurrentQuestion.getX());
        mChoiceY.setText("Y) " + mCurrentQuestion.getY());
        mChoiceZ.setText("Z) " + mCurrentQuestion.getZ());

        for(TextView choiceButton : mChoiceButtonList)
            choiceButton.setTextColor(Color.BLACK);
    }

    private void updateOnAnswerWidgets(boolean isAnswerCorrect, Choice userChoice, Choice answer) {
        String result = isAnswerCorrect ? "Correct" : "Incorrect";

        choiceButtonSetColor(answer, Color.GREEN);
        if(!isAnswerCorrect)
            choiceButtonSetColor(userChoice, Color.RED);

        Toast t = Toast.makeText(this, result, Toast.LENGTH_SHORT);
        t.show(); //TODO turn this to actual display on screen

        setChoiceButtonsEnabled(false);
        mNextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        TextView selectedChoice = (TextView) view;

        Choice userChoice = Choice.valueOf(selectedChoice.getText().toString().substring(0, 1).toUpperCase()); // Letter comes first in answer choice
        Choice answer = mCurrentQuestion.getCorrect();

        boolean isAnswerCorrect = (answer == userChoice);

        onAnswerResult(isAnswerCorrect, userChoice);
        updateOnAnswerWidgets(isAnswerCorrect, userChoice, answer);
    }

    private void onAnswerResult(boolean isAnswerCorrect, Choice userChoice) {
        if(isAnswerCorrect) {
            mQuestionsCorrect++;
        } else {
            mReviewQuestionsBank.add(new QuestionUserAnswerPair(mCurrentQuestion, userChoice));
        }
    }

    private void choiceButtonSetColor (Choice choice, int color) {
        TextView choiceButton = getChoiceButton(choice, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);
        choiceButton.setTextColor(color);
    }

    @Override
    public void setQuestion(Question question) {
        updateQuestionWidgets(question);
        setChoiceButtonsEnabled(true);
    }

    public static TextView getChoiceButton(Choice choice, TextView mChoiceW, TextView mChoiceX, TextView mChoiceY, TextView mChoiceZ)
    {
        switch(choice) {
            case W: return mChoiceW;
            case X: return mChoiceX;
            case Y: return mChoiceY;
            case Z: return mChoiceZ;
            default:  throw new IllegalArgumentException("Invalid letter choice: " + choice.toString());
        }
    }


    private static void setChoiceButtonsEnabled(boolean isEnabled)
    {
        for(TextView choiceButton : mChoiceButtonList)
            choiceButton.setEnabled(isEnabled);
    }

    private void writeToFirebaseLeaderboard(String userName, int toWrite)
    {
        Firebase mFirebaseRef = new Firebase("https://science-bowl.firebaseio.com/leaderboard");
        mFirebaseRef.child(userName).setValue(toWrite);
    }

    @Override
    public void onConnected(Bundle connectionHint) {}

    @Override
    public void onConnectionSuspended(int cause) {}

    @Override
    public void onConnectionFailed(ConnectionResult result) {}
}
