package com.example.pranaab.playtime_android_app;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

        //TODO: Loader for getting the values from the api.

        listItems.add("Running");
        listItems.add("Badminton");
        listItems.add("Basketball");
        listItems.add("Guitar");
        listItems.add("Coding");
        listItems.add("Cooking");
        listItems.add("Board Games");
        listItems.add("Online Games");
        listItems.add("Swimming");




        checkedItems = new boolean[listItems.size()];

        final String listArray[] = new String[listItems.size()];


        // Second approach

        interests_button = (Button) findViewById(R.id.btn_interests);

        signup_button = (Button) findViewById(R.id.btn_signup);

        name_text = (EditText) findViewById(R.id.input_name);

        email_text = (EditText) findViewById(R.id.input_email);

        password_text = (EditText) findViewById(R.id.input_password);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(userItems);
                System.out.println(name_text.getText());
                System.out.println(password_text.getText());
                System.out.println(email_text.getText());
            }
        });

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
