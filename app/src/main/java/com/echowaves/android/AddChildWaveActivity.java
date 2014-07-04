package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AddChildWaveActivity extends EWActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_wave);

        ImageView backButton = (ImageView) findViewById(R.id.addwave_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent home = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(home);
            }
        });


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.add_child_wave, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
