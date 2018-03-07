package com.example.andre.calendaralarm;

/**
 * Created by Andre on 3/4/2018.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseManager extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "alarmDB";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_ALARM = "alarms";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String ON = "onoff";

    public DatabaseManager( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    public void onCreate( SQLiteDatabase db ) {
        // build sql create statement
        String sqlCreate = "create table " + TABLE_ALARM + "( " + ID;
        sqlCreate += " integer primary key autoincrement, " + DATE;
        sqlCreate += " real, " + ON + " bit ) " ;

        db.execSQL( sqlCreate );
    }

    public void onUpgrade( SQLiteDatabase db,
                           int oldVersion, int newVersion ) {
        // Drop old table if it exists
        db.execSQL( "drop table if exists " + TABLE_ALARM );
        // Re-create tables
        onCreate( db );
    }

    public void insert( long alarm ) {
        SQLiteDatabase db = this.getWritableDatabase( );
        String sqlInsert = "insert into " + TABLE_ALARM;
        sqlInsert += " values( null, '" + alarm;
        sqlInsert += "', '" + 1 + "' )";

        db.execSQL( sqlInsert );
        db.close( );
    }

    public void deleteById( int id ) {
        SQLiteDatabase db = this.getWritableDatabase( );
        String sqlDelete = "delete from " + TABLE_ALARM;
        sqlDelete += " where " + ID + " = " + id;

        db.execSQL( sqlDelete );
        db.close( );
    }

    public void updateTime( int id, long alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlUpdate = "update " + TABLE_ALARM;
        sqlUpdate += " set " + DATE + " = '" + alarm + "'";
        sqlUpdate += " where " + ID + " = " + id;

        db.execSQL( sqlUpdate );
        db.close( );
    }

    public void updateOnOff( int id, int onOff) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlUpdate = "update " + TABLE_ALARM;
        sqlUpdate += " set " + ON + " = '" + onOff + "'";
        sqlUpdate += " where " + ID + " = " + id;

        db.execSQL( sqlUpdate );
        db.close( );
    }



    public static String getDate(long milliSeconds, String dateFormat)
    {

        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat/*"MM/dd/yyyy h:mm"*/);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }

    public ArrayList<String> fetchData() {
        String sqlQuery = "select * from " + TABLE_ALARM;
        ArrayList<String> listOfAllDates = new ArrayList<String>();
        ArrayList<Alarm> alarms = new ArrayList<>();
        String cDate = null;


        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    //iterate the cursor to get data.

                    cDate = getDate(cursor.getLong(1), "MM/dd/yyyy h:mm a");

                    listOfAllDates.add(cDate);
                    Alarm currentAlarm = new Alarm(Integer.parseInt(cursor.getString(0)), cDate,
                            Integer.parseInt(cursor.getString(2)), cursor.getLong(1));
                    alarms.add(currentAlarm);

                } while (cursor.moveToNext());
            }
            cursor.close();

            //Close the DB connection.
            db.close();



        }
        Log.i("alarms", String.valueOf(listOfAllDates));
        return listOfAllDates;
    }

    public ArrayList<Alarm> getAlarmsWithId(){
        String sqlQuery = "select * from " + TABLE_ALARM;

        SQLiteDatabase db = this.getWritableDatabase( );
        Cursor cursor = db.rawQuery( sqlQuery, null );

        ArrayList<Alarm> alarms = new ArrayList<Alarm>( );
        while( cursor.moveToNext( ) ) {
            Alarm currentAlarm = new Alarm(Integer.parseInt(cursor.getString(0)), getDate(cursor.getLong(1), "MM/dd/yyyy h:mm a"),
                    Integer.parseInt(cursor.getString(2)), cursor.getLong(1));
            alarms.add(currentAlarm);
        }
        db.close( );
        return alarms;
    }

}
