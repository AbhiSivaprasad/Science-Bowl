package com.abhi.android.sciencebowl;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayOnlineActivity extends QuestionActivity implements RandomQuestion.QuestionInterface, View.OnClickListener{


    private RandomQuestion rq;
    private Button mNextButton;
    private Settings userSetting;
    private List<Question> questionBank;
    private String opponent = "1234";
    final static String UID_KEY = "uid_key";
    boolean gameExists = false;
    private Question q;
    private Question mCurrentQuestion;
    private String key;
    private boolean mSelfAnswered = false;
    private int mScore = 0;
    private int mScoreOpp = 0;
    protected boolean mPlaying = false;
    private int mQuestionsCorrect  =0;
    private List<QuestionUserAnswerPair> mReviewQuestionsBank;
    private int index = 0;
    final static String SCORE_REF = "score";
    final static String ANSWER_REF = "choice";
    private boolean mOppAnswered = false;
    private Firebase mGames;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online);
        initializeVariables();
        opponent = getIntent().getStringExtra(UID_KEY);
        opponent = "1234";
        generateQuestions(3);
        //startGame

    }

    private void updateOpponentAnswer(Choice c){
        Toast.makeText(this,"Opponent answered: "+c+ " His/her score: "+ String.valueOf(mScoreOpp),Toast.LENGTH_LONG).show();
        boolean correct = (c == mCurrentQuestion.getCorrect());
        mOppAnswered = true;
        if(correct){
            updateWidgetsOnAnswer(true,c,mCurrentQuestion.getCorrect());
            nextQuestion();
        }else{
            setChoiceButtonColor(c, Color.RED);
            if(mSelfAnswered){
                nextQuestion();
            }
        }
    }

    private void nextQuestion() {
        mOppAnswered = false;
        Toast.makeText(this,"Starting next question.",Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        setQuestion(null);
    }

    @Override
    public void onClick(View view) {
        mSelfAnswered = true;
        TextView selectedChoice = (TextView) view;

        Choice userChoice = Choice.valueOf(selectedChoice.getText().toString().substring(0, 1).toUpperCase()); // Letter comes first in answer choice
        Choice answer = mCurrentQuestion.getCorrect();

        boolean isAnswerCorrect = (answer == userChoice);

        onAnswerResult(isAnswerCorrect, userChoice);
        updateWidgetsOnAnswer(isAnswerCorrect, userChoice, answer);
        if(mOppAnswered){
            nextQuestion();
        }
    }

    private void onAnswerResult(boolean isAnswerCorrect, Choice userChoice) {
        if(isAnswerCorrect) {
            mQuestionsCorrect++;
            mScore += 10;
        } else {
            mReviewQuestionsBank.add(new QuestionUserAnswerPair(mCurrentQuestion, userChoice));
        }
        Firebase mRef = new Firebase(getString(R.string.firebase_database_url)+getString(R.string.active_games_url)+"/"+key+"/"+UserInformation.getFbUid());
        mRef.setValue(new GameAttribute(mScore, userChoice.toString()));
    }

    private void updateWidgetsOnAnswer(boolean isAnswerCorrect, Choice userChoice, Choice answer) {
        String result = isAnswerCorrect ? "Correct" : "Incorrect";

        setChoiceButtonColor(answer, Color.GREEN);
        if(!isAnswerCorrect)
            setChoiceButtonColor(userChoice, Color.RED);

        Toast t = Toast.makeText(this, result, Toast.LENGTH_SHORT);
        t.show(); //TODO turn this to actual display on screen

        setChoiceButtonsEnabled(false);
        //mNextButton.setVisibility(View.VISIBLE);
    }

    //
    private void generateQuestions(int i) {
        checkExistingGame(opponent);
        if(!gameExists) {
            setQuestions(opponent, i);
        }else{
            fetchQuestions(opponent);
        }
    }

    private void fetchQuestions(String opponent) {
        questionBank = new LinkedList<Question>();
        Firebase firebaseRef = new Firebase(getString(R.string.firebase_database_url)+getString(R.string.user_states)+"/"+UserInformation.getUid()+"/"+opponent);
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getValue().toString();
                Firebase firebaseRef = new Firebase(getString(R.string.firebase_database_url)+getString(R.string.active_games_url)+"/"+key);
                firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       for(DataSnapshot s : dataSnapshot.getChildren()){
                           questionBank.add(s.getValue(Question.class));
                       }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(PlayOnlineActivity.this,"The fetch was cancelled: 1",Toast.LENGTH_SHORT).show();;
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(PlayOnlineActivity.this,"The fetch was cancelled.",Toast.LENGTH_SHORT).show();;
            }
        });
    }

    private void setQuestions(String opponent, int i) {
        //push new game
        Firebase firebaseRef = new Firebase(getString(R.string.firebase_database_url));
        mGames = firebaseRef.child(getString(R.string.active_games_url).substring(1));
        Firebase p1 = firebaseRef.child(getString(R.string.user_states).substring(1));
        key = mGames.push().getKey();
        String id = UserInformation.getFbUid();
        p1.child(UserInformation.getFbUid()+"/"+opponent).setValue(key);
        p1.child(opponent + "/" + UserInformation.getFbUid()).setValue(key);
        questionBank = new LinkedList<Question>();
        makeBank(i);
    }

    public void makeBank(int num){
        List<Question> ret = new LinkedList<Question>();
        for(int j = 0;j<num;j++) {
            //int i = ((int) (Math.random() * Subject.values().length));
            int i = 1;
            rq.next(Subject.values()[i]);
        }
        //return ret;
    }

    private void checkExistingGame(String opp) {
        Firebase firebaseRef = new Firebase(getString(R.string.firebase_database_url) + getString(R.string.user_states)+"/"+UserInformation.getUid());
        final String opponent = opp;
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameExists = dataSnapshot.hasChild(opponent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(PlayOnlineActivity.this,"The read was cancelled.",Toast.LENGTH_SHORT).show();;
            }
        });
    }

    @Override
    protected void initializeVariables() {
        Log.d("initalize", "BASE INITALIZATION");

        mReviewQuestionsBank = UserInformation.getReviewQuestionBank();
        /*mQuestionsCorrect = UserInformation.getQuestionsCorrect();
        userSetting = UserInformation.getUserSettings();*/
        ArrayList<Subject> subjs = new ArrayList<Subject>();
        for(int i = 0;i<Subject.values().length;i++){
            subjs.add(Subject.values()[i]);
        }
        userSetting = new Settings(subjs, 0);
        rq = new RandomQuestion(this, userSetting);

        mQuestionButton = (TextView) findViewById(R.id.question);
        mChoiceW = (Button) findViewById(R.id.choiceW);
        mChoiceX = (Button) findViewById(R.id.choiceX);
        mChoiceY = (Button) findViewById(R.id.choiceY);
        mChoiceZ = (Button) findViewById(R.id.choiceZ);
        mNextButton = (Button) findViewById(R.id.next_button);
        mChoiceButtonList = new Button[] {mChoiceW, mChoiceX, mChoiceY, mChoiceZ};
        for(Button choiceButton : mChoiceButtonList) {
            //    choiceButton.setBackgroundColor(Color.TRANSPARENT);
            choiceButton.setOnClickListener(this);
        }
    }

    @Override
    public void setQuestion(Question question) {
        if(!mPlaying) {
            questionBank.add(question);
            if(questionBank.size()>=3){
                mPlaying = true;
                mGames.child(key+"/"+"questions").setValue(questionBank);
                final Firebase firebaseRef = new Firebase(getString(R.string.firebase_database_url)+getString(R.string.active_games_url)+"/"+key+"/"+opponent);
                firebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(ANSWER_REF).getValue() == null) {
                            firebaseRef.setValue(new GameAttribute(0,""));
                            return;
                        }
                        String c = dataSnapshot.child(ANSWER_REF).getValue().toString();
                        mScoreOpp = Integer.parseInt(dataSnapshot.child(SCORE_REF).getValue().toString());
                        if(!c.equals(""))
                        updateOpponentAnswer(Choice.valueOf(c));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                mCurrentQuestion = questionBank.get(index++);
                updateQuestionWidgets(mCurrentQuestion);
                setChoiceButtonsEnabled(true);
            }
        }
        else{
            mCurrentQuestion = questionBank.get(index++);
            updateQuestionWidgets(mCurrentQuestion);
            setChoiceButtonsEnabled(true);
        }
        index = index % questionBank.size();
    }

    private class GameAttribute {

        public GameAttribute(int sc, String ch){
            score = sc;
            choice = ch;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        private int score;

        public String getChoice() {
            return choice;
        }

        public void setChoice(String choice) {
            this.choice = choice;
        }

        private String choice;

    }
}
