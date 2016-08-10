package com.abhi.android.sciencebowl;

import android.util.SparseArray;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomQuestion {
    // Exposes QuestionInterface, which must be implemented by caller.
    // The setQuestion() method of the caller will be called by the next() method of this class.
    private static final String TAG = RandomQuestion.class.getName();
    private static final String FIREBASE_QUESTIONS_URL = "https://science-bowl.firebaseio.com/final/";

    private int subjectIndex;
    private List<Subject> subjectList;
    private Settings settings;
    private QuestionInterface caller;
    private HashMap<String, HashMap<String, SparseArray<List<Integer>>>> questionCache;

    RandomQuestion(QuestionInterface caller, Settings settings) {
        this.settings = settings;
        this.caller = caller;
        subjectIndex = 0;
        subjectList = settings.getSubjects();
        questionCache = new HashMap<>();
    }

    public void next() {
        // if we have gone through the subjects, shuffle the order
        if (subjectIndex == 0)
            Collections.shuffle(subjectList);

        next(subjectList.get(subjectIndex));
        subjectIndex = (++subjectIndex) % subjectList.size();
    }

    public void next(final Subject subject) {
        Firebase firebaseRef = new Firebase(FIREBASE_QUESTIONS_URL + subject.toString());
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot subjectDataSnapshot) {
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
                    int difficulty = DifficultyDistribution.getDistribution(settings.getDifficulty()).getRandomDifficulty();
                    DataSnapshot difficultyDataSnapshot =
                            subsubjectDataSnapshot.child(Integer.toString(settings.getDifficulty()));  // TODO replace "settings.getDifficulty()" with "difficulty" once enough questions in db
                    // get random question. hacky because no way to access firebase node by index
                    int questionCount = (int)difficultyDataSnapshot.getChildrenCount();
                    if (questionCount == 0) return;

                    Question question;
                    int questionIndex = 0;
                    randomIndex = getRandomQuestionIndex(subject, subsubjectDataSnapshot.getKey(), settings.getDifficulty(), questionCount);  // TODO replace "settings.getDifficulty()" with "difficulty" once enough questions in db
                    for(DataSnapshot questionDataSnapshot : difficultyDataSnapshot.getChildren()) {
                        if (questionIndex == randomIndex) {
//                            question = questionDataSnapshot.getValue(Question.class);  // TODO get this to work instead of the lines below up until the constructor

                            String questionStr = questionDataSnapshot.child("question").getValue().toString();
                            String W = questionDataSnapshot.child("w").getValue().toString();
                            String X = questionDataSnapshot.child("x").getValue().toString();
                            String Y = questionDataSnapshot.child("y").getValue().toString();
                            String Z = questionDataSnapshot.child("z").getValue().toString();
                            Choice correct = Choice.valueOf(questionDataSnapshot.child("correct").getValue().toString().toLowerCase());
                            question = new Question(subject, questionStr, W, X, Y, Z, correct);

                            caller.setQuestion(question);
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

    private int getRandomQuestionIndex(Subject subject, String subsubject, int difficulty, int questionCount) {
        String subjectString = subject.toString();

        if (!questionCache.containsKey(subjectString)) {
            questionCache.put(subjectString, new HashMap<String, SparseArray<List<Integer>>>());
        }
        if (!questionCache.get(subjectString).containsKey(subsubject)) {
            questionCache.get(subjectString).put(subsubject, new SparseArray<List<Integer>>(Subject.SIZE));
        }
        if (questionCache.get(subjectString).get(subsubject).indexOfKey(difficulty) < 0
                || questionCache.get(subjectString).get(subsubject).get(difficulty).get(0) == 0) {  // key does not exist or index is back at 0 (questions cycled through)
            questionCache.get(subjectString).get(subsubject).put(difficulty, initializeQuestionArrayList(questionCount));
        }

        List<Integer> questionCacheSpecific = questionCache.get(subjectString).get(subsubject).get(difficulty);
        int index = questionCacheSpecific.get(0);
        questionCacheSpecific = questionCacheSpecific.subList(1, questionCacheSpecific.size());

        int randomQuestionIndex = questionCacheSpecific.get(index);
        index = ++index % questionCacheSpecific.size();  // index is always with respect to shortened questionCacheSpecific (as in the cache with index removed)
        questionCacheSpecific.add(0, index);
        questionCache.get(subjectString).get(subsubject).put(difficulty, questionCacheSpecific);

        return randomQuestionIndex;
    }

    private List<Integer> initializeQuestionArrayList (int questionCount) {
        ArrayList<Integer> questionArrayList = new ArrayList<>(questionCount);
        for (int i = 0; i < questionCount; i++) {
            questionArrayList.add(i);
        }
        Collections.shuffle(questionArrayList);
        questionArrayList.add(0, 0);  // index is first element and starts as 0

        return questionArrayList;
    }

    public interface QuestionInterface {
        void setQuestion(Question question);
    }
}
