package com.app.adarshan.selifie;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by nepal on 13/03/2016.
 */
public class UserLocalStore
{
    public static final String SP_NAME = "pictures";
    SharedPreferences userLocalStore;
    public UserLocalStore(Context context)
    {
        userLocalStore = context.getSharedPreferences(SP_NAME, 0);
    }


    public void setPictures(int times)
    {
        SharedPreferences.Editor editor = userLocalStore.edit();
        editor.putInt("times", times);
        editor.commit();
    }

    public int getTimes()
    {
       return userLocalStore.getInt("times",0);
    }

    public void clearUserData()
    {
        SharedPreferences.Editor editor = userLocalStore.edit();
        editor.clear();
        editor.commit();
    }

}
