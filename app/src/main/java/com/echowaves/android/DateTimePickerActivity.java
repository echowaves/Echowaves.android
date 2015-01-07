package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

//    public DateTimePickerActivity() {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);

        photosCount = (TextView) findViewById(R.id.dtpicker_photosCount);
        photosCount.setText("Wave " + Long.toString(ApplicationContextProvider.getPhotosCountSinceLast()) + " since:");

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
//        timePicker.setIs24HourView(true);

        Date dt = ApplicationContextProvider.getCurrentAssetDateTime();

        updatePickersFromDate(dt);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                updatePickersFromDate(getDateFromPickers());
                photosCount.setText("Wave " + Long.toString(ApplicationContextProvider.getPhotosCountSince(getDateFromPickers())) + " since:");
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                updatePickersFromDate(getDateFromPickers());
                photosCount.setText("Wave " + Long.toString(ApplicationContextProvider.getPhotosCountSince(getDateFromPickers())) + " since:");
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
        photosCount.setText("Wave " + Long.toString(ApplicationContextProvider.getPhotosCountSince(dt)) + " since:");

        Log.d("$$$$$$$$$$$$$$$$$$$$$$ updatePickersFromDate: ", dt.toString());

    }

    private Date getDateFromPickers() {
        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR,24);

        cal.set(Calendar.YEAR, datePicker.getYear());
        cal.set(Calendar.MONTH, datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

//        if(timePicker.getCurrentHour() < 12) {
//            cal.set(Calendar.AM_PM, Calendar.AM);
//        } else {
//            cal.set(Calendar.AM_PM, Calendar.PM);
//        }

        Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! getDateFromPickers: ", cal.getTime().toString());
        Log.d("++++++++++++++++++++++++++++ currentYear:", String.valueOf(datePicker.getYear()));
        Log.d("++++++++++++++++++++++++++++ currentMonth:", String.valueOf(datePicker.getMonth()));
        Log.d("++++++++++++++++++++++++++++ currentDayOfMonth:", String.valueOf(datePicker.getDayOfMonth()));
        Log.d("++++++++++++++++++++++++++++ currentHour:", timePicker.getCurrentHour().toString());
        Log.d("++++++++++++++++++++++++++++ currentMinute:", timePicker.getCurrentMinute().toString());
        return cal.getTime();
    }

}
