package com.example.pranaab.playtime_android_app.WebSocket;

import com.example.pranaab.playtime_android_app.Services.WebsocketService;

/**
 * Created by pranaab on 2017-11-21.
 */

public class SingletonWebSocketListener {
    private static ChatWebSocketListener listener;

    private SingletonWebSocketListener(){}

    public static ChatWebSocketListener get_Instance(WebsocketService service, String user_uid){
        if(listener == null){
            listener = new ChatWebSocketListener(service, user_uid);
        }
        return listener;
    }

    public static ChatWebSocketListener get_Instance(){
        return listener;
    }
}
