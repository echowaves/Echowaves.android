package com.echowaves.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.echowaves.android.model.EWBlend;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BlendwithActivity extends EWActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    private ListView completionsListView;
    private BlendsCompletionCustomAdapter defaultAdapter;
    private ArrayList<String> blendsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blendwith);

        ImageView backButton = (ImageView) findViewById(R.id.blendwith_imageViewBack);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent home = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                startActivity(home);
            }
        });

        searchView = (SearchView) findViewById(R.id.blendwith_searchBox);
//        searchView.requestFocus();

        searchView.onActionViewExpanded();

        if (searchView.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        completionsListView = (ListView) findViewById(R.id.blendwith_completionsListView);

        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this nameList and for producing
        // a view to represent an item in that data set.
        defaultAdapter = new BlendsCompletionCustomAdapter(this, blendsList);
        completionsListView.setAdapter(defaultAdapter);
        searchView.setOnQueryTextListener(this);


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& onQueryTextSubmit:", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& onQueryTextChange:", newText);

        if (newText.length() > 3) {
            displayResults(newText);
        } else {
            completionsListView.setAdapter(defaultAdapter);
        }

        return false;
    }

    private void displayResults(String query) {

        EWBlend.autoCompleteFor(query, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
//                EWWave.showLoadingIndicator(getApplicationContext());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonResponseArray) {
                Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponseArray.toString());


                blendsList = new ArrayList<String>(jsonResponseArray.length());
                for (int i = 0; i < jsonResponseArray.length(); i++) {
                    try {
                        Log.d("jsonObject::::::::::::::::::::::", jsonResponseArray.getJSONObject(i).toString());
                        Log.d("label:", jsonResponseArray.getJSONObject(i).getString("label"));
                        blendsList.add(jsonResponseArray.getJSONObject(i).getString("label"));
                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString(), e);
                    }
                }
                defaultAdapter = new BlendsCompletionCustomAdapter(getApplicationContext(), blendsList);
                completionsListView.setAdapter(defaultAdapter);

                // Click listener for the searched item that was selected
                completionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // Get the cursor, positioned to the corresponding row in the result set
                        String waveSelected = blendsList.get(position);

//                        blend wave request here
                        EWBlend.requestBlendingWith(waveSelected, new JsonHttpResponseHandler() {
                            @Override
                            public void onStart() {
                                EWBlend.showLoadingIndicator(searchView.getContext());
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
                                Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());
                                Intent navTabBarIntent = new Intent(getApplicationContext(), NavigationTabBarActivity.class);
                                startActivity(navTabBarIntent);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                                if (headers != null) {
                                    for (Header h : headers) {
                                        Log.d("................ failed   key: ", h.getName());
                                        Log.d("................ failed value: ", h.getValue());
                                    }
                                }
                                if (responseBody != null) {
                                    Log.d("................ failed : ", responseBody);
                                }
                                if (error != null) {
                                    Log.d("................ failed error: ", error.toString());

                                    String msg = "";
                                    if (null != responseBody) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(responseBody);
                                            msg = jsonResponse.getString("error");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        msg = error.getMessage();
                                    }


                                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                    builder.setTitle("Error")
                                            .setMessage(msg)
                                            .setCancelable(false)
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }


                            @Override
                            public void onFinish() {
                                EWBlend.hideLoadingIndicator();
                            }

                        });


                    }
                });

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                if (headers != null) {
                    for (Header h : headers) {
                        Log.d("................ failed   key: ", h.getName());
                        Log.d("................ failed value: ", h.getValue());
                    }
                }
                if (responseBody != null) {
                    Log.d("................ failed : ", responseBody);
                }
                if (error != null) {
                    Log.d("................ failed error: ", error.toString());

                    String msg = "";
                    if (null != responseBody) {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            msg = jsonResponse.getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        msg = error.getMessage();
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Error")
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }


            @Override
            public void onFinish() {
//                EWWave.hideLoadingIndicator();
            }
        });
    }

    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {
        protected TextView itemName;
    }

    public class BlendsCompletionCustomAdapter extends BaseAdapter {
        private ArrayList<String> mListItems;
        private LayoutInflater mLayoutInflater;

        public BlendsCompletionCustomAdapter(Context context, ArrayList<String> arrayList) {

            mListItems = arrayList;

            //get the layout inflater
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            //getCount() represents how many items are in the list
            return mListItems.size();
        }

        @Override
        //get the data of an item from a specific position
        //i represents the position of the item in the list
        public Object getItem(int i) {
            return null;
        }

        @Override
        //get the position id of the item from the list
        public long getItemId(int i) {
            return 0;
        }

        @Override

        public View getView(int position, View view, ViewGroup viewGroup) {

            // create a ViewHolder reference
            ViewHolder holder;

            //check to see if the reused view is null or not, if is not null then reuse it
            if (view == null) {
                holder = new ViewHolder();

                view = mLayoutInflater.inflate(R.layout.row_blend_with_list, null);
                holder.itemName = (TextView) view.findViewById(R.id.blendwith_list_item_text_view);

                // the setTag is used to store the data within this view
                view.setTag(holder);
            } else {
                // the getTag returns the viewHolder object set as a tag to the view
                holder = (ViewHolder) view.getTag();
            }

            //get the string item from the position "position" from array list to put it on the TextView
            String stringItem = mListItems.get(position);
            if (stringItem != null) {
                if (holder.itemName != null) {
                    //set the item name on the TextView
                    holder.itemName.setText(stringItem);
                }
            }

            //this method must return the view corresponding to the data at the specified position.
            return view;

        }

    }

}
