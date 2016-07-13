package com.abhi.android.sciencebowl;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.List;

public class PlayFragment extends QuestionFragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RandomQuestion.QuestionInterface  {

    private static Button mNextButton;
    private static Question mCurrentQuestion;

    private int mQuestionsCorrect;
    private List<QuestionUserAnswerPair> mReviewQuestionsBank;
    private RandomQuestion rq;

    private GoogleApiClient mGoogleApiClient;

    private Settings userSetting;

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserInformation.setReviewQuestionBank(mReviewQuestionsBank); //should this be here
        UserInformation.setQuestionsCorrect(mQuestionsCorrect); //set statistics

        writeToFirebaseLeaderboard(UserInformation.getUid(), mQuestionsCorrect);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(MainMenuActivity.EXTRA_SCORE, mQuestionsCorrect);
        getActivity().setResult(getActivity().RESULT_OK, intent);

        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeVariables();

        rq.next();

        //Initalize transparent background for choice buttons and set listeners
        for(TextView choiceButton : mChoiceButtonList) {
            //    choiceButton.setBackgroundColor(Color.TRANSPARENT);
            choiceButton.setOnClickListener(this);
        }

        //Make next button initially invisible and set a listener
        mNextButton.setVisibility(View.INVISIBLE);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextButton.setVisibility(View.INVISIBLE);
                rq.next();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        mQuestionButton = (TextView) view.findViewById(R.id.question);
        mChoiceW = (Button) view.findViewById(R.id.choiceW);
        mChoiceX = (Button) view.findViewById(R.id.choiceX);
        mChoiceY = (Button) view.findViewById(R.id.choiceY);
        mChoiceZ = (Button) view.findViewById(R.id.choiceZ);
        mNextButton = (Button) view.findViewById(R.id.next_button);
        mChoiceButtonList = new Button[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};

        return view;
    }

    private void updateWidgetsOnAnswer(boolean isAnswerCorrect, Choice userChoice, Choice answer) {
        String result = isAnswerCorrect ? "Correct" : "Incorrect";

        setChoiceButtonColor(answer, Color.GREEN);
        if(!isAnswerCorrect)
            setChoiceButtonColor(userChoice, Color.RED);

        Toast t = Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT);
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
        updateWidgetsOnAnswer(isAnswerCorrect, userChoice, answer);
    }

    private void onAnswerResult(boolean isAnswerCorrect, Choice userChoice) {
        if(isAnswerCorrect) {
            mQuestionsCorrect++;
        } else {
            mReviewQuestionsBank.add(new QuestionUserAnswerPair(mCurrentQuestion, userChoice));
        }
    }

    @Override
    public void setQuestion(Question question) {
        mCurrentQuestion = question;
        updateQuestionWidgets(question);
        setChoiceButtonsEnabled(true);
    }

    private void writeToFirebaseLeaderboard(String userName, int toWrite) {
        Firebase mFirebaseRef = new Firebase("https://science-bowl.firebaseio.com/leaderboard");
        mFirebaseRef.child(userName).setValue(toWrite);
    }

    @Override
    protected void initializeVariables() {
        mReviewQuestionsBank = UserInformation.getReviewQuestionBank();
        mQuestionsCorrect = UserInformation.getQuestionsCorrect();
        userSetting = UserInformation.getUserSettings();

        rq = new RandomQuestion(this, userSetting);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES).build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {}

    @Override
    public void onConnectionSuspended(int cause) {}

    @Override
    public void onConnectionFailed(ConnectionResult result) {}
}
