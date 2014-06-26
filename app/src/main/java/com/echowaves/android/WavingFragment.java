package com.echowaves.android;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class WavingFragment extends Fragment {

    private Spinner spinnerWaves;
    private TextView selectedWave;

    private String[] waves = {"Cupcake", "Donut", "Eclair", "Froyo",
            "Gingerbread", "HoneyComb", "IceCream Sandwich", "Jellybean",
            "kitkat"};

    @Override
    public void onStart() {
        super.onStart();
        Log.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Waving fragment ", "onStart()");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waving, container, false);

        Log.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Waving activity ", "onCreateView()");

        this.selectedWave = (TextView) view.findViewById(R.id.waving_wavePicked);
        this.spinnerWaves = (Spinner) view.findViewById(R.id.waving_wavePicker);

        ArrayAdapter<String> waves_adapter =
                new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, this.waves);


        waves_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerWaves.setAdapter(waves_adapter);

        spinnerWaves.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerWaves.setSelection(position);
                String selState = (String) spinnerWaves.getSelectedItem();
                selectedWave.setText("Selected Android OS:" + selState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        return view;
    }
}
