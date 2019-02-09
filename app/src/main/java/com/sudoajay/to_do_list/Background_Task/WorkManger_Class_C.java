package com.sudoajay.to_do_list.Background_Task;


import android.content.Context;
import android.support.annotation.NonNull;

import com.sudoajay.to_do_list.Notification.Alert_Notification;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkManger_Class_C extends Worker {
    private final Context context;

    public WorkManger_Class_C(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context =context;
    }

    @NonNull
    @Override
    public Result doWork() {

        // alert Notification
        Alert_Notification alert_notification = new Alert_Notification();
        alert_notification.notify(context,"Alert ",0);

        return Result.success();
    }
}
