package com.example.andre.calendaralarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Andre on 3/5/2018.
 */

public class DeleteActivity extends AppCompatActivity{
    private DatabaseManager dbManager;

    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        dbManager = new DatabaseManager( this );
        updateView( );
    }

    public void updateView( ) {
        ArrayList<Alarm> alarms = dbManager.getAlarmsWithId();
        RelativeLayout layout = new RelativeLayout( this );
        ScrollView scrollView = new ScrollView( this );
        RadioGroup group = new RadioGroup( this );
        for ( Alarm alarm : alarms ) {
            RadioButton rb = new RadioButton( this );
            rb.setId( alarm.getId());
            rb.setText( alarm.alarmString() + " " + alarm.getOnOff());
            group.addView( rb );
        }
        // set up event handling
        RadioButtonHandler rbh = new RadioButtonHandler( );
        group.setOnCheckedChangeListener(rbh);

        // create a back button
        Button backButton = new Button( this );
        backButton.setText( R.string.back );

        backButton.setOnClickListener( new View.OnClickListener( ) {
            public void onClick(View v) {
                DeleteActivity.this.finish();
            }
        });

        scrollView.addView(group);
        layout.addView( scrollView );

        // add back button at bottom
        RelativeLayout.LayoutParams params
                = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT );
        params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
        params.addRule( RelativeLayout.CENTER_HORIZONTAL );
        params.setMargins( 0, 0, 0, 200 );
        layout.addView( backButton, params );

        setContentView( layout );
    }

    private class RadioButtonHandler
            implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged( RadioGroup group, int checkedId ) {
            // delete alarm from database
            dbManager.deleteById( checkedId );
            Toast.makeText( DeleteActivity.this, "Alarm deleted",
                    Toast.LENGTH_SHORT ).show( );

            // update screen
            updateView( );
        }
    }
}
