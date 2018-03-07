package com.example.andre.calendaralarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Khoi on 3/6/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String getExtraString = intent.getExtras().getString("extra");


        Intent serviceIntent = new Intent(context, AlarmService.class);

        //pass string from MainActivity to AlarmService
        serviceIntent.putExtra("extra",getExtraString);

        context.startService(serviceIntent);
        Log.i("Testing AlarmReceiver", getExtraString);
    }


}
