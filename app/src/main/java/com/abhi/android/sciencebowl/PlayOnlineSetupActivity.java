package com.abhi.android.sciencebowl;

import android.content.Intent;
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
import com.google.firebase.auth.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayOnlineSetupActivity extends Fragment{

    FriendAdapter f;
    ArrayList<User> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_play_online_setup,container,false);
        ListView lv = (ListView) v.findViewById(R.id.lvFriends);
        data = new ArrayList<User>();
        f = new FriendAdapter(getActivity(),data,getResources(),false);
        lv.setAdapter(f);
        GraphRequest request1 = GraphRequest.newMyFriendsRequest(UserInformation.getFbToken(), new GraphRequest.GraphJSONArrayCallback() {
            JSONObject j;
            @Override
            public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                System.out.println(UserInformation.getFbToken()+ "..........."+graphResponse.getRawResponse());
                try {

                    for(int i = 0;i<jsonArray.length();i++) {
                        User u;
                        j = jsonArray.getJSONObject(i);
                        u = new User(j.getString("name"),j.getString("id"));
                        data.add(u);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                f.notifyDataSetChanged();
                Toast.makeText(getActivity(), "" + jsonArray.length(), Toast.LENGTH_SHORT).show();
            }
        });
        request1.executeAsync();
        return v;
    }
}
