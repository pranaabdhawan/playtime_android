package com.example.pranaab.playtime_android_app.WebSocket;

import android.content.SharedPreferences;

import com.example.pranaab.playtime_android_app.Services.WebsocketService;

/**
 * Created by pranaab on 2017-11-21.
 */

public class SingletonWebSocketListener {
    private static ChatWebSocketListener listener;

    private SingletonWebSocketListener(){}

    public static ChatWebSocketListener get_Instance(WebsocketService service, String user_uid, SharedPreferences pref){
        if(listener == null){
            listener = new ChatWebSocketListener(service, user_uid, pref);
        }
        return listener;
    }

    public static ChatWebSocketListener get_Instance(){
        return listener;
    }
}
