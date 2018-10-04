package to_do_list.com.sudoajay;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import to_do_list.com.sudoajay.DataBase.DataBase;

public class MainActivity extends AppCompatActivity {
    // global variable
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference here
        Reference();
    }

    public void On_Click_Process(View view){
        switch (view.getId()){
            case R.id.nothing_Text:
            case R.id.nothing_Image_View:
            case R.id.floatingActionButton:
                Intent intent = new Intent(getApplicationContext(),Create_New_To_Do_List.class);
                startActivity(intent);
                break;
        }

    }
    private void Reference(){
        dataBase= new DataBase(this);

        // Grab & Setup
        Grab_And_Setup_Database();
    }
    private void Grab_And_Setup_Database(){
//        if (tick_on_button_dataBase.check_For_Empty()) {
//
//        } else {
//            Cursor cursor = tick_on_button_dataBase.Get_All_Data();
//            int get, no = 1;
//            if (cursor.moveToFirst()) {
//                while (no < 10) {
//                    get = cursor.getInt(no);
//                    if (get == 0) {
//                        return_Id(no).setVisibility(View.GONE);
//                    }
//                    no++;
//                }
//            }
    }
}
