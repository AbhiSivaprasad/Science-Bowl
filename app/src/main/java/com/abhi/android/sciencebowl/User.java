package com.abhi.android.sciencebowl;

/**
 * Created by Tanuj on 7/20/16.
 */
public class User{
    private String uid;
    private String name;

    public User(String name, String id){
        this.name = name;
        uid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
