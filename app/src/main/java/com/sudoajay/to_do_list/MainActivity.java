package com.sudoajay.to_do_list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.sudoajay.to_do_list.Background_Task.WorkManger_Class_A;
import com.sudoajay.to_do_list.Background_Task.WorkManger_Class_B;
import com.sudoajay.to_do_list.Background_Task.WorkManger_Class_C;
import com.sudoajay.to_do_list.Background_Task.WorkManger_Class_D;
import com.sudoajay.to_do_list.DataBase.Main_DataBase;
import com.sudoajay.to_do_list.DataBase.Setting_Database;
import com.sudoajay.to_do_list.Fragments.Main_Class_Fragement;


public class MainActivity extends AppCompatActivity {
    // global variable
    private  final ArrayList<String> places = new ArrayList<>(Arrays.asList("Overdue", "Today", "Overdo"));
    private Fragment fragment;
    private BottomNavigationView bottom_Navigation_View;
    private boolean doubleBackToExitPressedOnce;
    private Setting_Database setting_database;
    private Main_DataBase main_DataBase;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference here
        Reference();

        if(getIntent().getExtras() != null ) {
            Intent intent = getIntent();
            if(intent.hasExtra("Send_The_ID")) {
                int get_Id = intent.getIntExtra("Send_The_ID", 0);
                main_DataBase.Update_The_Table_For_Done(get_Id + "", 1);
            }
        }

        // Setting Database Fill
        // First time check
        Setting_Database_Install();


        // task A  == Morning 4 Am
         Type_A_Task();

        // Task B  == night 10 pm
        Type_B_Task();

        // Type C Alert Notification
        Type_C_Task();

            // bottom navigation setup
        bottom_Navigation_View.setSelectedItemId(R.id.today_Tab);
         Main_Class_Fragement main_class_fragement = new Main_Class_Fragement();
        fragment = main_class_fragement.createInstance(this,places.get(1));
        Replace_Fragments();

        bottom_Navigation_View.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.overdue_Tab:
                        Main_Class_Fragement main_class_fragement1 = new Main_Class_Fragement();
                        fragment = main_class_fragement1.createInstance(MainActivity.this,places.get(0));
                        Replace_Fragments();
                        return true;

