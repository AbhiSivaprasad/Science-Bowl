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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, RandomQuestion.QuestionInterface {
    private static final String FIREBASE_QUIZLIST_URL =
            "https://science-bowl.firebaseio.com/quizlist";

    private static TextView mQuestionButton;
    private static Button mChoiceW;
    private static Button mChoiceX;
    private static Button mChoiceY;
    private static Button mChoiceZ;
    private static Button[] mChoiceButtonList;

    private static Button mNextButton;

    private static List<Question> mQuestionBank;
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

        writeToFirebaseLeaderboard(UserInformation.getUsername(), mQuestionsCorrect);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionBank = new ArrayList<>();

        mReviewQuestionsBank = UserInformation.getReviewQuestionBank();
        mCurrentQuestionIndex = UserInformation.getCurrentQuestionIndex();
        mQuestionsCorrect = UserInformation.getQuestionsCorrect();
        userSetting = UserInformation.getUserSettings();
        rq = new RandomQuestion(this, userSetting);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES).build();

        mQuestionButton = (TextView) findViewById(R.id.question);
        mChoiceW = (Button) findViewById(R.id.choiceW);
        mChoiceX = (Button) findViewById(R.id.choiceX);
        mChoiceY = (Button) findViewById(R.id.choiceY);
        mChoiceZ = (Button) findViewById(R.id.choiceZ);
        mChoiceButtonList = new Button[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};

        mNextButton = (Button) findViewById(R.id.next_button);

//        Firebase.setAndroidContext(this);
//        //Takes some time to get data. Executes subsequent code before data is retrieved causing a lag between inflation of
//        //XML and the appearance of question/answers. Need to fix this.
//        Firebase mFirebaseRef = new Firebase(FIREBASE_QUIZLIST_URL);
//        mFirebaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren())
//                {
//                    Question q = snapshot.getValue(Question.class);
//                    mQuestionBank.add(q);
//                }
//                goToNextQuestion(); //load first question
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });
        // load first question
        rq.next();

        for(Button choiceButton : mChoiceButtonList) {
            choiceButton.setBackgroundColor(Color.TRANSPARENT);
            choiceButton.setOnClickListener(this);
        }

        //attach listener to NextButton and make it initially invisible
        mNextButton.setVisibility(View.GONE);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mCurrentQuestionIndex = (mCurrentQuestionIndex + 1) % mQuestionBank.size(); //questions fed in circular loop. Need more sophisticated order.
//                goToNextQuestion();
                mNextButton.setVisibility(View.GONE);
                rq.next();
            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {}

    @Override
    public void onConnectionSuspended(int cause) {}

    @Override
    public void onConnectionFailed(ConnectionResult result) {}

    //update UI and mCurrQuestion
    private static void goToNextQuestion() {
        mNextButton.setVisibility(View.GONE);
//        updateQuestionWidgets();

        setChoiceButtonsEnabled(true);
    }

    public static void updateQuestionWidgets(Question question) {
//        mCurrentQuestion = mQuestionBank.get(mCurrentQuestionIndex);
        mCurrentQuestion = question;
        mQuestionButton.setText(mCurrentQuestion.getQuestion());
        mChoiceW.setText("W) " + mCurrentQuestion.getW());
        mChoiceX.setText("X) " + mCurrentQuestion.getX());
        mChoiceY.setText("Y) " + mCurrentQuestion.getY());
        mChoiceZ.setText("Z) " + mCurrentQuestion.getZ());

        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(View view) { //clean up this method
        Button selectedChoice = (Button) view;

        Choice choice = Choice.valueOf(selectedChoice.getText().toString().substring(0, 1).toUpperCase()); // Letter comes first in answer choice
        Choice answer = mCurrentQuestion.getCorrect();

        boolean isCorrect = (answer == choice);

        Button correctChoice = getChoiceButton(answer, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);
        correctChoice.setTextColor(Color.GREEN); //correct answer effect always shown

        String result;
        if(isCorrect) {
            result = "Correct";
            mQuestionsCorrect++;
        }
        else {
            result = "Incorrect";

            mReviewQuestionsBank.add(new QuestionUserAnswerPair(mCurrentQuestion, choice));

            Button incorrectChoice = getChoiceButton(choice, mChoiceW, mChoiceX, mChoiceY, mChoiceZ);
            incorrectChoice.setTextColor(Color.RED); //incorrect answer effect
        }

        Toast t = Toast.makeText(this , result, Toast.LENGTH_SHORT);
        t.show(); //turn this to actual display on screen

        setChoiceButtonsEnabled(false);

        //stop at last question
        //need different end condition. Index will not be incremented constantly by 1.
//        if(mCurrentQuestionIndex != mQuestionBank.size() - 1)
//            mNextButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setQuestion(Question question) {
        updateQuestionWidgets(question);
        setChoiceButtonsEnabled(true);
    }

    public static Button getChoiceButton(Choice choice, Button mChoiceW, Button mChoiceX, Button mChoiceY, Button mChoiceZ)
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
        for(Button choiceButton : mChoiceButtonList)
            choiceButton.setEnabled(isEnabled);
    }

    private void writeToFirebaseLeaderboard(String userName, int toWrite)
    {
        Firebase mFirebaseRef = new Firebase("https://science-bowl.firebaseio.com/leaderboard");
        mFirebaseRef.child(userName).setValue(toWrite);
    }
}
