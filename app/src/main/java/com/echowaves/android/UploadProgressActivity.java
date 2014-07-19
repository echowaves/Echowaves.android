package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadProgressActivity extends EWActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_progress);

        Button pauseAllButton = (Button)findViewById(R.id.upload_pauseAllButton);
        //Listening to button event
        pauseAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent navBarIntent = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(navBarIntent);
            }
        });


    }

}
