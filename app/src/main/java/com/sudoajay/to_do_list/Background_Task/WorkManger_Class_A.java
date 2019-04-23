package com.sudoajay.to_do_list.Background_Task;


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.sudoajay.to_do_list.AlarmManger.CallAlarmManger;
import com.sudoajay.to_do_list.DataBase.Main_DataBase;
import com.sudoajay.to_do_list.ForegroundService.TraceBackgroundService;
import com.sudoajay.to_do_list.Notification.Notify_Notification;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkManger_Class_A extends Worker {

    private final Context context;

    public WorkManger_Class_A(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

       RunThis(getApplicationContext());

        return Result.success();
    }

    public static void RunThis(final Context context){
        Main_DataBase main_dataBase = new Main_DataBase(context);


        // first task - clean the database for user . Delete all completed task from Database

        if (!main_dataBase.check_For_Empty()) {
            Cursor cursor = main_dataBase.Get_The_Id_From_Done(1);
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                do {
                    main_dataBase.deleteRow(cursor.getInt(0) + "");
                    new CallAlarmManger(context);
                } while (cursor.moveToNext());
            }
        }


        // second task - Show All the today task
        Notify_Notification notify_notification = new Notify_Notification();
        notify_notification.notify(context, "Today ", "Today List Alert");

        // set next date
        TraceBackgroundService traceBackgroundService = new TraceBackgroundService(context);
        traceBackgroundService.setTodayTask();
    }
}

