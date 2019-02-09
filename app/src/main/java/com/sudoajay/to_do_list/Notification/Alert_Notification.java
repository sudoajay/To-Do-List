package com.sudoajay.to_do_list.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.sudoajay.to_do_list.Create_New_To_Do_List;
import com.sudoajay.to_do_list.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import com.sudoajay.to_do_list.DataBase.Main_DataBase;
import com.sudoajay.to_do_list.R;

/**
 * Helper class for showing and canceling new message
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class Alert_Notification {
    /**
     * The unique identifier for this type of notification.
     */

    // globally variable
    private static final String NOTIFICATION_TAG = "NewMessage";
    private Context context;
    private NotificationManager notificationManager;
    private ArrayList<String> save_All_Date,task_Name;
    private ArrayList<Integer> array_Id;
    private Intent update_Intent ,edit_Intent;
    private Main_DataBase main_dataBase;
    private int total_No=0;
    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of new message notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */


    // it is only for alert notification
    // Alert
    public  void notify(final Context context,
                              final String exampleString, final int number) {
        String text="",channel_id;
        final Resources res = context.getResources();
        this.context = context;
        final String title = exampleString+res.getString(
                R.string.notification_title_name);

        main_dataBase = new Main_DataBase(context);
        // Grab the data From Database
        // Store Into Array for Execution
        Grab_The_Data_From_Database();

        // setup according Which Type
        // if There is no data match with query
            channel_id = context.getString(R.string.Channel_Id_Alert); // channel_id
        if(array_Id.size() == 1)
                text = res.getString(R.string.alert_notification_para, task_Name.get(0));
            else {
                text =res.getString(R.string.alert_notification_lines,total_No+"") ;
        }

        // if no data Grab From Database
        //  first box this for Alert Type
        // second box this is not for alert Type

        // now check for null notification manger
        if (notificationManager == null) {
            notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        // this check for android Oero In which Channel Id Come as New Feature
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            assert notificationManager != null;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(channel_id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channel_id, title, importance);
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        // complete button on action
        Update_The_Done();

        // snooze button on action
        Edit_Page();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channel_id)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(Notification.DEFAULT_ALL)
                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.today_icon)
                .setContentTitle(title)
                .setContentText(text)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Set ticker text (preview) information for this notification.
                .setTicker(exampleString)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(number)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                //.setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context, MainActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                // Show an expanded list of items on devices running Android 4.1
                // or later.


                // Example additional actions for this notification. These will
                // only show on devices running Android 4.1 or later, so you
                // should ensure that the activity in this notification's
                // content intent provides access to the same actions in
                // another way.


                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);
        // if index one is empty then this action don't come

            builder.addAction(
                    R.drawable.check_icon,
                    res.getString(R.string.action_Completed),
                    PendingIntent.getActivity(
                            context,
                            0,
                            update_Intent,
                            PendingIntent.FLAG_UPDATE_CURRENT)
            );

            // this is not visible more then one element in an array
        if(task_Name.size() ==  1) {
            builder.addAction(
                    R.drawable.snooze_icon,
                    res.getString(R.string.action_Snooze),
                    PendingIntent.getActivity(
                            context,
                            0,
                            edit_Intent,
                            PendingIntent.FLAG_UPDATE_CURRENT)
            );
        }
            // if the date more than 1
            if(task_Name.size()>1){
                builder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(task_Name.get(0) +" - "+ save_All_Date.get(0))
                        .addLine(task_Name.get(1) +" - "+ save_All_Date.get(1))
                        .addLine(task_Name.get(2) +" - "+ save_All_Date.get(2))
                        .addLine(task_Name.get(3) +" - "+ save_All_Date.get(3))
                        .addLine(task_Name.get(4) +" - "+ save_All_Date.get(4))
                        .setBigContentTitle(title)
                        .setSummaryText(""));
            }



        // if the data By mistake empty or deleted
        if(!task_Name.isEmpty() || task_Name.size() > 0) {
            notify(context, builder.build());
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private  void notify(final Context context, final Notification notification) {
        // this help to set the default sound ringtone
        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ringtone);
        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
        notificationManager.notify(NOTIFICATION_TAG, 0, notification);

    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, int)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, 0);
    }

    private void Grab_The_Data_From_Database(){
        Initialization();

        if(!main_dataBase.check_For_Empty()){
            Calendar calendar = Calendar.getInstance();
            int current_Year= calendar.get(Calendar.YEAR);
            int current_Month= calendar.get(Calendar.MONTH);
            int current_Day= calendar.get(Calendar.DAY_OF_MONTH);
            int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
            int current_Minute =calendar.get(Calendar.MINUTE);
            int hour,minute;

            // for today date
            String today_Date=current_Day + "-" + current_Month + "-" +current_Year;

            Cursor cursor = main_dataBase.Get_The_Id_Name_Original_Time_From_Date_Done_OriginalTime(today_Date,current_hour,0);
            if(cursor !=null && cursor.moveToFirst()){
                cursor.moveToFirst();
                do {
                    if (!cursor.getString(2).isEmpty() && cursor.getInt(3) != 24) {
                        String[] split = cursor.getString(2).split(":");
                        hour = cursor.getInt(3);
                        minute = Integer.parseInt(split[1].substring(0, 2));
                    } else {
                        hour = 12;
                        minute = 0;
                    }
                    if(hour == current_hour && current_Minute ==minute){
                        array_Id.add(cursor.getInt(0));
                        task_Name.add(cursor.getString(1));
                        save_All_Date.add(cursor.getString(2));
                    }

                }while ((cursor.moveToNext()));
                }
        }
        if(task_Name.size()>1) {

//            // Remove last Array element
//            array_Id.remove(array_Id.size() - 1);
//            task_Name.remove(task_Name.size() - 1);
//            save_All_Date.remove(save_All_Date.size() - 1);

            // set the array size
            total_No = array_Id.size();
            // add automatic array
            for (int i = task_Name.size(); i < 5; i++) {
                array_Id.add(0);
                task_Name.add("");
                save_All_Date.add("");
            }
        }


    }

    // initialization arrays
    private void Initialization(){
        save_All_Date = new ArrayList<>();
        task_Name = new ArrayList<>();
        array_Id = new ArrayList<>();
    }

    // complete action method
    private void Update_The_Done(){
        update_Intent = new Intent(context,MainActivity.class);
        update_Intent.putExtra("Send_The_ID_Array" ,array_Id);
    }

    // Snooze action method
    private void Edit_Page(){
        if(array_Id.size() >1 ) {
            edit_Intent = new Intent(context, Create_New_To_Do_List.class);
            edit_Intent.putExtra("to_do_list.com.sudoajay.Adapter_Id", array_Id.get(0));
        } else{
            edit_Intent = new Intent(context, MainActivity.class);

        }
    }
}
