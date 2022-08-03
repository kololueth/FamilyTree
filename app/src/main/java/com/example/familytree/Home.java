package com.example.familytree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import lite.sqlite.DbHelper;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Home";


    public static long userid;

    Button loginB;

    EditText email;
    EditText pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        loginB = (Button) findViewById(R.id.button);
         loginB.setOnClickListener(this);

         email = (EditText) findViewById(R.id.editTextTextEmailAddress);
         pass = (EditText) findViewById(R.id.editTextTextPassword);


    } // End of OnCreate

    @Override
    protected void onStart() {

        super.onStart();


        if(checkDatabase()) {  Log.d(TAG, "app database EXISTS");

            Intent intent = new Intent(this, UnitView.class);

            intent.putExtra("login", 0);


            // Create Static Home Member here

            startActivity(intent);

        } else {  Log.d(TAG, "app database DOES NOT EXIST");

            Intent intent = new Intent(this, TreeView.class);

            startActivity(intent);

        } // End of if


    } // End of onStart

    public boolean checkDatabase(){  Log.d(TAG, "Checking for app database...");

        SQLiteDatabase db = null;


        try{

            db = SQLiteDatabase.openDatabase("/data/data/"+ getApplication().getPackageName() + "/databases/" + DbHelper.DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);

            Cursor cursorCourses = db.rawQuery("SELECT _id FROM user", null);

            if(cursorCourses != null) {

                cursorCourses.moveToFirst();

                for(int i = 0; i < cursorCourses.getCount(); i++){

                    Log.d(TAG, "getting userid!");

                    userid = cursorCourses.getInt(0);

                    cursorCourses.moveToNext();

                } // End of for

            } // End of if


            cursorCourses.close();
            db.close();

        } catch (SQLiteException e) {

            return false;

        }

        return true;

    } // End of checkDatabase


    public void onClick (View v) {


/*
        Validator validator = new Validator();

        if(validator.emailCheck(email.getText().toString())){  // if validate method returns true, the text is valid

            Intent intent = new Intent(this, UnitView.class);

            startActivity(intent);

        } else {

            Toast toast=Toast.makeText(getApplicationContext(),"email is wrong", Toast.LENGTH_SHORT);
            toast.setMargin(50,50);
          //  toast.show();

        }  // End of if

        if(validator.passwordCheck(pass.getText().toString())){  // if validate method returns true, the text is valid

            Intent intent = new Intent(this, UnitView.class);

            startActivity(intent);

        } else {

            Toast toast=Toast.makeText(getApplicationContext(),"pass is wrong", Toast.LENGTH_SHORT);
            toast.setMargin(50,50);
            toast.show();

        }  // End of if


 */

    } // End of onClick

} // End of class