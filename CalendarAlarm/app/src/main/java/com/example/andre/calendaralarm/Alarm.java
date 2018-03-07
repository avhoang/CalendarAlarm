package com.example.andre.calendaralarm;

import java.util.Calendar;

/**
 * Created by Andre on 3/5/2018.
 */

public class Alarm {
    private int id;
    private String alarmTime;
    private int on_off;
    private long timeInMil;

    public Alarm (int newId, String alarm, int onOff, long milTime){
        setId(newId);
        setTime(alarm);
        setOn_off(onOff);
        setTimeInMil(milTime);

    }

    public void setId(int newId){
        id = newId;
    }

    public void setTime(String time){
        alarmTime = time;
    }

    public void setOn_off(int on_or_off){
        on_off = on_or_off;
    }

    public int getId(){
        return id;
    }

    public String getAlarmTime(){
        return alarmTime;
    }

    public int getOnOff(){
        return on_off;
    }

    public void setTimeInMil(long mil){
        timeInMil = mil;
    }

    public long getTimeInMil(){
        return timeInMil;
    }

    public String alarmString(){
        return id + ", " + alarmTime;
    }


}
