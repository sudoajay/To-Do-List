package to_do_list.com.sudoajay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Create_New_To_Do_List extends AppCompatActivity {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    // Globally Variable
    private ImageView mic_Image_View;
    private EditText enter_Task_Edit_Task,date_Edit_Text;
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
        switch (view.getId()){
            case R.id.back_Image_View:onBackPressed();
                break;
            case R.id.date_Edit_Text:
            case R.id.calendar_Image_View:
                Calendar_Date_Setup();
                break;
        }
    }
    public void Reference(){
        // all id Reference here
        mic_Image_View = findViewById(R.id.mic_Image_View);
        enter_Task_Edit_Task = findViewById(R.id.enter_Task_Edit_Task);
        date_Edit_Text = findViewById(R.id.date_Edit_Text);
    }

    public void startVoiceRecognitionActivity() {
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
            // matches is the result of voice input. It is a list of what the
            // user possibly said.
            // Using an if statement for the keyword you want to use allows the
            // use of any activity if keywords match
            // it is possible to set up multiple keywords to use the same
            // activity so more than one word will allow the user
            // to use the activity (makes it so the user doesn't have to
            // memorize words from a list)
            // to use an activity from the voice input information simply use
            // the following format;
            // if (matches.contains("keyword here") { startActivity(new
            // Intent("name.of.manifest.ACTIVITY")


        }
    }
    @SuppressLint("SimpleDateFormat")
    public void Calendar_Date_Setup(){

        final DatePickerBuilder builder = new DatePickerBuilder(this, listener)
                .pickerType(CalendarView.ONE_DAY_PICKER);
        DatePicker datePicker = builder.build();
        datePicker.show();

    }
    private OnSelectDateListener listener = new OnSelectDateListener() {
        @Override
        public void onSelect(List<Calendar> calendars) {

        }
    };
}
