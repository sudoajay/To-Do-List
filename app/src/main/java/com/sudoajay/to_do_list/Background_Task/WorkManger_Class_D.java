package com.sudoajay.to_do_list.Background_Task;


import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkManger_Class_D extends Worker {

    public WorkManger_Class_D(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Context context1 = context;
    }

    @NonNull
    @Override
    public Result doWork() {


        return Result.success();
    }
}
