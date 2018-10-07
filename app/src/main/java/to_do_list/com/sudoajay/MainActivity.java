package to_do_list.com.sudoajay;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import to_do_list.com.sudoajay.Adapter.Custom_Adapter_Recycleview;
import to_do_list.com.sudoajay.DataBase.DataBase;

public class MainActivity extends AppCompatActivity {
    // global variable
    private DataBase dataBase;
    private RecyclerView recycler_View;
    private ArrayList<String> task_Name;
    private ArrayList<Boolean> check_Box_Array;
    private TextView nothing_Text;
    private ImageView nothing_Image_View;
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


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_View.setLayoutManager(linearLayoutManager);
        Custom_Adapter_Recycleview custom_adapter_recycleview =
                new Custom_Adapter_Recycleview(this,task_Name,check_Box_Array);
        recycler_View.setAdapter(custom_adapter_recycleview);

        // visible or invisible
        if(custom_adapter_recycleview.getItemCount() > 0 ){
            nothing_Text.setVisibility(View.GONE);
            nothing_Image_View.setVisibility(View.GONE);
        }

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
        recycler_View = findViewById(R.id.recycler_View);
        nothing_Image_View = findViewById(R.id.nothing_Image_View);
        nothing_Text = findViewById(R.id.nothing_Text);

        // array start
        task_Name = new ArrayList<>();
        check_Box_Array= new ArrayList<>();

        // Grab & Setup
        Grab_And_Setup_Database();
    }
    private void Grab_And_Setup_Database() {

        if (dataBase.check_For_Empty()) {

        } else {

            Cursor cursor = dataBase.Get_All_Date();
                    if(cursor.moveToFirst()) {
                        int count =0;
                        do {
                                count++;
                            int return_value=check_For_Expiry_Date(cursor.getString(0));
                            if(return_value == 0){
                                dataBase.Delete_Row(count+"");
                            }else if(return_value == 1){
                                Fill_The_Adapter(1);
                            }
                        } while (cursor.moveToNext());
                    }
            cursor.close();
        }
        }
        // return 0 for expiry date
        // return 1 for today date
        // return 2 for future date
        private int check_For_Expiry_Date(String date){

            Calendar current_Date = Calendar.getInstance();
            int current_Year= current_Date.get(Calendar.YEAR);
            int current_Month= current_Date.get(Calendar.MONTH);
            int current_Day= current_Date.get(Calendar.DAY_OF_MONTH);

            String[] arrayList = date.split("-");
            Integer[] date_Array = Convert_String_To_Integer(arrayList);
            if(date_Array[2] >= current_Year ){
                if(date_Array[1] >= current_Month ){
                    if(date_Array[0] == current_Day ) return 1;
                    else if(date_Array[0] > current_Month )return 2;
                    else{
                        return 0;
                    }
                        }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }

        // convert String array to int array
        private Integer[] Convert_String_To_Integer(String[] array){
             Integer[] date_Array = new Integer[3];
            for(int i = 0 ; i < array.length ;i++){
                date_Array[i] =Integer.parseInt(array[i]);
            }
            return date_Array;
        }
        private void  Fill_The_Adapter(int id){
            Cursor cursor = dataBase.Specified_Row(id+"");
            if(cursor.moveToFirst()) {
                task_Name.add( cursor.getString(1)+"");
                check_Box_Array.add(false);
            }
        }
}
