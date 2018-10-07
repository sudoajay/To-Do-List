package to_do_list.com.sudoajay;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import to_do_list.com.sudoajay.DataBase.DataBase;

public class Create_New_To_Do_List extends AppCompatActivity implements MaterialSpinner.OnItemSelectedListener {

    // Globally Variable
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private ImageView mic_Image_View;
    private EditText enter_Task_Edit_Task,date_Edit_Text;
    private MaterialSpinner materialSpinner;
    private DataBase dataBase;
    private List<String> dataset_For_Repeat;
    private int repeat_Count=0;
    private  String set_Date;
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

        // Setup Custom Spinner
        Setup_Spinner();

        // Setup Current Date
        Setup_Current();

    }
    // all on click bustton come here .. or you say on click Listener
    public void On_Click_Process(View view){
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
                Calendar_Date_Setup();
                break;
        }
    }
    private void Setup_Current(){

        Calendar current_Time = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date find_Date = current_Time.getTime();

        set_Date = sdf.format(find_Date);

    }
    @SuppressLint("ClickableViewAccessibility")
    private void Reference(){
        // all id Reference here
        mic_Image_View = findViewById(R.id.mic_Image_View);
        enter_Task_Edit_Task = findViewById(R.id.enter_Task_Edit_Task);
        date_Edit_Text = findViewById(R.id.date_Edit_Text);
        materialSpinner = findViewById(R.id.materialSpinner);

        // create object of database
        dataBase= new DataBase(this);


        // date edit task seton touch
        date_Edit_Text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar_Date_Setup();
                return true;
            }
        });

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
    @SuppressLint("SimpleDateFormat")
    private void Calendar_Date_Setup(){

         DatePickerBuilder builder = new DatePickerBuilder(this, listener)
                .pickerType(CalendarView.ONE_DAY_PICKER)
                .date(Calendar.getInstance());

        DatePicker datePicker = builder.build();

        datePicker.show();

    }

    private void Setup_Spinner(){
         dataset_For_Repeat = new LinkedList<>(Arrays.asList(
                "Once", "Daily", "Mon to fri", "Sat and Sun"));

        materialSpinner.setItems(dataset_For_Repeat);
        materialSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        repeat_Count =position;
    }
    private OnSelectDateListener listener = new OnSelectDateListener() {
        @Override
        public void onSelect(List<Calendar> calendars) {

            Calendar current_Time = Calendar.getInstance();
            int current_Day = current_Time.get(Calendar.DAY_OF_MONTH);

            int last_Modified_Day = calendars.get(0).get(Calendar.DAY_OF_MONTH);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            Date find_Date = calendars.get(0).getTime();

            set_Date = sdf.format(find_Date);
            Toast.makeText(Create_New_To_Do_List.this, set_Date+"",Toast.LENGTH_LONG).show();
                if (current_Day == last_Modified_Day) date_Edit_Text.setText(getResources().getString(R.string.today_Date));
                else if (current_Day == (last_Modified_Day - 1)) date_Edit_Text.setText(getResources().getString(R.string.yesterday_Date));
                else {
                date_Edit_Text.setText(set_Date);
            }
        }
    };

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
                // fill in database
                date_Edit_Text.setText(set_Date);
                dataBase.Fill_It(enter_Task_Edit_Task.getText().toString(),date_Edit_Text.getText().toString(),get_Repeat());
                    onBackPressed();
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
        switch (repeat_Count){
            case 0:return dataset_For_Repeat.get(0);
            case 1:return dataset_For_Repeat.get(1);
            case 2:return dataset_For_Repeat.get(2);
            case 3:return dataset_For_Repeat.get(3);
        }
        return null;
    }
}
