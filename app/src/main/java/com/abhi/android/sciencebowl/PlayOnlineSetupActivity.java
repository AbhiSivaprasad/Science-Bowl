package com.abhi.android.sciencebowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PlayOnlineSetupActivity extends AppCompatActivity {

    FriendAdapter f;
    ArrayList<FriendAdapter.User> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online_setup);
        ListView lv = (ListView) findViewById(R.id.lvFriends);
        data = new ArrayList<FriendAdapter.User>();
        f = new FriendAdapter(this,data,getResources());
        lv.setAdapter(f);
        GraphRequest request1 = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
            JSONArray j;
            @Override
            public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {

                try {
                    j = jsonArray.getJSONArray(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(PlayOnlineSetupActivity.this, "" + jsonArray.length(), Toast.LENGTH_SHORT).show();
            }
        });
        request1.executeAsync();
    }
}
