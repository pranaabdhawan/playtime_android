package com.example.pranaab.playtime_android_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    private int subscriber_count;

    private int max_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        final Button _subscribeButton = (Button) findViewById(R.id.btn_event_subscribe);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String tokenHeader = "Token " + pref.getString("currUser", null);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Go to messenger! Building the Messaging Service", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(getIntent().getStringExtra("subscribeButton").equals("subscribe")){
            _subscribeButton.setText("Subscribe");
            _subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    String postSubscriptionsURL = "https://playtime-core-api.herokuapp.com/api/subscriptions/";
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("event", getIntent().getStringExtra("event_id"));

                    JsonObjectRequest req = new JsonObjectRequest(postSubscriptionsURL, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Toasty.success(getApplicationContext(), "Successful Subscription!", Toast.LENGTH_SHORT, true).show();
                                        VolleyLog.v("Subscription Response:%n %s", response.toString(4));
                                            finish();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toasty.error(getApplicationContext(), "Subscription Error!", Toast.LENGTH_SHORT, true).show();
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization", tokenHeader);
                            headers.put("Content-Type", "application/json");
                            return headers;
                        }
                    };
                    RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
                }
            });
        }
        else{
            _subscribeButton.setText("Unsubscribe");
            _subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    String subscriptionsURL = "https://playtime-core-api.herokuapp.com/api/subscriptions/";
                    subscriptionsURL += getIntent().getStringExtra("unSubEvent") + "/";

                    Log.i("unsub url", subscriptionsURL);

                    final StringRequest subscriptionRequest = new StringRequest(Request.Method.DELETE, subscriptionsURL,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d("Response", response);
                                    String subButtonResponse = "subscribe";
                                    try {
                                        Toasty.success(getApplicationContext(), "Successfully Unsubscribed!", Toast.LENGTH_SHORT, true).show();
                                        Log.d("Unsubscribe Response", response);
                                        finish();
                                    }
                                    catch(Exception e){
                                        Log.i("Excpetion Raised", e.getMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                    //goes here when the request fails
                                    Toasty.error(getApplicationContext(), "Unsubscription Error!", Toast.LENGTH_SHORT, true).show();
                                    Log.d("Unsubscribe Error","error => "+error.toString());
                                }
                            }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<>();
                            params.put("Authorization", tokenHeader);
                            return params;
                        }
                    };
                    RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(subscriptionRequest);
                }
            });
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }


        String eventsUrl = "https://playtime-core-api.herokuapp.com/api/events/";
        eventsUrl += getIntent().getStringExtra("event_id") + "/";

        final StringRequest eventRequest = new StringRequest(Request.Method.GET, eventsUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            subscriber_count = Integer.parseInt(jsonObject.getString("subscriber_count"));
                            max_count = Integer.parseInt(jsonObject.getString("max_subscribers"));
                            if(jsonObject.getString("max_subscribers").equals(jsonObject.getString("subscriber_count")) && getIntent().getStringExtra("subscribeButton").equals("subscribe")){
                                _subscribeButton.setEnabled(false);
                            }
                        }
                        catch(Exception e){
                            Log.i("Exception Raised", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //goes here when the request fails
                        Log.d("Specific Event Error","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Authorization", tokenHeader);
                return params;
            }
        };
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(eventRequest);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
