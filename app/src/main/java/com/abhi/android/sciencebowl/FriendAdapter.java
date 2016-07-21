package com.abhi.android.sciencebowl;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Tanuj on 7/17/16.
 */
public class FriendAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private ArrayList<User> data;
    private Resources res;

    public FriendAdapter(Context c, ArrayList<User> d, Resources resLocal){
        data = d;
        res = resLocal;

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

        }
        if(data.size() <= 0)
            return vi;

        TextView tv = ((TextView) vi.findViewById(R.id.tvFriend));
        tv.setText(data.get(i).getName());

            /******** Set Item Click Listner for LayoutInflater for each row *******/

        return vi;
    }


}
