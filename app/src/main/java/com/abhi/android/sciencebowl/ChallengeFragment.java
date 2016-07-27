package com.abhi.android.sciencebowl;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChallengeFragment extends Fragment {

    FriendAdapter f;
    ArrayList<User> data;
    static ArrayList<String> keys;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_play_online_setup,container,false);
        ListView lv = (ListView) v.findViewById(R.id.lvFriends);
        data = new ArrayList<User>();
        keys = new ArrayList<String>();
        f = new FriendAdapter(getActivity(),data,getResources(),true);
        lv.setAdapter(f);
        (new ChallengeAsync()).execute();
        return v;
    }

    class ChallengeAsync extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Firebase mRef = new Firebase(getString(R.string.firebase_database_url)+getString(R.string.user_states)+"/"+UserInformation.getFbUid());
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        User u = new User("","");
                        u.setName(d.child("name").getValue().toString());
                        u.setUid(d.child("uid").getValue().toString());
                        data.add(u);
                        keys.add(d.getKey());
                    }
                    f.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return null;
        }
    }
}
