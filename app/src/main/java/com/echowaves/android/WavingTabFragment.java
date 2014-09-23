package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.echowaves.android.model.ApplicationContextProvider;

import java.text.DateFormat;

public class WavingTabFragment extends EWTabFragment {

    private TextView photosCount;
    private Button sinceDateTimeValueButton;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("!!!!!!!!!!!!!", "WavingTabFragment onResume()");
        photosCount.setText(Long.toString(ApplicationContextProvider.getPhotosCountSinceLast()));
        sinceDateTimeValueButton.setText(DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.SHORT).format(ApplicationContextProvider.getCurrentAssetDateTime()));

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("!!!!!!!!!!!!!", "WavingTabFragment onStart()");
        photosCount.setText(Long.toString(ApplicationContextProvider.getPhotosCountSinceLast()));
        sinceDateTimeValueButton.setText(DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.SHORT).format(ApplicationContextProvider.getCurrentAssetDateTime()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waving, container, false);
        Log.d("WavingTabFragment", view.toString());

        sinceDateTimeValueButton = (Button) view.findViewById(R.id.waving_sinceDateTimeValueButton);
        sinceDateTimeValueButton.setText(DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.SHORT).format(ApplicationContextProvider.getCurrentAssetDateTime()));
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


        photosCount = (TextView) view.findViewById(R.id.waving_photosCount);

        Button waveAllButton = (Button) view.findViewById(R.id.waving_waveAllButton);
        //Listening to button event
        waveAllButton.
                setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(final View v) {
                                Intent pickWavesIntent = new Intent(v.getContext(), PickWavesForUploadActivity.class);
                                startActivity(pickWavesIntent);
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
//            editWaveButton.setText(waveName);
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
