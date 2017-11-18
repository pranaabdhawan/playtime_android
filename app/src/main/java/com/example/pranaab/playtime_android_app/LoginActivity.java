package com.example.pranaab.playtime_android_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private Context _context;

    //private RequestQueue requestQueue;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);
        _context = getApplicationContext();

        //Set login on click listener
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clicked login", "blah");
                //final String basicAuthHeader = "Basic " + new String(Base64.encode("jiaqi:super_secret_123".getBytes(), Base64.DEFAULT));
                String loginstring = _emailText.getText().toString() + ":" + _passwordText.getText().toString();
                Log.i("login str", loginstring);
                final String basicAuthHeader = "Basic " + new String(Base64.encode(loginstring.getBytes(), Base64.DEFAULT));

                String loginUrl = "https://playtime-core-api.herokuapp.com/api/auth/login/";
                final StringRequest loginRequest = new StringRequest(Request.Method.POST, loginUrl,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    token = jsonObject.getString("token");
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("currUser", token);
                                    editor.commit();
                                    //String retrievedToken = pref.getString("currUser", null);

                                }
                                catch(Exception e){

                                }
                                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                //goes here when the login fails
                                Toasty.error(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT, true).show();
                                Log.d("ERROR","error => "+error.toString());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<>();
                        params.put("Authorization", basicAuthHeader);
                        return params;
                    }
                };
                RequestQueueSingleton.getInstance(_context).addToRequestQueue(loginRequest);

            }
        });

        //Set signup on click listener
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start signup activity
                Intent intent =  new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }


}
