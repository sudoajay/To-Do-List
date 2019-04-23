package com.sudoajay.to_do_list.AlarmManger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class CallAlarmManger {

    private Context context;

    public CallAlarmManger(final Context context){
        this.context =context;
        Task();
    }

    private void Task() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, MyBroadCastReceiver.class);
        alarmIntent.setAction("JustCallTask");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        startAlarm(alarmManager, pendingIntent);
    }

    private void startAlarm(final AlarmManager alarmManager, final PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() , pendingIntent);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        }

    }

    private void cancelAlarm(final AlarmManager alarmManager, final PendingIntent pendingIntent
            , final Context context) {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_LONG).show();
    }
}
