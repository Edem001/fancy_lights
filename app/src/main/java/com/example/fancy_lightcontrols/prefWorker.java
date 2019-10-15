package com.example.fancy_lightcontrols;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class prefWorker  {
    SharedPreferences pref;
    public prefWorker(String key, Context context){
        pref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public String getStringValue(int position){
        return pref.getString(position+"", "");
    }
    public int getIntValue(int position){
        return pref.getInt(position+"", 0);
    }

    public void setStringValue(String value, int position){
        SharedPreferences.Editor note = pref.edit();
        note.putString(position+"", value);
        note.commit();
    }
    public void setIntValue(int value, int position){
        SharedPreferences.Editor note = pref.edit();
        note.putInt(position+"", value);
        note.commit();
    }
}
