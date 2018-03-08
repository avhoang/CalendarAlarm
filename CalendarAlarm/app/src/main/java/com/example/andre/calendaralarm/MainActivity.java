package com.example.andre.calendaralarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseManager dbManager;
    AlarmManager alarmManager;
    ScrollView scrollView;
    int buttonWidth;
    ArrayList<Alarm> alarmsList;
    ToggleButton[] buttons;
    int index;
    PendingIntent pendingIntent;
    //PendingIntent pendingIntentCancel;
    Intent alarmIntent;
    Boolean alarmShouldStart = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbManager = new DatabaseManager(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
            }
        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmIntent = new Intent(this, AlarmReceiver.class);

        scrollView = (ScrollView) findViewById( R.id.content );
        Point size = new Point( );
        getWindowManager( ).getDefaultDisplay( ).getSize( size );
        buttonWidth = size.x /2;
        updateView( );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            Intent deleteIntent = new Intent(this, DeleteActivity.class);
            this.startActivity(deleteIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addAlarm (){
        Intent intent = new Intent(this, AddActivity.class);
        this.startActivity(intent);

    }

    public void updateView(){
        alarmsList = dbManager.getAlarmsWithId();
        Calendar calendar = Calendar.getInstance();

        if( alarmsList.size( ) >= 0 ) {
            // remove subviews inside scrollView if necessary
            scrollView.removeAllViewsInLayout( );

            // set up the grid layout
            GridLayout grid = new GridLayout( this );
            grid.setRowCount( ( alarmsList.size( ) + 1 ) / 2 );
            grid.setColumnCount( 2 );




            // create array of buttons, 2 per row
            buttons = new ToggleButton[alarmsList.size()];


            // fill the grid
            index = 0;
            for (  final Alarm alarm : alarmsList) {
                // create the button
                buttons[index] = new ToggleButton(this);
                buttons[index].setTextOn(alarm.getAlarmTime() + "\n ON");
                buttons[index].setTextOff(alarm.getAlarmTime() + "\n OFF");

                alarmIntent.putExtra("extra","alarm on");
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this,alarm.getId(),
                        alarmIntent,0);

                if(alarm.getOnOff()==1) {
                    buttons[index].setChecked(true);
                    buttons[index].getBackground().setColorFilter(getResources().getColor(R.color.onColor), PorterDuff.Mode.MULTIPLY);
                    //startAlarm(alarm.getTimeInMil(),index);
                    alarmShouldStart=true;
                }
                else{
                    buttons[index].setChecked(false);
                    buttons[index].getBackground().setColorFilter(getResources().getColor(R.color.offColor), PorterDuff.Mode.MULTIPLY);
                    //stopAlarm(index);
                    alarmShouldStart=false;
                }

                /*
                if(calendar.getTimeInMillis() > alarm.getTimeInMil() + 60000){
                    buttons[index].setEnabled(false);
                    buttons[index].setText(alarm.getAlarmTime() + "\n OFF");
                    buttons[index].getBackground().setColorFilter(getResources().getColor(R.color.disabledColor), PorterDuff.Mode.MULTIPLY);
                    stopAlarm(index);

                }
                */

                // set up event handling
                buttons[index].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(alarm.getOnOff() == 1){
                            alarm.setOn_off(0);
                            dbManager.updateOnOff(alarm.getId(),0);
                            buttonView.getBackground().setColorFilter(getResources().getColor(R.color.offColor), PorterDuff.Mode.MULTIPLY);
                            Toast.makeText(getApplicationContext(),"Alarm Off", Toast.LENGTH_SHORT).show();
                            cancelAlarm(pendingIntent);
                            alarmShouldStart=false;
                            stopAlarm();
                            updateView();
                        }
                        else if(alarm.getOnOff() == 0){
                            alarm.setOn_off(1);
                            dbManager.updateOnOff(alarm.getId(),1);
                            buttonView.getBackground().setColorFilter(getResources().getColor(R.color.onColor), PorterDuff.Mode.MULTIPLY);
                            Toast.makeText(getApplicationContext(),"Alarm On", Toast.LENGTH_SHORT).show();
                            //startAlarm(alarm.getTimeInMil(), index);
                            alarmShouldStart=true;
                            updateView();
                        }

                    }
                });

                // add the button to grid
                grid.addView( buttons[index], buttonWidth,
                        GridLayout.LayoutParams.WRAP_CONTENT );


                Log.i("alarmManager milTime", String.valueOf(alarm.getTimeInMil()));
                if(alarmShouldStart){
                   //startAlarm(alarm.getTimeInMil(),index);
                    initializeAlarm(alarm.getTimeInMil());
                }
                else{
                    cancelAlarm(pendingIntent);
                }
                index++;
                Log.i("Index", String.valueOf(index));
            }



            scrollView.addView( grid );
        }
    }

    private void initializeAlarm(long alarmTime){
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent);
    }

    private PendingIntent startAlarm(long alarmTime, int requestCode){
        alarmIntent.putExtra("extra","alarm on");
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this,requestCode,
                alarmIntent,0);

        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent);

        return pendingIntent;
    }

    private void cancelAlarm(PendingIntent pendingIntentHolder){
        //pendingIntent = PendingIntent.getBroadcast(MainActivity.this, requestCode,
              //  alarmIntent,0);

        alarmManager.cancel(pendingIntentHolder);

    }

    private void stopAlarm(){
        alarmIntent.putExtra("extra","alarm off");
        sendBroadcast(alarmIntent);
    }

    protected void onResume( ) {
        super.onResume( );
        updateView( );
    }

}
