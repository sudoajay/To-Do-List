package com.sudoajay.to_do_list.ForegroundService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sudoajay.to_do_list.R;
public class ForegroundDialog {

    private Context mContext;
    private Activity activity;
    private TraceBackgroundService traceBackgroundService;
    // constructor
    public ForegroundDialog(final Context mContext, final Activity activity){
        this.mContext = mContext;
        this.activity = activity;
    }

    public  void call_Thread(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (!isServiceRunningInForeground(mContext, Foreground.class)) {
                Call_Custom_Permission_Dailog();
            }
        },3000);
    }
    public void Call_Custom_Permission_Dailog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_custom_foreground_permission);
        Button button_Learn_More = dialog.findViewById(R.id.see_More_button);
        Button button_Continue = dialog.findViewById(R.id.ok_Button);
        // if button is clicked, close the custom dialog

        button_Learn_More.setOnClickListener(v -> {
            try {
                final String url = "https://dontkillmyapp.com/problem?1";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                activity.startActivity(i);
            }catch (Exception ignored){

            }
            dialog.dismiss();
        });
        button_Continue.setOnClickListener(v -> {

            try {
                final String url = GetUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                activity.startActivity(i);

                traceBackgroundService = new TraceBackgroundService(mContext);
                traceBackgroundService.setForegroundServiceWorking(true);

                if(!isServiceRunningInForeground(mContext, Foreground.class)) {
                    // call Foreground Thread();
                    Intent startIntent = new Intent(mContext, Foreground.class);
                    startIntent.putExtra("com.sudoajay.whatapp_media_mover_to_sdcard.ForegroundDialog"
                            , "Start_Foreground");
                    activity.startService(startIntent);
                }
            }catch (Exception ignored){

            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private String GetUrl(){
        String deviceMan = android.os.Build.MANUFACTURER;
        switch (deviceMan){
            case "Xiaomi":return "https://dontkillmyapp.com/xiaomi";
            case "Nokia" :return "https://dontkillmyapp.com/nokia";
            case "OnePlus":return "https://dontkillmyapp.com/oneplus";
            case "Huawei" :return "https://dontkillmyapp.com/huawei";
            case "Meizu":return "https://dontkillmyapp.com/meizu";
            case "Samsung" :return "https://dontkillmyapp.com/samsung";
            case "Sony":return "https://dontkillmyapp.com/sony";
            case "HTC" :return "https://dontkillmyapp.com/htc";
            case "Google":return "https://dontkillmyapp.com/stock_android";
            case "Lenovo" :return "https://dontkillmyapp.com/lenovo";
            default: return  "https://dontkillmyapp.com/";

        }
    }

    public  boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                Log.i("Showwme", service.service.getClassName());
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }catch (Exception e){
            if (!ServicesWorking()) return true;
            return false;
        }
    }

    public boolean ServicesWorking() {
        traceBackgroundService.isBackgroundWorking();
        return  !traceBackgroundService.isBackgroundServiceWorking();
    }
}
