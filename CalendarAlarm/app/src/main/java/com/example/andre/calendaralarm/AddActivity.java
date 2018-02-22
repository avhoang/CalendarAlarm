package com.example.andre.calendaralarm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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

                    }

                }, currentYear, currentMonth, currentDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    public void setTime (View view){
        final EditText editTextTime = (EditText) findViewById(R.id.TimeSelect);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourPicked, int minutePicked) {
                        String AM_PM ;
                        if(hourPicked < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }

                        if(hourPicked>12){
                            hourPicked-=12;
                        }

                        editTextTime.setText( hourPicked + ":" + String.format("%02d", minutePicked) + " " + AM_PM );

                    }

                }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }
    public void done(View view){
        this.finish();
    }
}
