package to_do_list.com.sudoajay.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import to_do_list.com.sudoajay.Adapter.Custom_Adapter_Recycleview;
import to_do_list.com.sudoajay.Create_New_To_Do_List;
import to_do_list.com.sudoajay.DataBase.DataBase;
import to_do_list.com.sudoajay.MainActivity;
import to_do_list.com.sudoajay.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Class_Fragement extends Fragment  implements View.OnClickListener{

    // global variable
    private MainActivity main_Activity;
    private String which_Tab;
    private RecyclerView recyclerView;
    private  View view;
    private Custom_Adapter_Recycleview custom_adapter_recycleview;
    private ArrayList<String> task_Name,save_All_Date,task_Info;
    private ArrayList<Boolean> check_Box_Array,similar_Check_Box_Array;
    private ArrayList<Integer> array_Id;
    private TextView nothing_Text_View;
    private ImageView nothing_Image_View;
    private int MYCODE=1000;
    private DataBase dataBase ;
    private final ArrayList<String> type_Array = new ArrayList<>(Arrays.asList("Overdue", "Today", "Overdo"));
    private final ArrayList<String> weeks_Array = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday"
            ,"Wednesday", "Thursday", "Friday" , "Saturday"));

    public Main_Class_Fragement() {
        // Required empty public constructor
    }
    public Main_Class_Fragement createInstance(MainActivity main_Activity ,String which_Tab ) {
        this.main_Activity = main_Activity;
        this.which_Tab = which_Tab;
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_class, container, false);


        // Reference
        Reference();


        // setup Recycler view
        Setup_Recycler_View();

        // grab the data from database
        // store in save_All_Data array
            Grab_The_Data_From_DB();

         // fill Recycle view
            Fill_Recycler_View();

        // check for empty
        if(custom_adapter_recycleview != null)
        if(custom_adapter_recycleview.getItemCount()!=0){
            nothing_Image_View.setVisibility(View.GONE);
            nothing_Text_View.setVisibility(View.GONE);
            custom_adapter_recycleview.notifyDataSetChanged();
        }

        return view;
    }
    private void Reference(){
        recyclerView = view.findViewById(R.id.recycler_View);
        nothing_Image_View = view.findViewById(R.id.nothing_Image_View);
        nothing_Text_View = view.findViewById(R.id.nothing_Text_View);

        // setup onclick listener
        nothing_Text_View.setOnClickListener(this);
        nothing_Image_View.setOnClickListener(this);

        // array start
        task_Name = new ArrayList<>();
        check_Box_Array= new ArrayList<>();
        save_All_Date = new ArrayList<>();
        task_Info = new ArrayList<>();
        array_Id = new ArrayList<>();

        // database create object
        dataBase= new DataBase(main_Activity);

    }
    private void Setup_Recycler_View(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(main_Activity.getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void Grab_The_Data_From_DB(){
        if(!dataBase.check_For_Empty()){
            Cursor cursor = dataBase.Get_All_Date_And_ID_Done();
            if (cursor != null) {
                cursor.moveToFirst();
               do
                {
                    array_Id.add(cursor.getInt(1));
                    save_All_Date.add(cursor.getString(0));

                    if(cursor.getInt(2) == 0 )
                        check_Box_Array.add(false);
                    else{
                        check_Box_Array.add(true);
                    }
                }while (cursor.moveToNext()); }

        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result OK.d.
        if (requestCode == MYCODE) {
            Toast.makeText(main_Activity,"something" , Toast.LENGTH_SHORT).show();
            // do something good
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nothing_Text_View:
            case R.id.nothing_Image_View:
                Intent intent = new Intent(main_Activity.getApplicationContext(), Create_New_To_Do_List.class);
                startActivity(intent);
                break;

        }
    }
    private void Fill_Recycler_View(){
        // Grab The suitable Date From Array
       if(!dataBase.check_For_Empty()) {
           Suitable_Date_From_Array();

           for(Integer value : array_Id) {
               Cursor cursor = dataBase.Get_The_Value_From_Id(value);
               if (cursor != null) {
                   cursor.moveToFirst();
                        task_Name.add(cursor.getString(1));


                        // Split into array
                        String split_Date[] = cursor.getString(2).split("-");

                        // Get the Day
                   Calendar calendar = new GregorianCalendar(Integer.parseInt(split_Date[2]), Integer.parseInt(split_Date[1])
                           , Integer.parseInt(split_Date[0])); // Note that Month value is 0-based. e.g., 0 for January.

                        String day = Return_The_Day(calendar.get(Calendar.DAY_OF_WEEK));

                        String date = cursor.getString(3);
                        if(date.equals("")) date = "No Date";

                        task_Info.add(day +" , " +cursor.getString(2) + " , " + date);

                        // add to adapter and list view show
                            custom_adapter_recycleview = new Custom_Adapter_Recycleview(main_Activity
                                    ,task_Name,check_Box_Array,task_Info,array_Id,dataBase,this);
                            recyclerView.setAdapter(custom_adapter_recycleview);

               }
           }
       }
    }
    private void Suitable_Date_From_Array(){
        Calendar calendar = Calendar.getInstance();
        int current_Year= calendar.get(Calendar.YEAR);
        int current_Month= calendar.get(Calendar.MONTH);
        int current_Day= calendar.get(Calendar.DAY_OF_MONTH);

        if(which_Tab.equals(type_Array.get(0))){
            for(int i= save_All_Date.size()-1 ; i >=0 ;i--){
                String arr[]= save_All_Date.get(i).split("-");
                if(!((Integer.parseInt(arr[2]) < current_Year)
                        ||(Integer.parseInt(arr[1]) < current_Month)||
                        (Integer.parseInt(arr[0]) < current_Day))) {
                    save_All_Date.remove(i);
                    array_Id.remove(i);
                }
            }

        }else if(which_Tab.equals(type_Array.get(1))){
            for(int i= save_All_Date.size()-1; i >=0 ;i--) {
                String arr[] = save_All_Date.get(i).split("-");
                if (!((Integer.parseInt(arr[2]) == current_Year)
                        && (Integer.parseInt(arr[1]) == current_Month) &&
                        (Integer.parseInt(arr[0]) == current_Day))){
                    save_All_Date.remove(i);
                    array_Id.remove(i);
            }
            }

        }else {
            for (int i = save_All_Date.size() - 1; i >= 0; i--) {
                String arr[] = save_All_Date.get(i).split("-");
                if (!((Integer.parseInt(arr[2]) > current_Year)
                        || (Integer.parseInt(arr[1]) > current_Month) ||
                        (Integer.parseInt(arr[0]) > current_Day))) {
                    save_All_Date.remove(i);
                    array_Id.remove(i);
                }
            }
        }

    }
    private String Return_The_Day(int result){
        switch (result){
            case Calendar.SUNDAY:
                return weeks_Array.get(0);
            case Calendar.MONDAY:
                return weeks_Array.get(1);
            case Calendar.TUESDAY:
                return weeks_Array.get(2);
            case Calendar.WEDNESDAY:
                return weeks_Array.get(3);
            case Calendar.THURSDAY:
                return weeks_Array.get(4);
            case Calendar.FRIDAY:
                return weeks_Array.get(5);
            default:
                return weeks_Array.get(6);
        }
    }

    public void Make_Them_Default(int type ,int count){
        if(type == 1){
            // copy from one array to another array
            similar_Check_Box_Array=new ArrayList<>(check_Box_Array);

            for(int i = 0 ; i< check_Box_Array.size();i++) {
                if(i != count)
                check_Box_Array.set(i,false);
                else{
                    check_Box_Array.set(i,true);
                }
            }
        }else{
            for(int i = 0 ; i< check_Box_Array.size();i++) {
               check_Box_Array.set(i,similar_Check_Box_Array.get(i));
            }
        }
        custom_adapter_recycleview.notifyDataSetChanged();
    }

    public Custom_Adapter_Recycleview getCustom_adapter_recycleview() {
        return custom_adapter_recycleview;
    }
}
