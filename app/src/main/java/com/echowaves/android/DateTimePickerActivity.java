package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.echowaves.android.model.ApplicationContextProvider;

import java.util.Calendar;
import java.util.Date;

// http://www.yogeshblogspot.com/datepicker-and-timepicker/

public class DateTimePickerActivity extends EWActivity {
    private TextView photosCount;

    private DatePicker datePicker;
    private TimePicker timePicker;


    public DateTimePickerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);

        photosCount = (TextView) findViewById(R.id.dtpicker_photosCount);
        photosCount.setText(Long.toString(ApplicationContextProvider.getPhotosCountSinceLast()));


        ImageView backButton = (ImageView) findViewById(R.id.dtpicker_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent navTabBarActivity = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(navTabBarActivity);
            }
        });


        datePicker = (DatePicker) findViewById(R.id.dtpicker_datePicker);
        timePicker = (TimePicker) findViewById(R.id.dtpicker_timePicker);


        Date dt = ApplicationContextProvider.getCurrentAssetDateTime();

        updatePickersFromDate(dt);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                photosCount.setText(Long.toString(ApplicationContextProvider.getPhotosCountSince(getDateFromPickers())));
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                photosCount.setText(Long.toString(ApplicationContextProvider.getPhotosCountSince(getDateFromPickers())));
            }
        });



        Button setTimeButton = (Button) findViewById(R.id.dtpicker_button);
        //Listening to button event
        setTimeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                ApplicationContextProvider.setCurrentAssetDateTime(getDateFromPickers());
                finish();
            }
        });

        Button setTimeButtonNow = (Button) findViewById(R.id.dtpicker_nowbutton);
        //Listening to button event
        setTimeButtonNow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                updatePickersFromDate(new Date());
            }
        });


    }

    private void updatePickersFromDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        photosCount.setText(Long.toString(ApplicationContextProvider.getPhotosCountSince(dt)));
    }

    private Date getDateFromPickers() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, datePicker.getYear());
        cal.set(Calendar.MONTH, datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        cal.set(Calendar.HOUR, timePicker.getCurrentHour());
        cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        return cal.getTime();
    }




}
