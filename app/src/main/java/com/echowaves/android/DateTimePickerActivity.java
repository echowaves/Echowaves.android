package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.echowaves.android.model.ApplicationContextProvider;

import java.util.Calendar;
import java.util.Date;

// http://www.yogeshblogspot.com/datepicker-and-timepicker/

public class DateTimePickerActivity extends EWActivity {

    public DateTimePickerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);

        ImageView backButton = (ImageView) findViewById(R.id.dtpicker_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent navTabBarActivity = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(navTabBarActivity);
            }
        });


        final DatePicker datePicker = (DatePicker) findViewById(R.id.dtpicker_datePicker);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.dtpicker_timePicker);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(ApplicationContextProvider.getCurrentAssetDateTime());
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

        Button setTimeButton = (Button) findViewById(R.id.dtpicker_button);
        //Listening to button event
        setTimeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                DatePicker dp = (DatePicker) findViewById(R.id.dtpicker_datePicker);
                TimePicker tp = (TimePicker) findViewById(R.id.dtpicker_timePicker);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, dp.getYear());
                cal.set(Calendar.MONTH, dp.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
                cal.set(Calendar.HOUR, tp.getCurrentHour());
                cal.set(Calendar.MINUTE, tp.getCurrentMinute());

                ApplicationContextProvider.setCurrentAssetDateTime(cal.getTime());

                Intent navTabBarActivity = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(navTabBarActivity);
            }
        });

        Button setTimeButtonNow = (Button) findViewById(R.id.dtpicker_nowbutton);
        //Listening to button event
        setTimeButtonNow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                cal.setTime(new Date());
                datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

            }
        });


    }


}
