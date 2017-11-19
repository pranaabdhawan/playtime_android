package com.example.pranaab.playtime_android_app.WebSocket;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.Buffer;
import okio.ByteString;

/**
 * Created by pranaab on 2017-11-18.
 */


    public final class ChatWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        private String event_uid; // Event uid of the chat

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            /*try {
                JSONObject message = new JSONObject();
                message.put("type", "CHAT_MESSAGE");
                message.put("content","Hello, it's Pranaab");
                message.put("event_uid","123");
                message.put("sender","31a06a13-8126-4720-8d8f-ae17930282c9");
                String message_string = message.toString();
                webSocket.send(message_string);

            }catch(JSONException e){

            }*/
            //webSocket.send("Hello, it's Pranaab !");
            //webSocket.send("What's up ?");
            //webSocket.send(ByteString.decodeHex("deadbeef"));
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.i("WebSocketListener","Receiving : " + text);
            try {
                JSONObject message = new JSONObject(text);
                String type = message.getString("type");
                String event_uid = message.getString("event_uid");
                switch(type){
                    case "EVENT_INVITATION":    Log.i("WebSocketListener","Invite type : " + type);
                                                acceptEventInvitation(webSocket, event_uid);
                                                break;
                    case "EVENT_ACCEPT":        Log.i("WebSocketListener","Event Accept type : " + type);
                                                break;
                    case "CHAT_MESSAGE":        Log.i("WebSocketListener","Chat message type : " + type);
                                                send_chat_message(webSocket,"Hello chat people!");
                                                send_chat_message(webSocket,"How are you today?");
                                                break;
                    default: break;
                }
            }catch(JSONException e){

            }

        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.i("WebSocketListener","Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.i("WebSocketListener","Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.i("WebSocketListener","Error : " + t.getMessage());
        }

        private void send_chat_message(WebSocket webSocket, String text){
            try {
                JSONObject message = new JSONObject();
                message.put("type", "CHAT_MESSAGE");
                message.put("content",text);
                message.put("event_uid",this.event_uid);
                message.put("sender","31a06a13-8126-4720-8d8f-ae17930282c9");
                String message_string = message.toString();
                webSocket.send(message_string);
            }catch(JSONException e){

            }
        }

        private void acceptEventInvitation(WebSocket webSocket, String event_uid){
            // Set the event uid
            this.event_uid = event_uid;
            try {
                JSONObject message = new JSONObject();
                message.put("type", "EVENT_ACCEPT");
                message.put("event_uid",this.event_uid);
                String message_string = message.toString();
                webSocket.send(message_string);
                Log.i("WebSocketListener","Invitation Accepted : ");
            }catch(JSONException e){

            }

        }
    }

