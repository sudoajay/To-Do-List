package com.sudoajay.to_do_list.ForegroundService;

import android.app.IntentService;
import android.content.Intent;


public class ForegroundServiceBoot extends IntentService {

    public ForegroundServiceBoot() {
        super(null);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("RebootReceiver")) {
            Intent startIntent = new Intent(getApplicationContext(), Foreground.class);
            startIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundDialog"
                    , "Start_Foreground");
            startService(startIntent);
        }
        //Do reboot stuff
        //handle other types of callers, like a notification.
    }
}

