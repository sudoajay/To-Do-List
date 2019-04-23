package com.sudoajay.to_do_list.AlarmManger;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class MyService extends IntentService {


    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        new CallAlarmManger(getApplicationContext());
    }
}
