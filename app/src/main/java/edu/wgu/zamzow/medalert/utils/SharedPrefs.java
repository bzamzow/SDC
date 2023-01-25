package edu.wgu.zamzow.medalert.utils;

import android.content.Context;
import android.content.SharedPreferences;

import edu.wgu.zamzow.medalert.R;

public class SharedPrefs {

    private final SharedPreferences sharedPref;

    public SharedPrefs(Context context) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.prefence_file_key), Context.MODE_PRIVATE);
    }

    public boolean getLoginStatus() {
        return sharedPref.getBoolean("isLoggedIn",false);
    }

    public int getLogin() {
        Vars.userID = sharedPref.getInt("userName",0);
        return Vars.userID;
    }

    public void setLoginStatus() {
        sharedPref.edit().putBoolean("isLoggedIn",true).apply();
    }

    public void setLoginName(int userID) {
        sharedPref.edit().putInt("userName", userID).apply();
    }
}
