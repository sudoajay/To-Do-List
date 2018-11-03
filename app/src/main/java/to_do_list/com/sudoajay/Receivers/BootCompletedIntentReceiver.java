package to_do_list.com.sudoajay.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import to_do_list.com.sudoajay.Services.BackgroundService;


public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, BackgroundService.class);
            Toast.makeText(context,"Boot completed and service started",Toast.LENGTH_SHORT).show();
            context.startService(pushIntent);
        }
    }
}