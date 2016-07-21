package com.abhi.android.sciencebowl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class PlayOnlineSetupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FriendAdapter f;
    ArrayList<User> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online_setup);
        ListView lv = (ListView) findViewById(R.id.lvFriends);
        data = new ArrayList<User>();
        f = new FriendAdapter(this,data,getResources());
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
                Toast.makeText(PlayOnlineSetupActivity.this, "" + jsonArray.length(), Toast.LENGTH_SHORT).show();
            }
        });
        request1.executeAsync();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String link = data.get(i).getUid();
        if (link != null) {
            Intent intent = new Intent(this, Class.class);
            intent.putExtra(PlayOnlineActivity.UID_KEY, data.get(i).getUid());
            startActivity(intent);
        }
    }
}
