package to_do_list.com.sudoajay.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//            Intent pushIntent = new Intent(context, BackgroundService.class);
//           Toast.makeText(context,"Boot completed and service started",Toast.LENGTH_SHORT).show();
//            context.startService(pushIntent);

            Intent toastIntent= new Intent(context,ToastBroadcastReceiver.class);
            PendingIntent toastAlarmIntent = PendingIntent.getBroadcast(context, 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            long startTime=System.currentTimeMillis(); //alarm starts immediately
            AlarmManager backupAlarmMgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime,AlarmManager.INTERVAL_HALF_HOUR,toastAlarmIntent); // alarm will repeat after every 15 minutes
        }
    }
}