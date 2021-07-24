package com.example.blooddonation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       if(isAirPlaneModeOn(context.getApplicationContext())){
           Toast.makeText(context, "AirPlane mode is on", Toast.LENGTH_SHORT).show();
       }
       else{
           Toast.makeText(context, "AirPlane mode is off", Toast.LENGTH_SHORT).show();
       }
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private static boolean isAirPlaneModeOn(Context context){
        return Settings.System.getInt(context.getContentResolver(),Settings.Global.AIRPLANE_MODE_ON,0)!=0;
    }
}