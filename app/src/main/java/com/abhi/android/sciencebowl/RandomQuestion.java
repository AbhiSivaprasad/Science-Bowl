package com.abhi.android.sciencebowl;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomQuestion {
    // Exposes QuestionInterface, which must be implemented by caller.
    // The setQuestion() method of the caller will be called by the next() method of this class.
    private static final String FIREBASE_QUESTIONS_URL = "https://science-bowl.firebaseio.com/final/";

    private int index;
    private List<Subject> subjectList;
    private Settings settings;
    private QuestionInterface caller;

    RandomQuestion(QuestionInterface caller, Settings settings) {
        index = 0;
        subjectList = settings.getSubjects();
        this.settings = settings;
        this.caller = caller;
    }

    //returns difficulty of question
    public void next() {
        // if we have gone through the subjects, shuffle the order TODO revise implementation (not all subjects equal frequency?)
        if (index == 0)
            Collections.shuffle(subjectList);

        next(subjectList.get(index));
        index = (index + 1) % subjectList.size();
    }

    public void next(final Subject subject) {
        Firebase firebaseRef = new Firebase(FIREBASE_QUESTIONS_URL + subject.toString());
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot subjectDataSnapshot) {
                int diff = -1;
                Random rand = new Random();

                int subsubjectCount = (int)subjectDataSnapshot.getChildrenCount();
                if (subsubjectCount == 0) return;

                // get random subsubject. hacky because no way to access firebase node by index
                DataSnapshot subsubjectDataSnapshot = null;
                int subsubjectIndex = 0;
                int randomIndex = rand.nextInt(subsubjectCount);
                for(DataSnapshot subsubject : subjectDataSnapshot.getChildren()) {
                    if (subsubjectIndex == randomIndex) {
                        subsubjectDataSnapshot = subsubject;
                        break;
                    }
                    subsubjectIndex++;
                }


                if (subsubjectDataSnapshot != null) {
                    diff = settings.getRandomDiff();
                    DataSnapshot difficultyDataSnapshot =
                            subsubjectDataSnapshot.child(Integer.toString(diff));
                    // get random question. hacky because no way to access firebase node by index
                    int questionCount = (int)difficultyDataSnapshot.getChildrenCount();
                    if (questionCount == 0) return;

                    Question question;
                    int questionIndex = 0;
                    randomIndex = rand.nextInt(questionCount);
                    for(DataSnapshot questionDataSnapshot : difficultyDataSnapshot.getChildren()) {
                        if (questionIndex == randomIndex) {
//                            question = questionDataSnapshot.getValue(Question.class);  // TODO get this to work instead of the lines below up until the constructor

                            String questionStr = questionDataSnapshot.child("question").getValue().toString();
                            String W = questionDataSnapshot.child("w").getValue().toString();
                            String X = questionDataSnapshot.child("x").getValue().toString();
                            String Y = questionDataSnapshot.child("y").getValue().toString();
                            String Z = questionDataSnapshot.child("z").getValue().toString();
                            Choice correct = Choice.valueOf(questionDataSnapshot.child("correct").getValue().toString());
                            question = new Question(subject, questionStr, W, X, Y, Z, correct);

                            caller.setQuestion(question, diff);
                            break;
                        }
                        questionIndex++;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }



    public interface QuestionInterface {
        void setQuestion(Question question, int difficulty);
    }
}
