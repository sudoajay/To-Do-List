package com.sudoajay.to_do_list.ForegroundService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.sudoajay.to_do_list.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lincoln on 05/05/16.
 */
public class TraceBackgroundService {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;


    @SuppressLint("CommitPrefEdits")
    public TraceBackgroundService(final Context _context) {
        this._context = _context;
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(_context.getString(R.string.shared_preference_name), PRIVATE_MODE);
        editor = pref.edit();


        // set default value
        if (!pref.contains(_context.getString(R.string.today_task_shared_preference)))
            editor.putString(_context.getString(R.string.today_task_shared_preference), NextDate(1));


        if (!pref.contains(_context.getString(R.string.due_task_shared_preference)))
            editor.putString(_context.getString(R.string.due_task_shared_preference), NextDate(2));

        editor.apply();
    }

    public String getTodayTask() {
        return pref.getString(_context.getString(R.string.today_task_shared_preference), NextDate(1));
    }

    public void setTodayTask() {
        editor.putString(_context.getString(R.string.today_task_shared_preference), NextDate(1));
        editor.apply();


    }

    public String getDueTask() {
        return pref.getString(_context.getString(R.string.due_task_shared_preference), NextDate(2));
    }

    public void setDueTask() {
        editor.putString(_context.getString(R.string.due_task_shared_preference), NextDate(2));
        editor.apply();
    }


    public void setBackgroundServiceWorking(boolean backgroundServiceWorking) {
        editor.putBoolean(_context.getString(R.string.background_service_working), backgroundServiceWorking);
        editor.apply();
    }

    public boolean isBackgroundServiceWorking() {
        return pref.getBoolean(_context.getString(R.string.background_service_working), true);
    }

    public void setForegroundServiceWorking(final boolean foregroundServiceWorking) {
        editor.putBoolean(_context.getString(R.string.foreground_service_working), foregroundServiceWorking);
        editor.apply();
    }

    public boolean isForegroundServiceWorking() {
        return pref.getBoolean(_context.getString(R.string.foreground_service_working), false);
    }

    public static String NextDate(int type) {
        int hour = 0, fixedHour;
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (type == 1)
            fixedHour = 4;
        else {
            fixedHour = 20;
        }
        if (currentHour >= fixedHour) {
            hour = (24 - currentHour) + fixedHour;
        } else {
            hour = fixedHour - currentHour;
        }

        // get Today Date as default
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        calendar.add(Calendar.HOUR, hour);
        dateFormat.setTimeZone(calendar.getTimeZone());
        return dateFormat.format(calendar.getTime());
    }

    public void isBackgroundWorking() {

        // today date
        Calendar calendar = Calendar.getInstance();
        // juts add this for Yesterday
        calendar.add(Calendar.DATE, -1);
        Date yesterDay = calendar.getTime();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date getDate = dateFormat.parse(getTodayTask());
            if (yesterDay.after(getDate))
                setBackgroundServiceWorking(false);

            getDate = dateFormat.parse(getDueTask());
            if (yesterDay.after(getDate))

                setBackgroundServiceWorking(false);


        } catch (ParseException e) {
            setBackgroundServiceWorking(true);
        }

        setBackgroundServiceWorking(true);

    }
}
