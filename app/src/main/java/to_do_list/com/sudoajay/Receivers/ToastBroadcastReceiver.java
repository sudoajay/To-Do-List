package to_do_list.com.sudoajay.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import to_do_list.com.sudoajay.Services.BackgroundService;


/**
 * Created by harun on 1/10/18.
 */

public class ToastBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent= new Intent(context,BackgroundService.class);
        context.startService(serviceIntent);

    }
}
