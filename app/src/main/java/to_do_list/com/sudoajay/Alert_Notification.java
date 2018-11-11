package to_do_list.com.sudoajay;

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

import java.util.ArrayList;
import java.util.Calendar;

import to_do_list.com.sudoajay.DataBase.Main_DataBase;

/**
 * Helper class for showing and canceling alert
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class Alert_Notification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "Alert_";
    private Context context;
    private NotificationManager notificationManager;
    private Main_DataBase mainDataBase;
    private ArrayList<String> save_All_Date,task_Name;
    private ArrayList<Integer> array_Id;
    private Intent update_Intent , edit_Intent;


    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of alert  notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public void notify(final Context context,
                              final String notification_Hint) {


        String channel_id = context.getString(R.string.Channel_Id); // default_channel_id
        final Resources res = context.getResources();
        this.context = context;
        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        Grab_The_Data_From_Database();

        final String title = res.getString(
                R.string.alert__notification_title_template);
        final String text = res.getString(
                R.string.alert__notification_placeholder_text_template,task_Name.get(0));
        if (notificationManager == null) {
            notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
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
                .setSmallIcon(R.drawable.ic_stat_alert_)
                .setContentTitle(title)
                .setContentText(text)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
           //     .setSound(uri)
                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
              //  .setLargeIcon(picture)

                // Set ticker text (preview) information for this notification.
                .setTicker(notification_Hint)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                .setNumber(5)

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
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(task_Name.get(1)  + save_All_Date.get(1))
                        .addLine(task_Name.get(2) + save_All_Date.get(2))
                        .setBigContentTitle(title)
                        .setSummaryText(""))

                // Example additional actions for this notification. These will
                // only show on devices running Android 4.1 or later, so you
                // should ensure that the activity in this notification's
                // content intent provides access to the same actions in
                // another way.
                .addAction(
                        R.drawable.done_icon,
                        res.getString(R.string.action_Completed),
                        PendingIntent.getActivity(
                                context,
                                0,
                                update_Intent,
                                PendingIntent.FLAG_UPDATE_CURRENT)
                        )
                .addAction(
                        R.drawable.snooze_icon,
                        res.getString(R.string.action_Snooze),
                        PendingIntent.getActivity(
                                context,
                                0,
                                edit_Intent,
                                PendingIntent.FLAG_UPDATE_CURRENT)
                )
                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);
        // if index one is empty then this action don't come

        if(task_Name.size() > 3) {
            builder.addAction(
                    R.drawable.more_white_icon,
                    res.getString(R.string.action_More),
                    PendingIntent.getActivity(
                            context,
                            0,
                            new Intent(context, MainActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT)
            );
        }

        notify(context, builder.build());

    }
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void notify(final Context context, final Notification notification) {

        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ringtone);
        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
        notificationManager.notify(NOTIFICATION_TAG, 0, notification);
    }


    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, 0);
    }

    private void Grab_The_Data_From_Database() {
        mainDataBase = new Main_DataBase(context);

        Insaliziation();
        Grab_The_Data_From_DB();

    }
        private void Grab_The_Data_From_DB(){
            // local variable
            String temp;

            Calendar calendar = Calendar.getInstance();
            if(!mainDataBase.check_For_Empty()){
                Cursor cursor = mainDataBase.Get_The_Data_From_Date_Orignal_Time(0,calendar.get(Calendar.DATE)+"-"+
                                                calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR));
                // calendar.get(Calendar.DATE)+"-"+
                //                        calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR)
                if( cursor != null && cursor.moveToFirst() ){
                    cursor.moveToFirst();
                    do
                    {
                        array_Id.add(cursor.getInt(0));
                        if(cursor.getString(1).length() < 20) {
                            temp = cursor.getString(1);
                            for(int i = temp.length()+1 ; i <= 20;i++){

                                temp+=" ";
                            }
                            task_Name.add(temp);
                        }else{
                            task_Name.add(cursor.getString(1).substring(0,20));
                        }
                        if(!cursor.getString(3).isEmpty()) {
                            save_All_Date.add(cursor.getString(3));
                        }else{
                            save_All_Date.add("No Time Set");
                        }


                    }while (cursor.moveToNext());
                cursor.close();
                }

                    // if not empty check
                if(task_Name.size() >= 2) {
                    for (int i = 1; i < 3; i++) {
                        if (i >= task_Name.size()) {
                            task_Name.add("");
                            save_All_Date.add("");
                        }else{
                            task_Name.set(i,task_Name.get(i)+"  -  ");
                        }
                    }
                }else{
                    task_Name.add("No More Task Left For Today");
                    save_All_Date.add("");
                    task_Name.add("");
                    save_All_Date.add("");
                }

            }
            // check for error handling
            if(task_Name.size() < 3){
                task_Name.add("No More Task Left For Today ");
                save_All_Date.add("");
            }

        }
        private void Insaliziation(){
        save_All_Date = new ArrayList<>();
        task_Name = new ArrayList<>();
        array_Id = new ArrayList<>();

    }
    // complete action method
    private void Update_The_Done(){
       if(array_Id.isEmpty()) array_Id.add(0);

        update_Intent = new Intent(context,MainActivity.class);
        update_Intent.putExtra("Send_The_ID" ,array_Id.get(0));
    }

    // Snooze action method
    private void Edit_Page(){
        if(array_Id.isEmpty()) array_Id.add(0);
        edit_Intent = new Intent(context,Create_New_To_Do_List.class);
        edit_Intent.putExtra("to_do_list.com.sudoajay.Adapter_Id" ,array_Id.get(0) );
    }
}
