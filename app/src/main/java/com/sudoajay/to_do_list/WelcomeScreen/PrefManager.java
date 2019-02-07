package com.sudoajay.to_do_list.WelcomeScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.sudoajay.to_do_list.R;

/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static  String PREF_NAME ;

    private static  String IS_FIRST_TIME_LAUNCH ;

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        this._context = context;
        PREF_NAME = context.getResources().getString(R.string.shared_preference_name);
        IS_FIRST_TIME_LAUNCH =context.getResources().getString(R.string.is_first_time_launch);
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
