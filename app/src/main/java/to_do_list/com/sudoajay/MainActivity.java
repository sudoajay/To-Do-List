package to_do_list.com.sudoajay;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    // global variable
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Refrence here
        Refernece();
    }

    public void Refernece(){
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use Intent to pass on Create new Activity
                Intent intent = new Intent(getApplicationContext(),Create_New_To_Do_Lsit.class);
                startActivity(intent);

            }
        });
    }
}
