package to_do_list.com.sudoajay.Background_Task;


import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkManger_Class_C extends Worker {


    public WorkManger_Class_C(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        return null;
    }
}
