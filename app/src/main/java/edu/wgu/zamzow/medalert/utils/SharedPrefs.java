package edu.wgu.zamzow.medalert.utils;

import android.content.Context;
import android.content.SharedPreferences;

import edu.wgu.zamzow.medalert.R;

public class SharedPrefs {

    private SharedPreferences sharedPref;
    private Context context;

    public SharedPrefs(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(context.getString(R.string.prefence_file_key), Context.MODE_PRIVATE);
    }

    public boolean getLoginStatus() {
        return sharedPref.getBoolean("isLoggedIn",false);
    }

    public String getLogin() {
        Vars.userID = sharedPref.getInt("userName",0);
        return Vars.userName;
    }

    public void setLoginStatus() {
        sharedPref.edit().putBoolean("isLoggedIn",true).apply();
    }

    public void setLoginName(int userID) {
        sharedPref.edit().putInt("userName", userID).apply();
    }
}
