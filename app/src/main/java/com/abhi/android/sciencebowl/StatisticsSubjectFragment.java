package com.abhi.android.sciencebowl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatisticsSubjectFragment extends Fragment {
    private static final String ARG_SUBJECT = "com.abhi.android.sciencebowl.ARG_SUBJECT";

    private Subject mSubject;

    public static StatisticsSubjectFragment newInstance(int position) {
        StatisticsSubjectFragment fragment = new StatisticsSubjectFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SUBJECT, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubject = getArguments() == null ?
                Subject.SUBJECTS[0] : Subject.SUBJECTS[getArguments().getInt(ARG_SUBJECT)];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subject_statistics, container, false);
        TextView subjectTv = (TextView) v.findViewById(R.id.subject_name);
        subjectTv.setText(mSubject.toString());
        return v;
    }
}
