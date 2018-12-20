package com.sudoajay.to_do_list.Background_Task;


import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkManger_Class_D extends Worker {
    private Context context;

    public WorkManger_Class_D(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context =context;
    }

    @NonNull
    @Override
    public Result doWork() {


        return Result.success();
    }
}
