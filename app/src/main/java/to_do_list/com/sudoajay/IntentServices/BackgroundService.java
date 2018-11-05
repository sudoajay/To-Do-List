package to_do_list.com.sudoajay.Services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import to_do_list.com.sudoajay.MainActivity;

/**
 * Created by harun on 1/10/18.
 */

public class BackgroundService extends IntentService {
    public static final String ACTION="to_do_list.com.sudoajay.Receivers.ResponseBroadcastReceiver";

    // Must create a default constructor
    public BackgroundService() {
        // Used to name the worker thread, important only for debugging.
        super("backgroundService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // This describes what will happen when service is triggered
        Log.i("backgroundService","Service running");

        //create a broadcast to send the toast message
        Intent toastIntent= new Intent(ACTION);
        toastIntent.putExtra("resultCode", Activity.RESULT_OK);
        toastIntent.putExtra("toastMessage","I'M running after ever 15 minutes");
        sendBroadcast(toastIntent);

        MainActivity mainActivity =new MainActivity();
    }
    public static String getACTION() {
        return ACTION;
    }
}
