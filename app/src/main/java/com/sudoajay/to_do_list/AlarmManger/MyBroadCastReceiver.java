package com.sudoajay.to_do_list.AlarmManger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.sudoajay.to_do_list.DataBase.Main_DataBase;
import com.sudoajay.to_do_list.ForegroundService.ForegroundServiceBoot;
import com.sudoajay.to_do_list.ForegroundService.TraceBackgroundService;
import com.sudoajay.to_do_list.Notification.Alert_Notification;

import java.util.Calendar;
import java.util.Objects;

public class MyBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TraceBackgroundService traceBackgroundService = new TraceBackgroundService(context);
        if (traceBackgroundService.isBackgroundServiceWorking()) {
            if (Objects.requireNonNull(intent).getAction() != null) {
                Type_C_Task(context);
            } else {
                Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show();
                Log.d("onReceive", "Done W get");

                // alert Notification
                Alert_Notification alert_notification = new Alert_Notification();
                alert_notification.notify(context, "Alert ", 0);

                Type_C_Task(context);
            }
        } else {
            Intent serviceIntent = new Intent(context, ForegroundServiceBoot.class);
            serviceIntent.putExtra("caller", "RebootReceiver");
            context.startService(serviceIntent);
        }
    }

    private void Type_C_Task(Context context) {

        Main_DataBase main_dataBase = new Main_DataBase(context);
        if (!main_dataBase.check_For_Empty()) {

            Calendar calendar = Calendar.getInstance();
            int current_Year = calendar.get(Calendar.YEAR);
            int current_Month = calendar.get(Calendar.MONTH);
            int current_Day = calendar.get(Calendar.DAY_OF_MONTH);
            int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
            int current_minute = calendar.get(Calendar.MINUTE), hour, minute, total_Minute = 24;

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, MyBroadCastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            // for today date
            String today_Date = current_Day + "-" + current_Month + "-" + current_Year;

            Cursor cursor = main_dataBase.Get_The_Id_Name_Original_Time_From_Date_Done_OriginalTime(today_Date, 0);
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                do {
                    if (!cursor.getString(2).equals("")) {
                        String[] split = cursor.getString(2).split(":");
                        hour = cursor.getInt(3);
                        minute = Integer.parseInt(split[1].substring(0, 2));
                    } else {
                        hour = 16;
                        minute = 0;
                    }

                    total_Minute = ((hour - current_hour) * 60) + (minute - current_minute);

                    Log.d("getTotalTime", total_Minute + "  ---  " + cursor.getString(2));
                } while (cursor.moveToNext() && total_Minute <= 0);

//                if there is no task for today
                if (total_Minute == 24 || total_Minute <= 0)
                    total_Minute = ((24 - current_hour) * 60) + (60 - current_minute);


                startAlarm(alarmManager, pendingIntent, total_Minute);
            }
        }
    }

    private void startAlarm(final AlarmManager alarmManager, final PendingIntent pendingIntent, final int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + (1000 * 60 * minute), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        }

    }

    private void cancelAlarm(final AlarmManager alarmManager, final PendingIntent pendingIntent) {
        alarmManager.cancel(pendingIntent);
    }

}
