package com.example.andre.calendaralarm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity  {
    final Calendar c = Calendar.getInstance();
    int currentDay = c.get(Calendar.DAY_OF_MONTH);
    int currentYear = c.get(Calendar.YEAR);
    int currentMonth = c.get(Calendar.MONTH);
    int currentHour = c.get(Calendar.HOUR_OF_DAY);
    int currentMinute = c.get(Calendar.MINUTE);

    boolean dateSet = false;
    boolean timeSet = false;

    Calendar alarm = Calendar.getInstance();
    DatabaseManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    public void setDate (View view) {
        final EditText editTextDate = (EditText) findViewById(R.id.DateSelect);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int yearPicked, int monthPicked, int dayPicked) {
                        editTextDate.setText( monthPicked+1 + "/" + dayPicked + "/" + yearPicked);
                        alarm.set(Calendar.DAY_OF_MONTH, dayPicked);
                        alarm.set(Calendar.MONTH, monthPicked);
                        alarm.set(Calendar.YEAR, yearPicked);
                        dateSet = true;
                        enableDoneButton();
                        Log.i("Date and Time", String.valueOf(yearPicked) + String.valueOf(monthPicked) + String.valueOf(dayPicked));
                    }

                }, currentYear, currentMonth, currentDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        dateSet = false;
    }


    public void setTime (View view){

        final EditText editTextTime = (EditText) findViewById(R.id.TimeSelect);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourPicked, int minutePicked) {
                        String AM_PM ;
                        String hour;
                        if(hourPicked < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }

                        if(hourPicked>12){
                            hour = String.valueOf(hourPicked-12);
                        }
                        else if(hourPicked == 00){
                            hour = String.valueOf(12);
                        }
                        else{
                            hour = String.valueOf(hourPicked);
                        }
                        Log.i("Date and Time", String.valueOf(hourPicked) + String.valueOf(minutePicked));
                        editTextTime.setText( hour + ":" + String.format("%02d", minutePicked) + " " + AM_PM );
                        alarm.set(Calendar.MINUTE, minutePicked);
                        alarm.set(Calendar.HOUR_OF_DAY,hourPicked);
                        timeSet=true;
                        enableDoneButton();

                    }

                }, currentHour, currentMinute, false);

        timePickerDialog.show();
        timeSet=false;
    }

    public int getAlarmYear(){return alarm.get(Calendar.YEAR);}

    public int getAlarmMonth(){return alarm.get(Calendar.MONTH);}

    public int getAlarmDay(){return alarm.get(Calendar.DAY_OF_MONTH);}

    public int getAlarmHour(){return alarm.get(Calendar.HOUR_OF_DAY);}

    public int getAlarmMinute(){return alarm.get(Calendar.MINUTE);}

    public long getTimeInMilli(){
        Log.i("Time in Milliseconds", String.valueOf(alarm.getTimeInMillis()));
        return alarm.getTimeInMillis();

    }

    private void enableDoneButton(){
        if(dateSet == true && timeSet == true){
            Button doneButton = findViewById(R.id.DoneButton);
            doneButton.setEnabled(true);
        }
    }

    public void done(View view){
        dbManager = new DatabaseManager(this);
        dbManager.insert(getTimeInMilli());
        dbManager.fetchData();

        Toast.makeText( AddActivity.this, "Alarm added",
                Toast.LENGTH_SHORT ).show( );

        this.finish();
    }
}
