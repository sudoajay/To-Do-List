package to_do_list.com.sudoajay;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import to_do_list.com.sudoajay.DataBase.Main_DataBase;
import to_do_list.com.sudoajay.Fragments.Main_Class_Fragement;
import to_do_list.com.sudoajay.Receivers.ResponseBroadcastReceiver;
import to_do_list.com.sudoajay.Receivers.ToastBroadcastReceiver;


public class MainActivity extends AppCompatActivity {
    // global variable
    private Main_DataBase mainDataBase;
    private  final ArrayList<String> places = new ArrayList<>(Arrays.asList("Overdue", "Today", "Overdo"));
    private Fragment fragment;
    private BottomNavigationView bottom_Navigation_View;
    private boolean doubleBackToExitPressedOnce;
    private ResponseBroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference here
        Reference();

        if(getIntent().getExtras() != null ) {
            Intent intent = getIntent();
             int get_Id = intent.getIntExtra("Send_The_ID", 0);
                mainDataBase.Update_The_Table_For_Done(get_Id+"" ,1);
        }


        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(to_do_list.com.sudoajay.IntentServices.BackgroundService.ACTION);
        registerReceiver(broadcastReceiver,intentFilter);

        // schedule Background Task
        Schedule_Alarm();


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

    public void Schedule_Alarm() {
        Intent toastIntent= new Intent(getApplicationContext(),ToastBroadcastReceiver.class);
        PendingIntent toastAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        long startTime=System.currentTimeMillis(); //alarm starts immediately
        AlarmManager backupAlarmMgr=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime,2*60*1000,toastAlarmIntent); // alarm will repeat after every 15 minutes
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
        mainDataBase = new Main_DataBase(this);
        bottom_Navigation_View = findViewById(R.id.bottom_Navigation_View);
        broadcastReceiver = new ResponseBroadcastReceiver();

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
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(to_do_list.com.sudoajay.IntentServices.BackgroundService.ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


}
