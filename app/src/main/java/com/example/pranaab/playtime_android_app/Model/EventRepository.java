package com.example.pranaab.playtime_android_app.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pranaab.playtime_android_app.ItemListActivity;
import com.example.pranaab.playtime_android_app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pranaab on 2017-11-17.
 */

public class EventRepository {

    public static List<Event> events = new ArrayList<>();
    private Context applicationContext;

    //View : for MVC design pattern
    private RecyclerView.Adapter<ItemListActivity.SimpleItemRecyclerViewAdapter.ViewHolder> events_adapter;


    public EventRepository(Context context, RecyclerView.Adapter<ItemListActivity.SimpleItemRecyclerViewAdapter.ViewHolder> adptr) {
        //events = new ArrayList<Event>();
        events_adapter = adptr;
    }

    public void fetch_Events_Async(SharedPreferences pref){

        // First clear current events
        EventRepository.events.clear();

        final String tokenHeader = "Token " + pref.getString("currUser", null);
        Log.i("token", tokenHeader);

        String interestsUrl = "https://playtime-core-api.herokuapp.com/api/events";
        final StringRequest interestRequest = new StringRequest(Request.Method.GET, interestsUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                Log.i("name", jsonobject.getString("name"));
                                String uid = jsonobject.getString("uid");
                                String name = jsonobject.getString("name");
                                String expiry = jsonobject.getString("expiry");
                                Integer subscriber_count = jsonobject.getInt("subscriber_count");
                                Integer max_subscribers = jsonobject.getInt("max_subscribers");
                                String start_time = jsonobject.getString("start_time");
                                String end_time = jsonobject.getString("end_time");
                                String location = jsonobject.getString("location");
                                String thumbnail_link = jsonobject.getString("thumbnail_link");

                                events.add(new Event(uid, name, subscriber_count, max_subscribers, location, expiry,  start_time, end_time,  thumbnail_link));
                                events.get(i).setDetails();
                            }

                            //Update view again
                            Log.i("NOTIFYSETCHANGED", getClass().toString());
                            events_adapter.notifyDataSetChanged();
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
                        //goes here when the login fails
                        Log.d("ERROR","error => "+error.toString());
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
        RequestQueueSingleton.getInstance(applicationContext).addToRequestQueue(interestRequest);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
