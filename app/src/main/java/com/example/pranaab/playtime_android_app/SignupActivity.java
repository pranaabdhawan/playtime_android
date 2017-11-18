package com.example.pranaab.playtime_android_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {

    private Button interests_button;

    private Button signup_button;

    private EditText name_text;

    private EditText email_text;

    private EditText password_text;


    ArrayList<String> listItems = new ArrayList<String>();

    // Boolean is needed by the Adapter for the Alert Dialog Box
    boolean[] checkedItems;

    // Contains the indices of the selected values, not the interests themselves.
    ArrayList<Integer> userItems = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        interests_button = (Button) findViewById(R.id.btn_interests);

        signup_button = (Button) findViewById(R.id.btn_signup);

        name_text = (EditText) findViewById(R.id.input_name);

        email_text = (EditText) findViewById(R.id.input_email);

        password_text = (EditText) findViewById(R.id.input_password);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final String tokenHeader = "Token " + pref.getString("currUser", null);
        Log.i("token", tokenHeader);

        String interestsUrl = "https://playtime-core-api.herokuapp.com/api/interests";
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
                                Log.i("jsonobject", jsonobject.getString("name"));
                                listItems.add(jsonobject.getString("name"));
                                checkedItems = new boolean[listItems.size()];

                                final String listArray[] = new String[listItems.size()];

                                interests_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                        builder.setTitle("Please select interests: ");
                                        System.out.println("HERE");
                                        builder.setMultiChoiceItems( listItems.toArray(listArray), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                                if(isChecked){
                                                    if(!userItems.contains(position)){
                                                        System.out.println("Adding " + position);
                                                        userItems.add(position);
                                                    }
                                                } else if(userItems.contains(position)){
                                                    System.out.println("Removing " + position);
                                                    userItems.remove(Integer.valueOf(position));
                                                }
                                            }
                                        });

                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Do nothing for now.
                                            }
                                        });

                                        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                        AlertDialog mDialog = builder.create();
                                        mDialog.show();
                                    }


                                });

                            }
                        }
                        catch(Exception e){

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
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(interestRequest);

        signup_button.setClickable(true);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postUserUrl = "https://playtime-core-api.herokuapp.com/api/users/";
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", name_text.getText().toString());
                params.put("password", password_text.getText().toString());

                JsonObjectRequest req = new JsonObjectRequest(postUserUrl, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Toasty.success(getApplicationContext(), "Successful Account Creation!", Toast.LENGTH_SHORT, true).show();
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                    Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String body = "";
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if(error.networkResponse.data!=null) {
                            try {
                                body = new String(error.networkResponse.data,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            StringBuilder builder = new StringBuilder();
                            final JSONObject obj = new JSONObject(body);
                            JSONArray userErrorInfo = new JSONArray();
                            JSONArray passErrorInfo = new JSONArray();
                            if(obj.has("username")){
                                userErrorInfo = obj.getJSONArray("username");
                                for (int i = 0; i < userErrorInfo.length(); ++i) {
                                    builder.append(userErrorInfo.get(i).toString());
                                    builder.append(" ");
                                }
                                builder.append("\n");
                            }
                            if(obj.has("password")){
                                passErrorInfo = obj.getJSONArray("password");
                                for (int i = 0; i < passErrorInfo.length(); ++i) {
                                    builder.append(passErrorInfo.get(i).toString());
                                    builder.append(" ");
                                }
                                builder.append("\n");
                            }
                            Toasty.error(getApplicationContext(), builder.toString(), Toast.LENGTH_SHORT, true).show();
                        } catch( JSONException e){
                            Toasty.error(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT, true).show();
                        }
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


                Log.i("here", "here");
                for (Integer mem : userItems){
                    Log.i("UserItem num: ", mem.toString());
                }
                Log.i("name: ", name_text.getText().toString());
                Log.i("pass: ", password_text.getText().toString());
                Log.i("email: ", password_text.getText().toString());
            }
        });

    }
}
