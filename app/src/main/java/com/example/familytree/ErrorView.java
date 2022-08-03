package com.example.familytree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ErrorView extends AppCompatActivity {



    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_view);



        textView = (TextView) findViewById(R.id.errorTextView);

    }



    @Override
    protected void onStart() {

        super.onStart();

        if(getIntent().hasExtra("error")) {

            String error = getIntent().getStringExtra("error");

            textView.setText(error);

        }

    } // End of onStart



}


