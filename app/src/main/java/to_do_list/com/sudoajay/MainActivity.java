package to_do_list.com.sudoajay;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import to_do_list.com.sudoajay.DataBase.DataBase;
import to_do_list.com.sudoajay.Fragments.Main_Class_Fragement;

public class MainActivity extends AppCompatActivity {
    // global variable
    private DataBase dataBase;
    private Main_Class_Fragement main_class_fragement;
    private ArrayList<String> task_Name;
    private ArrayList<Boolean> check_Box_Array;

    private Fragment fragment;
    private BottomNavigationView bottom_Navigation_View;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference here
        Reference();

        task_Name.add("fill");
        task_Name.add("fillit");
        task_Name.add("fillit");

        check_Box_Array.add(false);
        check_Box_Array.add(false);
        check_Box_Array.add(false);

        // bottom navigation setup

        bottom_Navigation_View.setSelectedItemId(R.id.today_Tab);
        fragment = main_class_fragement.createInstance(this,"Today");
        Replace_Fragments();

        bottom_Navigation_View.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.overdue_Tab:
                        fragment = main_class_fragement.createInstance(MainActivity.this,"Overdue");
                        break;

                    case R.id.today_Tab:
                        fragment = main_class_fragement.createInstance(MainActivity.this,"Today");
                        break;
                    case R.id.overdo_Tab:
                        fragment =main_class_fragement.createInstance(MainActivity.this,"Overdo");
                        break;
                }
                Replace_Fragments();
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
        dataBase= new DataBase(this);
        bottom_Navigation_View = findViewById(R.id.bottom_Navigation_View);
        // array start
        task_Name = new ArrayList<>();
        check_Box_Array= new ArrayList<>();

        // create object of a class
        main_class_fragement = new Main_Class_Fragement();

    }
    // Replace Fragments
    public void Replace_Fragments(){

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_Layout, fragment);
            ft.commit();
        }
    }
}
