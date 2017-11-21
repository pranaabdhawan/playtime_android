package com.example.pranaab.playtime_android_app.WebSocket;

import android.app.IntentService;
import android.os.Parcelable;
import android.util.Log;

import com.example.pranaab.playtime_android_app.Chat.ChatActivity;
import com.example.pranaab.playtime_android_app.Chat.DefaultMessagesActivity;
import com.example.pranaab.playtime_android_app.Services.WebsocketService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.Buffer;
import okio.ByteString;

/**
 * Created by pranaab on 2017-11-18.
 */

    public class ChatWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        private String event_uid; // Event uid of the chat

        private WebsocketService service;

        private String user_uid;

        private WebSocket webSocket;

        public DefaultMessagesActivity chatActivity;

        public ChatWebSocketListener(WebsocketService service, String user_uid){
            this.user_uid = user_uid;
            this.service = service;
        }

        public String cur_message;



        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            this.webSocket = webSocket;
            Log.i("WebSocketListener", "Websocket opeened");
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
                                                String content = message.getString("content");
                                                this.cur_message = content;
                                                chatActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        chatActivity.showReceivedText(cur_message);
                                                        //stuff that updates ui

                                                    }
                                                });
                                                //send_chat_message("Hello chat people!");
                                                //send_chat_message("How are you today?");
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

        public void send_chat_message(String text){
            try {
                JSONObject message = new JSONObject();
                message.put("type", "CHAT_MESSAGE");
                message.put("content",text);
                message.put("event_uid",this.event_uid);
                message.put("sender","31a06a13-8126-4720-8d8f-ae17930282c9");
                String message_string = message.toString();
                this.webSocket.send(message_string);
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
                this.service.beginChat();
            }catch(JSONException e){

            }

        }
    }

