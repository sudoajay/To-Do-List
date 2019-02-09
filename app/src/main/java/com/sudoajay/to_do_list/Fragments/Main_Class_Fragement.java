package com.sudoajay.to_do_list.Fragments;


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
import com.sudoajay.to_do_list.Create_New_To_Do_List;
import com.sudoajay.to_do_list.MainActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import com.sudoajay.to_do_list.Adapter.Custom_Adapter_Recycleview;
import com.sudoajay.to_do_list.DataBase.Main_DataBase;
import com.sudoajay.to_do_list.R;


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
    private ArrayList<String> task_Name,save_All_Date,task_Info,weeks,endlesslyData;
    private ArrayList<Boolean> check_Box_Array,similar_Check_Box_Array;
    private ArrayList<Integer> array_Id,onRepeatId;
    private TextView nothing_Text_View;
    private ImageView nothing_Image_View;
    private Main_DataBase mainDataBase;
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
        weeks = new ArrayList<>();
        onRepeatId = new ArrayList<>();
        endlesslyData = new ArrayList<>();

        // database create object
        mainDataBase = new Main_DataBase(getContext());


    }
    private void Setup_Recycler_View(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void Grab_The_Data_From_DB(){

        if(!mainDataBase.check_For_Empty()){
            Cursor cursor = mainDataBase.Get_All_Date_And_ID_Done_Week();
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
                    onRepeatId.add(cursor.getInt(3));

                    // if its have custom weekdays
                    if(cursor.getInt(3) == 5 ) weeks.add(cursor.getString(4));
                    else{
                        weeks.add(null);
                    }

                    endlesslyData.add(cursor.getString(5));
                }while (cursor.moveToNext()); }

        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result OK.d.
        int MYCODE = 1000;
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
       if(!mainDataBase.check_For_Empty()) {
           Suitable_Date_From_Array();

           for(Integer value : array_Id) {
               Cursor cursor = mainDataBase.Get_The_Value_From_Id(value);
               if (cursor != null) {
                   cursor.moveToFirst();
                        task_Name.add(cursor.getString(1));


                        // Split into array
                        String split_Date[] = cursor.getString(2).split("-");

                        // Get the Day
                   Calendar calendar = new GregorianCalendar(Integer.parseInt(split_Date[2]), Integer.parseInt(split_Date[1])
                           , Integer.parseInt(split_Date[0])); // Note that Month value is 0-based. e.g., 0 for January.

                        String day = Return_The_Day(calendar.get(Calendar.DAY_OF_WEEK));

                        String time = cursor.getString(3);
                        if(time.equals("")) time = "No Time Set";

                        List<Integer> dates =new ArrayList<>();
                        for(String  show: split_Date){
                            dates.add(Integer.parseInt(show));
                        }

                        String date = dates.get(0)+"-"+(dates.get(1)+1)+"-"+dates.get(2);

                        task_Info.add(day +" , " +date + " , " + time);

                        // add to adapter and list view show
                            custom_adapter_recycleview = new Custom_Adapter_Recycleview(main_Activity
                                    ,task_Name,check_Box_Array,task_Info,array_Id, mainDataBase,this);
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


        // previous day
        if(which_Tab.equals(type_Array.get(0))){
            for(int i= save_All_Date.size()-1 ; i >=0 ;i--){
                String arr[]= save_All_Date.get(i).split("-");
                if(!((Integer.parseInt(arr[2]) < current_Year) ||
                        ((Integer.parseInt(arr[2]) <= current_Year) &&(Integer.parseInt(arr[1]) < current_Month))
                        || ((Integer.parseInt(arr[2]) <= current_Year) &&(Integer.parseInt(arr[1]) <= current_Month)&& (Integer.parseInt(arr[0]) < current_Day)))){
                    save_All_Date.remove(i);
                    array_Id.remove(i);
                    check_Box_Array.remove(i);
                    onRepeatId.remove(i);
                    weeks.remove(i);
                }
                else {

                    // this is for weekdays configuration
                    if(onRepeatId.get(i) != 0){
                        createNewData(i);
                    }
                }
            }

            // today day
        }else if(which_Tab.equals(type_Array.get(1))){
            for(int i= save_All_Date.size()-1; i >=0 ;i--) {
                String arr[] = save_All_Date.get(i).split("-");
                if (!((Integer.parseInt(arr[2]) == current_Year)
                        && (Integer.parseInt(arr[1]) == current_Month) &&
                        (Integer.parseInt(arr[0]) == current_Day))){
                    save_All_Date.remove(i);
                    array_Id.remove(i);
                    check_Box_Array.remove(i);
                    onRepeatId.remove(i);
                    weeks.remove(i);
            }else {

                    // this is for weekdays configuration
                    if(onRepeatId.get(i) != 0){
                        createNewData(i);
                    }
                }
            }
        // tomorrow day
        }else {
            for (int i = save_All_Date.size() - 1; i >= 0; i--) {
                String arr[] = save_All_Date.get(i).split("-");
                if(!((Integer.parseInt(arr[2]) >current_Year) ||
                        ((Integer.parseInt(arr[2]) >= current_Year) &&(Integer.parseInt(arr[1]) > current_Month))
                        || ((Integer.parseInt(arr[2]) >= current_Year) &&(Integer.parseInt(arr[1]) >= current_Month)&& (Integer.parseInt(arr[0]) > current_Day)))){
                    save_All_Date.remove(i);
                    array_Id.remove(i);
                    check_Box_Array.remove(i);
                    onRepeatId.remove(i);
                    weeks.remove(i);
                }else {
//                    if(!weeks.get(i).equals("")){
//                        Create_New_Data_Future(i);
//                    }
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
        Fix_Database_As_Per_Tick();
        custom_adapter_recycleview.notifyDataSetChanged();
    }

    // tick button
    public void Tick_Action_Mode(ArrayList<Boolean> tick){
        for(int i = 0 ;i < tick.size();i++){
            check_Box_Array.set(i,tick.get(i));
        }
        custom_adapter_recycleview.notifyDataSetChanged();
    }

    // fix Database
    public void Fix_Database_As_Per_Tick(){
        for(int i = 0 ; i< array_Id.size();i++) {
            if(check_Box_Array.get(i))
                mainDataBase.Update_The_Table_For_Done(array_Id.get(i) +"", 1);
            else {
                mainDataBase.Update_The_Table_For_Done(array_Id.get(i) +"", 0);
            }
        }
    }

    // delete button on action mode
    public void Delete_Action_Mode(ArrayList<Boolean> tick){
        for(int i = tick.size()-1 ;i >=0;i--){
            if(tick.get(i)){
                task_Name.remove(i);
                task_Info.remove(i);
                mainDataBase.deleteRow(array_Id.get(i)+"");
                array_Id.remove(i);
                tick.remove(i);

            }
        }
        custom_adapter_recycleview.notifyDataSetChanged();
    }
    // this is for today only
    // and this method duplicate the date
    private void createNewData(int i){

        Log.i("getSomething"," --- " + i );

        // get the day of today
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK), count = 0;

        // Week days Store in Integer
        // for mon -fir
        // for sat , sun
        // for custom weekdays
        List<Integer> week_Days1,week_Days2,week_Days3 = null;
        switch(onRepeatId.get(i)){
            case 1:
                // this is for daily repeating
                count=1;
                break;
            case 2:
                // this is for mon -fri week days only
                week_Days1 = new ArrayList<>(Arrays.asList(2,3,4,5,6));
                count = Count_Days_Main(day, week_Days1);
                break;
            case 3:
                // this is for sat , sunday week day only
                week_Days2 = new ArrayList<>(Arrays.asList(1,7));
                count = Count_Days_Main(day, week_Days2);
                break;
            case 4:
                // this is for same data for next month
                // empty here because its work on next day method
                break;

            case 5:
            // this method for custom weekdays in repeat section
            // convert string week days to int array

             week_Days3 = new ArrayList<>();
                for (int k = 0;k < weeks.get(i).length(); k++) {
                    week_Days3.add(Integer.parseInt(String.valueOf(weeks.get(i).charAt(k))));
                }

            count = Count_Days_Main(day, week_Days3);
                break;
        }

            // grab the data from id
            // create new data with new next date
            // empty today date repeat column

        // first part
        Cursor cursor = mainDataBase.Get_The_Value_From_Id(array_Id.get(i));
        if (cursor != null) {
            cursor.moveToFirst();

            // find the current day , current month , current year
            Calendar calendar = Calendar.getInstance();
            int current_Year= calendar.get(Calendar.YEAR);
            int current_Month= calendar.get(Calendar.MONTH);
            int current_Day= calendar.get(Calendar.DAY_OF_MONTH);
            int currentWeek =calendar.get(Calendar.DAY_OF_WEEK);


            if(endlesslyData.get(i) != null){
            // split the array int string  by "-"
            String[] array= endlesslyData.get(i).split("-");

                if(((Integer.parseInt(array[2]) >current_Year) ||
                        ((Integer.parseInt(array[2]) >= current_Year) &&(Integer.parseInt(array[1]) > current_Month))
                        || ((Integer.parseInt(array[2]) >= current_Year) &&(Integer.parseInt(array[1]) >= current_Month)&& (Integer.parseInt(array[0]) > current_Day))))
                {

                    mainDataBase.Fill_It(cursor.getString(1), Next_Day(count, cursor.getString(2), i), cursor.getString(3), cursor.getInt(4)
                            , cursor.getString(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8));

                }
            }else{
                mainDataBase.Fill_It(cursor.getString(1), Next_Day(count, cursor.getString(2), i), cursor.getString(3), cursor.getInt(4)
                        , cursor.getString(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8));
            }
            // clear the data
            // repeat id , weekdays , endlessly
            ClearTheData(array_Id.get(i)+"");



            if(onRepeatId.get(i) == 2 ){
                if(currentWeek == 7 || currentWeek == 1) deleteEverything(i);
            }else if(onRepeatId.get(i) == 3 ){
                if(currentWeek == 2 || currentWeek == 3 || currentWeek == 4 || currentWeek == 5
                        || currentWeek == 6) deleteEverything(i);
            }else if(onRepeatId.get(i) ==5 ){
                // it used for deleted an row
                // match boolean
                boolean matches =false;

                assert week_Days3 != null;
                for(Integer weeks:week_Days3){
                    if(weeks == currentWeek)matches =true;
                }
                if(!matches) deleteEverything(i);
            }
            // its not matches and remove the data from everywhere

            // refresh the list
            if(custom_adapter_recycleview != null )
            custom_adapter_recycleview.notifyDataSetChanged();
        }
    }

//    // this is for future day
//    // check if the repeat match with day
//    private void Create_New_Data_Future(int i) {
//        // local variable
//        List<Integer> days = new ArrayList<>();
//        int count = 0;
//
//        String[] split_Date = save_All_Date.get(i).split("-");
//
//
//        Calendar calendar = new GregorianCalendar(Integer.parseInt(split_Date[2]), Integer.parseInt(split_Date[1])
//                , Integer.parseInt(split_Date[0])); // Note that Month value is 0-based. e.g., 0 for January.
//
//        int day_No = calendar.get(Calendar.DAY_OF_WEEK);
//
//        // get the repeat of specified id
//        Cursor cursor = mainDataBase.Get_The_Repeat_From_Id(array_Id.get(i));
//        if (cursor != null) {
//            cursor.moveToFirst();
//
//            String[] split = cursor.getString(0).split("");
//
//            for (String show : split) {
//                try {
//                    days.add(Integer.parseInt(show));
//                } catch (Exception e) {
//
//                }
//            }
//        }
//
//        for (Integer get : days) {
//            if (get == day_No) {
//                return;
//            }
//        }
//
//        count = Count_Days_Main(day_No, days);
//
//        // grab the specified date from id
//        Cursor cursors = mainDataBase.Get_The_Date_From_Id(array_Id.get(i));
//        if (cursors != null) {
//            cursors.moveToFirst();
//            mainDataBase.Update_The_Table_For_Date(array_Id.get(i)+"", Next_Day(count,cursors.getString(0)));
//        }
//    }
    //this method for weekdays next date
    private static int Count_Days_Main(int day,  List<Integer> week_Days){
        int temp =day,count=0;
        do{
            count++;
            temp++;
            if(temp==8)temp=1;

            for(Integer week:week_Days){
                if(temp == week)return count;
            }
        }while(temp != day);
        return 0;
    }
    private String Next_Day( int count ,   String date,final int i){

        String[] split = date.split("-");
        List<Integer> dates = new ArrayList<>();
        for (String days : split) {
            try {
                dates.add(Integer.parseInt(days));
            } catch (Exception e) {

            }
        }
      if(onRepeatId.get(i)!=4 ) {

            // make count
          count+=dates.get(0);
          // check for feb
          if ((dates.get(1) == 1) && ((count) >= 28)) {
              date = Change_Month_Feb(dates, count);
          }

          // check for months pass
          else if ((count) >= 30) {
              date = Change_Month(dates, count);
          } else {
              date = (count) + "-" + dates.get(1) + "-" + dates.get(2);
          }
      }else{
            // this is for once a month
            date  = getOnceAMonth(dates.get(0),(dates.get(1)),dates.get(2));
          }
        return date;
    }
    // except feb month
    private String Change_Month( List<Integer> dates , int count){
        String return_back = null;
        Integer month_30[] = {3,5,8,10};
        Integer month_31[] = {0,2,4,6,7,9,11};

        // for 31 Day month
        for(Integer month31: month_31){
            if(dates.get(1).intValue() == month31){
                count %= 31;
                if(month31 == 11)
                    return_back = count+"-"+1+"-"+(dates.get(2)+1);
                else {
                    return_back = count+"-"+(dates.get(1)+1)+"-"+dates.get(2);
                }
            }
        }


        // for 30 Day month
        for(Integer month30: month_30){
            if(dates.get(1).intValue() == month30){
                count %= 30;
                return_back = count+"-"+(dates.get(1)+1)+"-"+dates.get(2);
            }
        }
        return return_back;
    }
    // month feb
    private String Change_Month_Feb( List<Integer> dates , int count){

        if(dates.get(2) % 4 == 0) {
            count %= 29;
        }else {
            count %= 28;
        }
        return count+"-"+(dates.get(1)+1)+"-"+dates.get(2);

    }

    // clear the data from database
    // with no repeat , custom weekdays , endlessly
    private void ClearTheData(String id){
        mainDataBase.UpdateTheTableAndClear(id+"");
    }
    // this is for same day on Month day

    // check for
    private String getOnceAMonth(final int day , final int month , final int year ){
        if(day == 31){
            // for feb month
            if(month== 0){
                if(year%4 ==0)return 28+"-"+(month+1)+"-"+year;
                else {
                     return 29+"-"+(month+1)+"-"+year;
                }
            }
            // for specified 31 month except 6 bcz 7 also there
            else if(month ==2 || month ==4 || month == 7 || month == 9 ){
                return 30+"-"+(month+1)+"-"+year;
            }

        }
        // only for 11 month
        if(month == 11) return day+"-"+(0)+"-"+(year+1);

        return day+"-"+(month+1)+"-"+year;

    }

    // delete Everything
    private void deleteEverything(int i){
            mainDataBase.deleteRow(array_Id.get(i) + "");
            save_All_Date.remove(i);
            array_Id.remove(i);
            check_Box_Array.remove(i);
            onRepeatId.remove(i);
            weeks.remove(i);

    }
}
