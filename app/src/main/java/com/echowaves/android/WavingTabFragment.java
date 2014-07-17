package com.echowaves.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.echowaves.android.model.ApplicationContextProvider;
import com.echowaves.android.model.SecurePreferences;

import java.text.DateFormat;
import java.util.Date;

public class WavingTabFragment extends EWTabFragment {
    static final private SharedPreferences prefs = new SecurePreferences(ApplicationContextProvider.getContext());

    public static final String CURRENT_ASSET_DATE_TIME_KEY = "currentAssetDateTime";

    private static Date currentAssetDateTime;

    public static Date getCurrentAssetDateTime() {
        if(currentAssetDateTime == null) {
            long storedTime = prefs.getLong(CURRENT_ASSET_DATE_TIME_KEY, 0);
            if(storedTime == 0) {
                storedTime = (new Date()).getTime();
                setCurrentAssetDateTime(new Date(storedTime));
            }
            currentAssetDateTime = new Date(storedTime);
        }
        return currentAssetDateTime;
    }

    public static void setCurrentAssetDateTime(Date currentAssetDateTime) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(CURRENT_ASSET_DATE_TIME_KEY, currentAssetDateTime.getTime());
        editor.commit();
        WavingTabFragment.currentAssetDateTime = currentAssetDateTime;
    }




    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "WavingTabFragment onStart()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waving, container, false);
        Log.d("WavingTabFragment", view.toString());

        Button sinceDateTimeValueButton = (Button) view.findViewById(R.id.waving_sinceDateTimeValueButton);

        sinceDateTimeValueButton.setText(DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.SHORT).format(getCurrentAssetDateTime()));
        //Listening to button event
        sinceDateTimeValueButton.
                setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(final View v) {
                                Intent dtPickerIntent = new Intent(v.getContext(), DateTimePickerActivity.class);
                                startActivity(dtPickerIntent);
                            }
                        }
                );


        Button addWaveButton = (Button) view.findViewById(R.id.waving_addNewWaveButton);
        //Listening to button event
        addWaveButton.
                setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(final View v) {
                                Intent addChildWaveIntent = new Intent(v.getContext(), AddChildWaveActivity.class);
                                startActivity(addChildWaveIntent);
                            }
                        }
                );


        return view;
    }

    @Override
    public void updateWave(final String waveName) {
        super.updateWave(waveName);
        Log.d("&&&&&&&&&&&&&&&& WavingTabFragment updateWave", waveName);

        View view = getView();
        if (view != null) {
            Button editWaveButton = (Button) view.findViewById(R.id.waving_editWaveButton);
            editWaveButton.setText(waveName);
            //Listening to button event
            editWaveButton.
                    setOnClickListener(
                            new View.OnClickListener() {
                                public void onClick(final View v) {
                                    Intent waveDetails = new Intent(v.getContext(), WaveDetailsActivity.class);
                                    startActivity(waveDetails);
                                }
                            }
                    );
        }
    }
}
