package to_do_list.com.sudoajay.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import to_do_list.com.sudoajay.DataBase.Main_DataBase;
import to_do_list.com.sudoajay.DataBase.Setting_Database;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by harun on 1/10/18.
 */

public class ResponseBroadcastReceiver extends BroadcastReceiver {
    private Setting_Database setting_database;
    private Main_DataBase main_dataBase;

    @Override
    public void onReceive(Context context, Intent intent) {
        //get the broadcast message

        int resultCode=intent.getIntExtra("resultCode",RESULT_CANCELED);
        if (resultCode==RESULT_OK){
            String message=intent.getStringExtra("toastMessage");
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();



        }
    }

}
