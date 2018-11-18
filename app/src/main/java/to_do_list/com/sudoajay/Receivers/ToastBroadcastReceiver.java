package to_do_list.com.sudoajay.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import to_do_list.com.sudoajay.DataBase.Main_DataBase;
import to_do_list.com.sudoajay.DataBase.Setting_Database;
import to_do_list.com.sudoajay.Notification.Alert_Notification;


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

        setting_database = new Setting_Database(context);
        main_dataBase = new Main_DataBase(context);

        if(!setting_database.check_For_Empty()){
            Cursor cursor =setting_database.Get_All_Date();
            Calendar today = Calendar.getInstance();
            if (cursor != null) {
                cursor.moveToFirst();

                do{

                    // is for old Finish Task

                    // date check
                    if(cursor.getInt(1) <= today.get(Calendar.DATE) ){
                        // check for Time
                        if(today.get(Calendar.HOUR) > 4) {
                            Cursor cursor1 = main_dataBase.Get_The_Id_From_Done(1);
                            if (cursor1 != null) {
                                cursor1.moveToFirst();
                                do {
                                    main_dataBase.Delete_Row(cursor.getInt(0)+"");

                                } while (cursor1.moveToNext());

                                Toast.makeText(context,"The Data is Refresh",Toast.LENGTH_SHORT).show();

                                // new date transfer in old Finish Task
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.DATE, 1);

                                setting_database.Update_The_Old_Fin_Task(1 + "", calendar.get(Calendar.DATE));
                            }
                        }
                    }

                    // is for today task

                    
    //                 check for Date
                        if(cursor.getInt(2) <= today.get(Calendar.DATE)) {
                            // check for time
                            if (today.get(Calendar.HOUR) > 4) {
                                Alert_Notification alert_notification = new Alert_Notification();
                                alert_notification.notify(context,"Today "," Today Alert");


                                // new date transfer in Today Finish Task
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.DATE, 1);

                                setting_database.Update_The_Today_Task(1 + "", calendar.get(Calendar.DATE));
                            }
                        }
                //    check for Date
                    if(cursor.getInt(3) <= today.get(Calendar.DATE)) {

                        // check for time
                        if (today.get(Calendar.HOUR_OF_DAY) > 4) {
                            Alert_Notification alert_notification = new Alert_Notification();
                            alert_notification.notify(context,"Due ","Due Alert");


//                            // new date transfer in Due Finish Task
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.add(Calendar.DATE, 1);
//
//                            setting_database.Update_The_Due_Task(1 + "", calendar.get(Calendar.DATE));
                        }
                    }
                }while (cursor.moveToNext());
            }
        }


        Calendar calendar = Calendar.getInstance();



    }
}
