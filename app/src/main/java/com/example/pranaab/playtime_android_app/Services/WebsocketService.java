package com.example.pranaab.playtime_android_app.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.pranaab.playtime_android_app.WebSocket.ChatWebSocketListener;
import com.example.pranaab.playtime_android_app.WebSocket.SingletonWebSocketListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WebsocketService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.pranaab.playtime_android_app.Services.action.FOO";
    private static final String ACTION_BAZ = "com.example.pranaab.playtime_android_app.Services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.pranaab.playtime_android_app.Services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.pranaab.playtime_android_app.Services.extra.PARAM2";

    public WebsocketService() {
        super("WebsocketService");
    }

    public static final String CHAT_NOTIFICATION = "com.pranaab.android.service.receiver";


    public void beginChat(){
        Intent intent = new Intent(CHAT_NOTIFICATION);
        intent.setAction(CHAT_NOTIFICATION);
        sendBroadcast(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WebsocketService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String user_uid = intent.getStringExtra("user_uid");
        OkHttpClient client = new OkHttpClient.Builder().pingInterval(10, TimeUnit.SECONDS).build();
        okhttp3.Request request = new okhttp3.Request.Builder().url("ws://playtime-chat.herokuapp.com/connect?user_uid=" + "31a06a13-8126-4720-8d8f-ae17930282c9").build();
        ChatWebSocketListener listener = SingletonWebSocketListener.get_Instance(this, user_uid);
        WebSocket ws = client.newWebSocket(request, listener);

        Integer milli = client.pingIntervalMillis();
        Log.i("Websocket", milli.toString());

        client.dispatcher().executorService().shutdown();
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
