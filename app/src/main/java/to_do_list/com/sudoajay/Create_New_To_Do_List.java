package to_do_list.com.sudoajay;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import to_do_list.com.sudoajay.DataBase.DataBase;

// outside library
import com.dpro.widgets.WeekdaysPicker;




public class Create_New_To_Do_List extends AppCompatActivity {

    // Globally Variable
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private ImageView mic_Image_View;
    private EditText enter_Task_Edit_Task,time_Edit_Text,date_Edit_Text;
    private DataBase dataBase;
    private WeekdaysPicker weekdays;
    private String get_Selected_Date;
    private int original_Time;


    // private OnSelectDateListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_to_do_list);

        // Reference here
        Reference();

        // Check to see if a recognition activity is present
        // if running on AVD virtual device it will give this message. The mic
        // required only works on an actual android device
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            mic_Image_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVoiceRecognitionActivity();
                }
            });
        } else {
            mic_Image_View.setEnabled(false);
            Toast.makeText(this , "Recognizer not present" , Toast.LENGTH_LONG).show();
        }

    }
    // all on click bustton come here .. or you say on click Listener
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
                            new DatePickerDialog.OnDateSetListener() {

                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                             get_Selected_Date = dayOfMonth+"-"+monthOfYear+"-"+year;
                                                date_Edit_Text.setText(get_Selected_Date);
                                            if((mYear ==year) && (mMonth ==monthOfYear)) {
                                                if (mDay == dayOfMonth)
                                                    date_Edit_Text.setText(getResources().getString(R.string.today_Date));
                                                else if ((mDay-1) == dayOfMonth)
                                                    date_Edit_Text.setText(getResources().getString(R.string.yesterday_Date));
                                                else if ((mDay+1) == dayOfMonth)
                                                    date_Edit_Text.setText(getResources().getString(R.string.tomorrow_Date));
                                            }
                                    Toast.makeText(Create_New_To_Do_List.this, date_Edit_Text.getText().toString(),Toast.LENGTH_LONG).show();
                                }
                            }, mYear, mMonth, mDay);

                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    datePickerDialog.setIcon(R.drawable.done_icon);
                    datePickerDialog.setTitle("Please select Date.");
                }

                datePickerDialog.show();

                break;
            case R.id.time_Edit_Text:
            case R.id.time_Image_View:

                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                original_Time = hourOfDay;
                                String set_Time = Convert_Into(hourOfDay,minute);
                                Toast.makeText(Create_New_To_Do_List.this, set_Time+"",Toast.LENGTH_LONG).show();
                                time_Edit_Text.setText(set_Time);

                            }
                        }, mHour, mMinute, false);

                timePickerDialog.setIcon(R.drawable.done_icon);
                timePickerDialog.setTitle("Please select time.");
                timePickerDialog.show();
                break;
        }
    }

    // convert 24hr to 12 hr
    private String Convert_Into(int hourOfDay,int minute){
        if(hourOfDay / 12 == 0){
            return hourOfDay+":"+minute +" AM ";
        }else{
            return (hourOfDay%12)+":"+minute +" PM ";
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

        // create object of database
        dataBase= new DataBase(this);

        // setup weekdays selector
        // no day select default
        weekdays.selectDay(0);

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

    public void Call_Custom_Dailog(String Message) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_custom_save_dialog);
        TextView text_Message = dialog.findViewById(R.id.text_Message);
        text_Message.setText(Message);
        TextView button_No = dialog.findViewById(R.id.button_No);
        TextView button_Yes = dialog.findViewById(R.id.button_Yes);
        // if button is clicked, close the custom dialog

        button_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if date and time is empty
                final Calendar c = Calendar.getInstance();
                if(date_Edit_Text.getText().toString().equals(""))
                    get_Selected_Date = c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR);
                if(time_Edit_Text.getText().toString().equals(""))
                    time_Edit_Text.setText(null);

                // fill in database
                dataBase.Fill_It(enter_Task_Edit_Task.getText().toString(),get_Selected_Date,
                        time_Edit_Text.getText().toString(),get_Repeat(),0,original_Time);

                // intent to open main activity class
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });
        button_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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

}
