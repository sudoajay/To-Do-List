package to_do_list.com.sudoajay.Background_Task;


import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import to_do_list.com.sudoajay.Notification.Notify_Notification;

public class WorkManger_Class_B extends Worker {
    private Context context;


    public WorkManger_Class_B(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context =context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Notify_Notification notify_notification = new Notify_Notification();
        notify_notification.notify(context, "Due ", "Due List Alert");

        return Result.SUCCESS;
    }
}
