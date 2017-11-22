package com.example.pranaab.playtime_android_app.Chat.model;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pranaab.playtime_android_app.ItemListActivity;
import com.example.pranaab.playtime_android_app.Model.Event;
import com.example.pranaab.playtime_android_app.RequestQueueSingleton;
import com.example.pranaab.playtime_android_app.Services.WebsocketService;
import com.example.pranaab.playtime_android_app.WebSocket.ChatWebSocketListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by pranaab on 2017-11-21.
 */

public class UserRepository {
    private HashMap<String, User> chat_Users;

    private ChatWebSocketListener listener_caller;

    private SharedPreferences pref;

    public UserRepository(ChatWebSocketListener listener_caller, SharedPreferences pref){
        this.listener_caller = listener_caller;
        this.chat_Users = new HashMap<>();
        this.pref = pref;
    }

    public boolean getOrFetchUser(String uid){
        if(chat_Users.containsKey(uid)){
            return true;
        }
        else{
            return false;
        }
    }

    public void fetchUserAndUpdateUI(String uid, final String content){

        String userUrl = "https://playtime-core-api.herokuapp.com/api/users/" + uid;
        final String tokenHeader = "Token " + pref.getString("currUser", null);
        Log.i("token", tokenHeader);

        //Add token header

        final StringRequest userRequest = new StringRequest(Request.Method.GET, userUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String uid = jsonObject.getString("uid");
                            String name = jsonObject.getString("username");
                            String avatar_url = jsonObject.getString("profile_picture");
                            User user = new User("1",name, avatar_url,true);
                            user.setUid(uid);
                            chat_Users.put(uid, user);

                            listener_caller.sendMessageToActivity(content, uid); //Will be update in the UI thread.

                        }
                        catch(Exception e){
                            Log.i("AVATAREXCEPTION", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERRORFETCHINGAVATAR",error.toString());
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
        RequestQueueSingleton.getInstanceIfExists().addToRequestQueue(userRequest);


    }

    public synchronized User getUser(String uid){
        if(chat_Users.containsKey(uid)){
            return chat_Users.get(uid);
        }
        else {
            return null;
        }
    }
}
