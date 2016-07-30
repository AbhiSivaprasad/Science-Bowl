package com.abhi.android.sciencebowl;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Tanuj on 7/17/16.
 */
public class FriendAdapter extends BaseAdapter implements View.OnClickListener {
    private final LayoutInflater inflater;
    private ArrayList<User> data;
    private Resources res;
    private Context context;
    private boolean cFrag;

    public FriendAdapter(Context c, ArrayList<User> d, Resources resLocal, boolean challengeFragment){
        data = d;
        res = resLocal;
        context = c;
        cFrag = challengeFragment;
        /*********** Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View vi = convertView;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.friend, null);
            vi.setFocusable(true);
        }
        if(data.size() <= 0)
            return vi;

        TextView tv = ((TextView) vi.findViewById(R.id.tvFriend));
        tv.setText(data.get(i).getName());
        Button b = (Button) vi.findViewById(R.id.bChallenge);
        b.setTag(i);
        b.setOnClickListener(this);
            /******** Set Item Click Listner for LayoutInflater for each row *******/
        return vi;
    }

    @Override
    public void onClick(View view) {
        int i = (int) view.getTag();
        String link = data.get(i).getUid();
        if (link != null) {
            Intent intent = new Intent(context, PlayOnlineActivity.class);
            intent.putExtra(PlayOnlineFragment.UID_KEY, data.get(i).getUid());
            intent.putExtra(PlayOnlineFragment.NAME_KEY, data.get(i).getName());
            if(cFrag){
                intent.putExtra(PlayOnlineFragment.GAME_KEY,ChallengeFragment.keys.get(i));
            }
            context.startActivity(intent);
        }
    }
}
