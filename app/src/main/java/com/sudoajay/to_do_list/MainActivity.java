package com.sudoajay.to_do_list;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.sudoajay.to_do_list.AlarmManger.CallAlarmManger;
import com.sudoajay.to_do_list.Background_Task.WorkManger_Class_A;
import com.sudoajay.to_do_list.Background_Task.WorkManger_Class_B;
import com.sudoajay.to_do_list.DataBase.Main_DataBase;
import com.sudoajay.to_do_list.ForegroundService.Foreground;
import com.sudoajay.to_do_list.ForegroundService.ForegroundDialog;
import com.sudoajay.to_do_list.ForegroundService.TraceBackgroundService;
import com.sudoajay.to_do_list.Fragments.Main_Class_Fragement;
import com.sudoajay.to_do_list.WelcomeScreen.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    // global variable
    private final ArrayList<String> places = new ArrayList<>(Arrays.asList("Overdue", "Today", "Overdo"));
    private Fragment fragment;
    private BottomNavigationView bottom_Navigation_View;
    private boolean doubleBackToExitPressedOnce;
    private Main_DataBase main_DataBase;
    private String value = "";
    private PrefManager prefManager;
    private TraceBackgroundService traceBackgroundService;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference here
        Reference();
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra("Send_The_ID_Array")) {
                ArrayList<Integer> get_Id = intent.getIntegerArrayListExtra("Send_The_ID_Array");
                for (Integer fill : get_Id) {
                    Toast.makeText(getApplicationContext(), fill + "  ", Toast.LENGTH_SHORT).show();
                    main_DataBase.Update_The_Table_For_Done(fill + "", 1);
                }
            }
            if (intent.hasExtra("Passing")) {
                value = intent.getStringExtra("Passing");
            }
            if (intent.getAction() != null && intent.getAction().equals("Stop_Foreground(Setting)")) {
                Intent startIntent = new Intent(getApplicationContext(), Foreground.class);
                startIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundDialog"
                        , "Stop_Foreground");
                startService(startIntent);
            }
        }

        if (prefManager.isFirstTimeLaunch()) {

            // Task A  == Morning 4 Am
            TypeATask();

            // Task B  == night 8 pm
            TypeBTask();

            // Type C Alert Notification
            new CallAlarmManger(getApplicationContext());

        } else {
            if (traceBackgroundService.isBackgroundServiceWorking()) {
                traceBackgroundService.isBackgroundWorking();
            }

            if (!traceBackgroundService.isBackgroundServiceWorking()) {
                if (!isServiceRunningInForeground(getApplicationContext(), Foreground.class)) {

                    ForegroundDialog foregroundService = new ForegroundDialog(MainActivity.this, MainActivity.this);
                    foregroundService.call_Thread();

                }
            }
        }
        if (value.equalsIgnoreCase("DueList")) {
            // bottom navigation setup Due tab
            bottom_Navigation_View.setSelectedItemId(R.id.overdue_Tab);
            Main_Class_Fragement main_class_fragement = new Main_Class_Fragement();
            fragment = main_class_fragement.createInstance(this, places.get(0));
            Replace_Fragments();
        } else {
            // bottom navigation setup Today tab
            // by default
            bottom_Navigation_View.setSelectedItemId(R.id.today_Tab);
            Main_Class_Fragement main_class_fragement = new Main_Class_Fragement();
            fragment = main_class_fragement.createInstance(this, places.get(1));
            Replace_Fragments();
        }
        bottom_Navigation_View.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.overdue_Tab:
                    Main_Class_Fragement main_class_fragement1 = new Main_Class_Fragement();
                    fragment = main_class_fragement1.createInstance(MainActivity.this, places.get(0));
                    Replace_Fragments();
                    return true;

                case R.id.today_Tab:
                    Main_Class_Fragement main_class_fragement2 = new Main_Class_Fragement();
                    fragment = main_class_fragement2.createInstance(MainActivity.this, places.get(1));
                    Replace_Fragments();
                    return true;
                case R.id.overdo_Tab:
                    Main_Class_Fragement main_class_fragement3 = new Main_Class_Fragement();
                    fragment = main_class_fragement3.createInstance(MainActivity.this, places.get(2));
                    Replace_Fragments();
                    return true;
            }


            return false;
        });

    }


    public void On_Click_Process(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                Intent intent = new Intent(getApplicationContext(), Create_New_To_Do_List.class);
                startActivity(intent);
                break;

        }

    }

    private void Reference() {
        main_DataBase = new Main_DataBase(this);
        bottom_Navigation_View = findViewById(R.id.bottom_Navigation_View);
        prefManager = new PrefManager(getApplicationContext());
        traceBackgroundService = new TraceBackgroundService(getApplicationContext());
    }

    // Replace Fragments
    public void Replace_Fragments() {

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_Layout, fragment);
            ft.commit();
        }
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, " Click Back Again To Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    public void Finish() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }


    private void TypeATask() {

        int fixedHour = 4, diffHour;
        // this task for cleaning and show today task 4 clock
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);


        if (currentHour >= fixedHour) {
            diffHour = (24 - currentHour) + fixedHour;
        } else {
            diffHour = fixedHour - currentHour;
        }

        OneTimeWorkRequest morning_Work =
                new OneTimeWorkRequest.Builder(WorkManger_Class_A.class).addTag("Show Today Task").setInitialDelay(15
                        , TimeUnit.MINUTES).build();
        WorkManager.getInstance().enqueueUniqueWork("Show Today Task", ExistingWorkPolicy.REPLACE, morning_Work);

        WorkManager.getInstance().getWorkInfoByIdLiveData(morning_Work.getId())
                .observe(this, workInfo -> {
                    // Do something with the status
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        // ...
                        TypeATask();
                    }
                });


    }

    private void TypeBTask() {

        // this task for Showing Due Task

        int fixedHour = 20, diffHour;
        // this task for cleaning and show today task 8 clock pm
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);


        if (currentHour >= fixedHour) {
            diffHour = (24 - currentHour) + fixedHour;
        } else {
            diffHour = fixedHour - currentHour;
        }

        OneTimeWorkRequest morning_Work =
                new OneTimeWorkRequest.Builder(WorkManger_Class_B.class).addTag("Showing Due Task").setInitialDelay(2
                        , TimeUnit.DAYS).build();
        WorkManager.getInstance().enqueueUniqueWork("Showing Due Task", ExistingWorkPolicy.KEEP, morning_Work);

        WorkManager.getInstance().getWorkInfoByIdLiveData(morning_Work.getId())
                .observe(this, workInfo -> {
                    // Do something with the status
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        // ...
                        TypeBTask();
                    }
                });
    }

    public boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            if (!ServicesWorking()) return true;
            return false;
        }
    }

    public boolean ServicesWorking() {
        traceBackgroundService.isBackgroundWorking();
        return !traceBackgroundService.isBackgroundServiceWorking();
    }
}
