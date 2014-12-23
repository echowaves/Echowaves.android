package com.echowaves.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class PickContactForBlendingActivity extends EWActivity {
    private static List<String> contactDetailsList;
    private static ListView contactDetailsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact_for_blending);

        contactDetailsList = getIntent().getStringArrayListExtra("contacts_details");

        ImageView backButton = (ImageView) findViewById(R.id.pickcontact_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });


        contactDetailsListView = (ListView) findViewById(R.id.pickcontact_listview);

        ArrayAdapter<String> contact_detail_adapter =
                new ArrayAdapter<String>(contactDetailsListView.getContext(), R.layout.row_pick_contact_detail_for_blending, contactDetailsList) {
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        View rowView = convertView;
                        // reuse views
                        if (rowView == null) {
                            LayoutInflater inflater = getLayoutInflater();
                            rowView = inflater.inflate(R.layout.row_pick_contact_detail_for_blending, null);
                            // configure view holder
                            final ViewHolder viewHolder = new ViewHolder();
                            viewHolder.contactDetail = (TextView) rowView.findViewById(R.id.contact_detail);

                            viewHolder.contactDetail
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //here set the detail info and return back to the calling intent
                                            String tmpResult = contactDetailsList.get(position);
                                            String returnSelected = tmpResult.substring(tmpResult.indexOf(":\n") + 2);
                                            Intent output = new Intent();
                                            output.putExtra(DetailedImageFragment.PICKED_CONTACT_FIELD, returnSelected);
                                            setResult(RESULT_OK, output);
                                            Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&& clicking finish: ", returnSelected);
                                            finish();
                                        }
                                    });
                            rowView.setTag(viewHolder);
                        }

                        // fill data
                        ViewHolder holder = (ViewHolder) rowView.getTag();
                        String contactDetail = contactDetailsList.get(position);
                        holder.contactDetail.setText(contactDetail);
                        return rowView;
                    }
                };

        contactDetailsListView.setAdapter(contact_detail_adapter);

    }

    static class ViewHolder {
        public TextView contactDetail;
    }


}
