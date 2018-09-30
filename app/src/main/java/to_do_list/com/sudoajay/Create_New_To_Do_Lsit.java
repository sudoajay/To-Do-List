package to_do_list.com.sudoajay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Create_New_To_Do_Lsit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__new__to__do__lsit);
    }
    // all on click bustton come here .. or you say on click Listener
    public void On_Click_Process(View view){
        switch (view.getId()){
            case R.id.back_Image_View:onBackPressed();
                break;
        }
    }
}
