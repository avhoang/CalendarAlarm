package com.example.andre.calendaralarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Andre on 3/6/2018.
 */

public class AlarmService extends Service {

    MediaPlayer alarmNoise;
    int startId = 0;
    boolean alarmPlaying;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("AlarmService","Test Test");

        String state = intent.getExtras().getString("extra");

        /*
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent mainIntent = new Intent(this.getApplicationContext(),MainActivity.class);
        PendingIntent pendingMainActivity = PendingIntent.getActivity(this, 0,mainIntent,0);

        Notification notify = new Notification.Builder(this)
                .setContentTitle("Alarm is ringing")
                .setContentText("Click Here")
                .setContentIntent(pendingMainActivity)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notificationManager.notify(0,notify);
        */

        assert state !=null;
        if(state.equals("alarm on")){
            startId = 1;
        }
        else if(state.equals("alarm off")){
            startId = 0;
        }


        if(!this.alarmPlaying && startId == 1){
            alarmNoise = MediaPlayer.create(this,R.raw.alarm_noise);
            alarmNoise.start();
            Log.i("AlarmService", "Alarm not started But Alarm Should Start");
            this.alarmPlaying = true;
            this.startId = 0;
        }
        else if(!this.alarmPlaying && startId == 0){
            Log.i("AlarmService", " if there was not sound and you want end sound");
            this.alarmPlaying = false;
            this.startId = 0;
        }
        else if (this.alarmPlaying && startId == 1){
            Log.i("AlarmService", " if there is sound and you want start sound");
            this.alarmPlaying = true;
            this.startId = 0;
        }
        else {
            Log.i("AlarmService", " if there is sound and you want end");
            alarmNoise.stop();
            alarmNoise.reset();
            this.alarmPlaying = false;
            this.startId = 0;
        }


        //alarmNoise = MediaPlayer.create(this, R.raw.alarm_noise);
        //alarmNoise.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.alarmPlaying=false;

    }

}