                    case R.id.today_Tab:
                        Main_Class_Fragement main_class_fragement2 = new Main_Class_Fragement();
                        fragment = main_class_fragement2.createInstance(MainActivity.this,places.get(1));
                        Replace_Fragments();
                        return true;
                    case R.id.overdo_Tab:
                        Main_Class_Fragement main_class_fragement3 = new Main_Class_Fragement();
                        fragment =main_class_fragement3.createInstance(MainActivity.this,places.get(2));
                        Replace_Fragments();
                        return true;
                }


                return false;
            }
        });

    }


    public void On_Click_Process(View view){
        switch (view.getId()){
            case R.id.floatingActionButton:
                Intent intent = new Intent(getApplicationContext(),Create_New_To_Do_List.class);
                startActivity(intent);
                break;

        }

    }
    private void Reference(){
        main_DataBase = new Main_DataBase(this);
        bottom_Navigation_View = findViewById(R.id.bottom_Navigation_View);
        setting_database = new Setting_Database(this);
    }
    // Replace Fragments
    public void Replace_Fragments(){

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

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void Finish() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }

    private void Setting_Database_Install(){
        // setting database fill
        if(setting_database.check_For_Empty()){

            int tomorrow_Task,due_Task;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,1);
            tomorrow_Task = calendar.get(Calendar.DATE);
            if(calendar.get(Calendar.HOUR) < 22 )
            {
                calendar.add(Calendar.DATE,-1);
                due_Task=calendar.get(Calendar.DATE);
            }else{
                due_Task = tomorrow_Task;
            }

            setting_database.Fill_It(tomorrow_Task,tomorrow_Task,due_Task);
        }
    }

    private void Type_A_Task(){

        // this task for cleaning and show today task

        int diff ,hour;
        Calendar calendar = Calendar.getInstance();
         hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(hour > 4)
                diff = (24-(hour-4));
            else {
                diff = 4-hour;
            }
        OneTimeWorkRequest morning_Work =
                new OneTimeWorkRequest.Builder(WorkManger_Class_A.class).addTag("A").setInitialDelay( diff,TimeUnit.HOURS)
                        .build();
        WorkManager.getInstance().enqueueUniqueWork("B", ExistingWorkPolicy.KEEP, morning_Work);


        WorkManager.getInstance().getWorkInfoByIdLiveData(morning_Work.getId())
                .observe(this, workInfo -> {
                    // Do something with the status
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        // ...
                        PeriodicWorkRequest.Builder morning_Work_builder =
                                new PeriodicWorkRequest.Builder(WorkManger_Class_A.class, 1,
                                        TimeUnit.DAYS).addTag("Morning Work");

                        // Create the actual work object:
                        PeriodicWorkRequest morning_Worker = morning_Work_builder.build();

                        // Then enqueue the recurring task:
                        WorkManager.getInstance().enqueueUniquePeriodicWork("Morning Work", ExistingPeriodicWorkPolicy.KEEP
                                ,morning_Worker);
                    }
                });
    }
    private void Type_B_Task(){

        // this task for Showing Due Task

        int diff ,hour;
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour > 22)
            diff = (24-(hour-22));
        else {
            diff = 22-hour;
        }
        OneTimeWorkRequest night_Work =
                new OneTimeWorkRequest.Builder(WorkManger_Class_B.class).addTag("B").setInitialDelay(diff ,TimeUnit.HOURS )
                        .build();
        WorkManager.getInstance().enqueueUniqueWork("B", ExistingWorkPolicy.KEEP, night_Work);


        WorkManager.getInstance().getWorkInfoByIdLiveData(night_Work.getId())
                .observe(this, workInfo -> {
                    // Do something with the status
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        // ...
                        PeriodicWorkRequest.Builder morning_Work_builder =
                                new PeriodicWorkRequest.Builder(WorkManger_Class_B.class, 1,
                                        TimeUnit.DAYS).addTag("Night Work");
                        // Create the actual work object:
                        PeriodicWorkRequest morning_Worker = morning_Work_builder.build();
                        // Then enqueue the recurring task:
                        WorkManager.getInstance().enqueueUniquePeriodicWork("Night Work", ExistingPeriodicWorkPolicy.KEEP
                                ,morning_Worker);
                    }
                });
    }
    private void Type_C_Task() {

        if (!main_DataBase.check_For_Empty()) {

            Calendar calendar = Calendar.getInstance();
            int current_Year = calendar.get(Calendar.YEAR);
            int current_Month = calendar.get(Calendar.MONTH);
            int current_Day = calendar.get(Calendar.DAY_OF_MONTH);
            int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
            int current_minute = calendar.get(Calendar.MINUTE), hour, minute, total_Minute = 0;
            // for today date
            String today_Date = current_Day + "-" + current_Month + "-" + current_Year;

            Cursor cursor = main_DataBase.Get_The_Id_Name_Original_Time_From_Date_Done_OriginalTime(today_Date, current_hour, 0);
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                do {
                    if (!cursor.getString(2).isEmpty() && cursor.getInt(3) != 24) {
                        String[] split = cursor.getString(2).split(":");
                        hour = cursor.getInt(3);
                        minute = Integer.parseInt(split[1].substring(0, 2));
                    } else {
                        hour = 16;
                        minute = 0;
                    }

                    total_Minute = ((hour - current_hour) * 60) + (minute - current_minute);
                    Log.d("Checking" , total_Minute+"" );
                    // if is it in minus
                } while (cursor.moveToNext() && total_Minute < 0);
            }

            OneTimeWorkRequest alert_Work;
            if (total_Minute < 0) {
                 alert_Work =
                        new OneTimeWorkRequest.Builder(WorkManger_Class_D.class)
                                .addTag("Alert Task").setInitialDelay(60, TimeUnit.MINUTES)
                                .build();

            } else {
                 alert_Work =
                        new OneTimeWorkRequest.Builder(WorkManger_Class_C.class)
                                .addTag("Alert Task").setInitialDelay(total_Minute, TimeUnit.MINUTES)
                                .build();
            }
            WorkManager.getInstance().enqueueUniqueWork("Alert Task", ExistingWorkPolicy.REPLACE, alert_Work);

            WorkManager.getInstance().getWorkInfoByIdLiveData(alert_Work.getId())
                    .observe(this, workInfo -> {
                        // Do something with the status
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            Type_C_Task();

                        }
                    });
        }
    }
}
