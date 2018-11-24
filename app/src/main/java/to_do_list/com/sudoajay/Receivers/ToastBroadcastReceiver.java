package to_do_list.com.sudoajay.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.util.Calendar;

import to_do_list.com.sudoajay.DataBase.Main_DataBase;
import to_do_list.com.sudoajay.DataBase.Setting_Database;
import to_do_list.com.sudoajay.Notification.Notify_Notification;


/**
 * Created by harun on 1/10/18.
 */

public class ToastBroadcastReceiver extends BroadcastReceiver {
    private Setting_Database setting_database;
    private Main_DataBase main_dataBase;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent= new Intent(context, to_do_list.com.sudoajay.IntentServices.BackgroundService.class);
        context.startService(serviceIntent);

        if(intent.getExtras() != null) {
            if (!intent.hasExtra("send_The_Id")) {
                setting_database = new Setting_Database(context);
                main_dataBase = new Main_DataBase(context);

                if (!setting_database.check_For_Empty()) {
                    Cursor cursor = setting_database.Get_All_Date();
                    Calendar today = Calendar.getInstance();
                    if (cursor != null) {
                        cursor.moveToFirst();

                        do {

//                            // is for old Finish Task
//
//                            // date check
//                            if (cursor.getInt(1) <= today.get(Calendar.DATE)) {
//                                // check for Time
//                                if (today.get(Calendar.HOUR) > 4) {
//                                    Cursor cursor1 = main_dataBase.Get_The_Id_From_Done(1);
//                                    if (cursor1 != null) {
//                                        cursor1.moveToFirst();
//                                        do {
//                                            main_dataBase.Delete_Row(cursor.getInt(0) + "");
//
//                                        } while (cursor1.moveToNext());
//
//                                        Toast.makeText(context, "The Data is Refresh", Toast.LENGTH_SHORT).show();
//
//                                        // new date transfer in old Finish Task
//                                        Calendar calendar = Calendar.getInstance();
//                                        calendar.add(Calendar.DATE, 1);
//
//                                        setting_database.Update_The_Old_Fin_Task(1 + "", calendar.get(Calendar.DATE));
//                                    }
//                                }
//                            }
//
//                            // is for today task
//
//
//                            //                 check for Date
//                            if (cursor.getInt(2) <= today.get(Calendar.DATE)) {
//                                // check for time
//                                if (today.get(Calendar.HOUR) > 4) {
//                                    Notify_Notification alert_notification = new Notify_Notification();
//                                    alert_notification.notify(context, "Today ", " Today Alert");
//
//
//                                    // new date transfer in Today Finish Task
//                                    Calendar calendar = Calendar.getInstance();
//                                    calendar.add(Calendar.DATE, 1);
//
//                                    setting_database.Update_The_Today_Task(1 + "", calendar.get(Calendar.DATE));
//                                }
//                            }
//                            //    check for Date
//                            if (cursor.getInt(3) <= today.get(Calendar.DATE)) {
//
//                                // check for time
//                                if (today.get(Calendar.HOUR_OF_DAY) > 4) {
//
//
//
//                                    // new date transfer in Due Finish Task
//                                    Calendar calendar = Calendar.getInstance();
//                                    calendar.add(Calendar.DATE, 1);
//
//                                    setting_database.Update_The_Due_Task(1 + "", calendar.get(Calendar.DATE));
//                                }
//                            }
                        } while (cursor.moveToNext());
                    }

                    Calendar calendar = Calendar.getInstance();
                    int date = calendar.get(Calendar.DATE);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    int current_Hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int current_Minute = calendar.get(Calendar.MINUTE);
                    Cursor cursor1 = main_dataBase.Get_All_Data_From_Date_Done_Time(date + "-" + month + "-" + year, current_Hour, 0);

                    if (cursor1 != null) {
                        cursor1.moveToFirst();

                        // local variable
                        int minute, hour, diff;
                        do {

                            // Grab The Hour and minute from Database
                            hour = cursor1.getInt(6);
                            if (!cursor1.getString(3).equalsIgnoreCase("")) {
                                String[] split = cursor1.getString(3).split(":");
                                minute = Integer.parseInt(split[1].substring(0, 1));
                            } else {
                                minute = 0;
                            }

                            // Check If is it in one hour
                            // Then Run The Time
                            if ((current_Hour == hour && current_Minute <= minute) ||
                                    (current_Hour + 1 == hour && current_Minute >= minute)) {
                                Log.i("Working", "cursor11 - " + cursor1.getString(1));
                                // find How much Time Difference
                                if (current_Hour == hour) diff = minute - current_Minute;
                                else {
                                    diff = (60 - (current_Minute - minute));
                                }
                                Schedule_Alarm(context, cursor1.getString(0), diff);
                            }

                        } while (cursor1.moveToNext());
                    }


                }
            } else {
                    Notify_Notification notify_notification = new Notify_Notification();
                    notify_notification.notify(context, "Alert", "Alert");
            }
        }
    }
    public void Schedule_Alarm(Context context , String ID ,int diff) {
        Log.i("Working"," ID -"+ ID + " diff - "+ diff);
        Intent toastIntent= new Intent(context,ToastBroadcastReceiver.class);
        toastIntent.putExtra("send_The_Id",ID);
        PendingIntent toastAlarmIntent = PendingIntent.getBroadcast(context, 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager backupAlarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        backupAlarmMgr.set(AlarmManager.RTC_WAKEUP,diff*60*1000,toastAlarmIntent);// 60*100 = 1 minute
    }
}
