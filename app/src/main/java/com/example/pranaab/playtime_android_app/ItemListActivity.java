package com.example.pranaab.playtime_android_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.pranaab.playtime_android_app.Chat.DefaultMessagesActivity;
import com.example.pranaab.playtime_android_app.Model.DummyContent;
import com.example.pranaab.playtime_android_app.Model.Event;
import com.example.pranaab.playtime_android_app.Model.EventRepository;
import com.example.pranaab.playtime_android_app.Services.WebsocketService;
import com.example.pranaab.playtime_android_app.WebSocket.ChatWebSocketListener;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private EventRepository eventRepository;

    private RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> events_adapter;

    private BroadcastReceiver receiver;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(receiver != null) {
            Log.i("RECEIVER", "UNREGISTER");
            unregisterReceiver(receiver);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Events Around You");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        // Make request to get event suggestions. TODO: Refactor into EventRepositoryClass
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        eventRepository = new EventRepository(getApplicationContext(), events_adapter);     // Initially required only by this activity
        eventRepository.fetch_Events_Async(pref);


        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Log.i("Refreshing", "stay fresh");
                eventRepository.fetch_Events_Async(pref);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        Log.i("ACTIVE","i");


        //DefaultMessagesActivity.open(this);

        /*
        OkHttpClient client = new OkHttpClient.Builder().pingInterval(10, TimeUnit.SECONDS).build();
        okhttp3.Request request = new okhttp3.Request.Builder().url("ws://playtime-chat.herokuapp.com/connect?user_uid=31a06a13-8126-4720-8d8f-ae17930282c9").build();
        ChatWebSocketListener listener = new ChatWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);

        Integer milli = client.pingIntervalMillis();
        Log.i("Webscoket", milli.toString());

        client.dispatcher().executorService().shutdown();*/

        //Attaching the receiver
        if(receiver == null) {
            Log.i("ITEMLIST", "Setting new receiver");
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle bundle = intent.getExtras();
                    if(bundle!=null){
                        //See if something needs to be done
                    }
                    Log.i("ServiceReceiver", "Got Service Receiver Intent");
                    Intent stopIntent = new Intent(getApplicationContext(), WebsocketService.class);
                    stopService(stopIntent);


                    Intent activity_intent = new Intent(getApplicationContext(), DefaultMessagesActivity.class);
                    startActivity(activity_intent);
                    //DefaultMessagesActivity.open(getApplicationContext());

                }
            };
            registerReceiver(this.receiver, new IntentFilter(
                    WebsocketService.CHAT_NOTIFICATION));
        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        events_adapter = new SimpleItemRecyclerViewAdapter(EventRepository.events);
        recyclerView.setAdapter(events_adapter);
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(eventRepository.getEvents()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Event> mValues;

        public SimpleItemRecyclerViewAdapter(List<Event> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_event_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView = position;
            //holder.mImageView.setImageDrawable();
            //holder.mIdView.setText(mValues.get(position).getId().toString());
            holder.mContentView.setText(mValues.get(position).getName());

            Log.i("HERE","i");
            Glide.with(getApplicationContext()).load(mValues.get(position).getThumbnail_link()).into(holder.mImageView);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, Integer.toString(position) );
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        final Context context = v.getContext();
                        final Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, Integer.toString(position));
                        intent.putExtra("event_id", mValues.get(position).getUId());
                        final String eventid = mValues.get(position).getUId();

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        final String tokenHeader = "Token " + pref.getString("currUser", null);

                        String subscriptionUrl = "https://playtime-core-api.herokuapp.com/api/subscriptions/";
                        final StringRequest subscriptionRequest = new StringRequest(Request.Method.GET, subscriptionUrl,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("Response", response);
                                        String subButtonResponse = "subscribe";
                                        try {
                                            JSONArray jsonarray = new JSONArray(response);
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if(jsonobject.getString("event").equals(eventid)){
                                                    subButtonResponse = "unsubscribe";
                                                    intent.putExtra("unSubEvent", jsonobject.getString("uid"));
                                                    break;
                                                }
                                            }
                                            intent.putExtra("subscribeButton", subButtonResponse);
                                            context.startActivity(intent);
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
                                        context.startActivity(intent); //just go to it anyway
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
                        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(subscriptionRequest);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public int mIdView;
            public final TextView mContentView;
            public final ImageView mImageView;
            public Event mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = 0;
                mImageView = (ImageView) view.findViewById(R.id.thumbnail);
                //mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }


    }
}