package com.sudoajay.to_do_list;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import com.sudoajay.to_do_list.DataBase.Main_DataBase;

// outside library
import com.dpro.widgets.WeekdaysPicker;

import org.angmarch.views.NiceSpinner;


public class Create_New_To_Do_List extends AppCompatActivity {

    // Globally Variable
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private ImageView mic_Image_View,repeat_Off_Image_View;
    private EditText enter_Task_Edit_Task,time_Edit_Text,date_Edit_Text,endlessly_Edit_Text;
    private Main_DataBase mainDataBase;
    private WeekdaysPicker weekdays;
    private String getSelectedDate="",getSelectedEndlesslyDate;
        // default value is 24 for non empty time
    private int original_Time=24,get_Id ;
    private NiceSpinner custom_Spinner;


    // private OnSelectDateListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_to_do_list);

        // Reference here
        Reference();

        // set custom spinner
        setCustomSpinner();

        if(getIntent().getExtras() != null ){
            Intent intent = getIntent();
             get_Id=intent.getIntExtra("to_do_list.com.sudoajay.Adapter_Id",0);

            Cursor cursor = mainDataBase.Get_The_Value_From_Id(get_Id);
            if (cursor != null) {
                cursor.moveToFirst();
                do
                {
                    enter_Task_Edit_Task.setText(cursor.getString(1));

                    Sent_To_Date_Box(cursor.getString(2));

                    time_Edit_Text.setText(cursor.getString(3));


                    custom_Spinner.setSelectedIndex(cursor.getInt(4));

                    if(cursor.getInt(4) == 5) {
                        weekdays.setVisibility(View.VISIBLE);
                        Fill_The_Selected_Weekdays(cursor.getString(5));
                    }
                    if(cursor.getInt(4) != 0 ) {
                        endlessly_Edit_Text.setVisibility(View.VISIBLE);
                        repeat_Off_Image_View.setVisibility(View.VISIBLE  );
                        endlessly_Edit_Text.setText(cursor.getString(6));
                        if (endlessly_Edit_Text.getText().toString().isEmpty())
                            endlessly_Edit_Text.setText("No Date Set");
                    }
                }while (cursor.moveToNext()); }
        }
        // Check to see if a recognition activity is present
        // if running on AVD virtual device it will give this message. The mic
        // required only works on an actual android device
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            mic_Image_View.setOnClickListener(v -> startVoiceRecognitionActivity());
        } else {
            mic_Image_View.setEnabled(false);
            Toast.makeText(this , "Recognizer not present" , Toast.LENGTH_LONG).show();
        }



    }
    // all on click bustton come here .. or you say on click Listener
    @SuppressLint("SetTextI18n")
    public void On_Click_Process(View view){
        final Calendar c = Calendar.getInstance();
        switch (view.getId()){
            case R.id.back_Image_View:onBackPressed();
                break;
            case R.id.save_Image_View:
                if(date_Edit_Text.getText() == null || enter_Task_Edit_Task.getText().toString().equals("")){
                        Toast.makeText(Create_New_To_Do_List.this,"Enter Task" ,Toast.LENGTH_SHORT).show();
                }
                else if(date_Edit_Text.getText() == null || date_Edit_Text.getText().toString().equals(""))
                    Call_Custom_Dailog("Are You Sure To Save The Task ? With Today Date ?");
                else{
                    Call_Custom_Dailog("Are You Sure To Save The Task ?");
                }
                break;
            case R.id.date_Edit_Text:
            case R.id.calendar_Image_View:
                // Get Current Date

                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog;
                int theme;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        theme = android.R.style.Theme_Material_Light_Dialog;
                }else{
                        theme = android.R.style.Theme_Holo_Dialog;
                }
                    datePickerDialog = new DatePickerDialog(this,theme,
                            (view1, year, monthOfYear, dayOfMonth) -> {
                                         getSelectedDate = dayOfMonth+"-"+monthOfYear+"-"+year;
                                            date_Edit_Text.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                                        if((mYear ==year) && (mMonth ==monthOfYear)) {
                                            if (mDay == dayOfMonth)
                                                date_Edit_Text.setText(getResources().getString(R.string.today_Date));
                                            else if ((mDay-1) == dayOfMonth)
                                                date_Edit_Text.setText(getResources().getString(R.string.yesterday_Date));
                                            else if ((mDay+1) == dayOfMonth)
                                                date_Edit_Text.setText(getResources().getString(R.string.tomorrow_Date));
                                        }
                                // if the date is old
                                // then tell the user about this
                                String[] split_Date = getSelectedDate.split("-");
                                if(((Integer.parseInt(split_Date[2]) < c.get(Calendar.YEAR)) ||
                                        ((Integer.parseInt(split_Date[2]) <= c.get(Calendar.YEAR)) &&(Integer.parseInt(split_Date[1]) < c.get(Calendar.MONTH)))
                                        || ((Integer.parseInt(split_Date[2]) <= c.get(Calendar.YEAR)) &&(Integer.parseInt(split_Date[1]) <= c.get(Calendar.MONTH))&& (Integer.parseInt(split_Date[0]) < c.get(Calendar.DAY_OF_MONTH)))))
                                    Toast.makeText(getApplicationContext(), "Oops... The Date You selected is Already gone", Toast.LENGTH_SHORT).show();
                                else {
                                    // it will show the text which is present in date edit text
                                    Toast.makeText(getApplicationContext(), date_Edit_Text.getText().toString(), Toast.LENGTH_LONG).show();
                                }
                                }, mYear, mMonth, mDay);


                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    datePickerDialog.setIcon(R.drawable.check_icon);
                    datePickerDialog.setTitle("Please select Date.");
                }
                datePickerDialog.show();
                break;
            case R.id.time_Edit_Text:
            case R.id.time_Image_View:

               final int mHour = c.get(Calendar.HOUR_OF_DAY);
               final int mMinute = c.get(Calendar.MINUTE);
                final int nYear = c.get(Calendar.YEAR);
                final int nMonth = c.get(Calendar.MONTH);
                final int nDay = c.get(Calendar.DAY_OF_MONTH);
                // time picker dialog setup
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Dialog,
                        (view12, hourOfDay, minute) -> {
                            original_Time = hourOfDay;
                        String set_Time = Convert_Into(hourOfDay,minute);


                        time_Edit_Text.setText(set_Time);

                        if(getSelectedDate.equals("")){
                            if(TimeIsPast(mHour,mMinute,hourOfDay,minute))
                                Toast.makeText(getApplicationContext(), "Oops... The Time You selected is Already gone", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(Create_New_To_Do_List.this, set_Time+"",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            String[] split_Date = getSelectedDate.split("-");
                            if(Integer.parseInt(split_Date[0]) == nDay && Integer.parseInt(split_Date[1]) == nMonth
                                    && Integer.parseInt(split_Date[2]) == nYear){
                                if(TimeIsPast(mHour,mMinute,hourOfDay,minute))
                                    Toast.makeText(getApplicationContext(), "Oops... The Time You selected is Already gone", Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(Create_New_To_Do_List.this, set_Time+"",Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                        }, mHour, mMinute, false);

                timePickerDialog.setIcon(R.drawable.check_icon);
                timePickerDialog.setTitle("Please select time.");
                timePickerDialog.show();
                break;
            case R.id.repeat_Image_View:
                setCustomSpinner();
            case R.id.repeat_Off_Image_View:
            case R.id.endlessly_Edit_Text:
                // Get Current Date

                final int cYear = c.get(Calendar.YEAR);
                final int cMonth = c.get(Calendar.MONTH);
                final int cDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog1;
                int theme1;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    theme1 = android.R.style.Theme_Material_Light_Dialog;
                }else{
                    theme1 = android.R.style.Theme_Holo_Dialog;
                }
                datePickerDialog1 = new DatePickerDialog(this,theme1,
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            getSelectedEndlesslyDate = dayOfMonth+"-"+monthOfYear+"-"+year;
                            endlessly_Edit_Text.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                            if((cYear ==year) && (cMonth ==monthOfYear)) {
                                if (cDay == dayOfMonth)
                                    endlessly_Edit_Text.setText(getResources().getString(R.string.today_Date));
                                else if ((cDay-1) == dayOfMonth)
                                    endlessly_Edit_Text.setText(getResources().getString(R.string.yesterday_Date));
                                else if ((cDay+1) == dayOfMonth)
                                    endlessly_Edit_Text.setText(getResources().getString(R.string.tomorrow_Date));
                            }

                            // if date already done then show the user
                            String[] split_Date = getSelectedEndlesslyDate.split("-");
                            if(((Integer.parseInt(split_Date[2]) < c.get(Calendar.YEAR)) ||
                                    ((Integer.parseInt(split_Date[2]) <= c.get(Calendar.YEAR)) &&(Integer.parseInt(split_Date[1]) < c.get(Calendar.MONTH)))
                                    || ((Integer.parseInt(split_Date[2]) <= c.get(Calendar.YEAR)) &&(Integer.parseInt(split_Date[1]) <= c.get(Calendar.MONTH))&& (Integer.parseInt(split_Date[0]) < c.get(Calendar.DAY_OF_MONTH)))))
                                Toast.makeText(getApplicationContext(), "Oops... The Date You selected is Already gone", Toast.LENGTH_SHORT).show();
                            else {
                                // print the endlessly_Edit_Text text
                                Toast.makeText(getApplicationContext(), endlessly_Edit_Text.getText().toString(), Toast.LENGTH_LONG).show();
                            }
                        }, cYear, cMonth, cDay);


                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    datePickerDialog1.setIcon(R.drawable.check_icon);
                    datePickerDialog1.setTitle("Please select Date.");
                }
                datePickerDialog1.show();
                break;
        }
    }

    // convert 24hr to 12 hr
    private String Convert_Into(int hourOfDay,int minute){
        String min = minute+"";
        if(minute < 10)min="0"+minute;
        if(hourOfDay / 12 == 0){
            return hourOfDay+":"+min +" AM ";
        }else{
            return (hourOfDay%12)+":"+min +" PM ";
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void Reference(){
        // all id Reference here
        mic_Image_View = findViewById(R.id.mic_Image_View);
        enter_Task_Edit_Task = findViewById(R.id.enter_Task_Edit_Task);
        date_Edit_Text = findViewById(R.id.date_Edit_Text);
        time_Edit_Text = findViewById(R.id.time_Edit_Text);
        weekdays =  findViewById(R.id.weekdays);
        custom_Spinner =  findViewById(R.id.custom_Spinner);
        repeat_Off_Image_View = findViewById(R.id.repeat_Off_Image_View);
        endlessly_Edit_Text = findViewById(R.id.endlessly_Edit_Text);

        // create object of database
        mainDataBase = new Main_DataBase(this);

        // setup weekdays selector
        // select today day default
        Calendar calendar = Calendar.getInstance();
        weekdays.selectDay(calendar.get(Calendar.DAY_OF_WEEK));

    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            enter_Task_Edit_Task.setText(matches.get(0)+ "");



        }
    }

    // set spinner list for repeat
    private void setCustomSpinner(){

        List<String> repeat_Array = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.custom_Weekdays_Setup)));
        custom_Spinner.attachDataSource(repeat_Array);

        // custom listener with

        custom_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endlessly_Edit_Text.setVisibility(View.VISIBLE);
                repeat_Off_Image_View.setVisibility(View.VISIBLE);
                if(position== 5){
                    weekdays.setVisibility(View.VISIBLE);
                }else{
                    weekdays.setVisibility(View.GONE);
                }
                if(position ==0){
                    endlessly_Edit_Text.setVisibility(View.GONE);
                    repeat_Off_Image_View.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void Call_Custom_Dailog(String Message) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_custom_save_dialog);
        TextView text_Message = dialog.findViewById(R.id.text_Message);
        text_Message.setText(Message);
        TextView button_No = dialog.findViewById(R.id.button_No);
        TextView button_Yes = dialog.findViewById(R.id.button_Yes);
        // if button is clicked, close the custom dialog

        button_Yes.setOnClickListener(v -> {
            // if date and time is empty
            final Calendar c = Calendar.getInstance();

            if(date_Edit_Text.getText().toString().equals(""))
                getSelectedDate = c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR);

            // if time is empty
            if(time_Edit_Text.getText().toString().equals("")) time_Edit_Text.setText(null);

            // if endlessly is empty
            if(endlessly_Edit_Text.getText().toString().equals("No Date Set")) getSelectedEndlesslyDate=null;

            // update the database
            if(getIntent().getExtras() != null){
                mainDataBase.Update_The_Table(get_Id+"",enter_Task_Edit_Task.getText().toString(),getSelectedDate,
                        time_Edit_Text.getText().toString(),custom_Spinner.getSelectedIndex(),get_Repeat(),
                        getSelectedEndlesslyDate,0,original_Time);
            }else{
                  // fill in database
                mainDataBase.Fill_It(enter_Task_Edit_Task.getText().toString(),getSelectedDate,
                        time_Edit_Text.getText().toString(),custom_Spinner.getSelectedIndex(),get_Repeat(),
                        getSelectedEndlesslyDate,0,original_Time);
            }

            // check when date already overdue



            // intent to open main activity class
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

            dialog.dismiss();
        });
        button_No.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    public String get_Repeat(){
        List<Integer> weekday = weekdays.getSelectedDays();
        String join="";
        for(Integer week: weekday){
            join+=week+"";
        }
        return join;
    }

    // put value to date box
    private void Sent_To_Date_Box(String value){
        Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        String[] split = value.split("-");
        int dayOfMonth =Integer.parseInt(split[0]);
        int monthOfYear =Integer.parseInt(split[1]);
        int year =Integer.parseInt(split[2]);


        getSelectedDate = dayOfMonth+"-"+monthOfYear+"-"+year;
        date_Edit_Text.setText(getSelectedDate);
        if((mYear ==year) && (mMonth ==monthOfYear)) {
            if (mDay == dayOfMonth)
                date_Edit_Text.setText(getResources().getString(R.string.today_Date));
            else if ((mDay-1) == dayOfMonth)
                date_Edit_Text.setText(getResources().getString(R.string.yesterday_Date));
            else if ((mDay+1) == dayOfMonth)
                date_Edit_Text.setText(getResources().getString(R.string.tomorrow_Date));
        }

    }

    // week days selected
    private void Fill_The_Selected_Weekdays(String week){
        String[] split  =week.split("");
        List<Integer> list = new ArrayList<>();
        for(String weeks_Days: split){
            try {
                list.add(Integer.parseInt(weeks_Days));
            }catch (Exception e){

            }
            }
        weekdays.setSelectedDays(list);
    }


    // check for time was past or not
    private boolean TimeIsPast(int mHour , int mMinute,int selHour, int selMinute){
        if(mHour > selHour)return true;
        else if(mHour == selHour && mMinute >selMinute)
            return true;
        return false;
    }
}
