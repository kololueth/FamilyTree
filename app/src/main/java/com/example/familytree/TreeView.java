package com.example.familytree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import data.Validator;
import lite.sqlite.DbHelper;
import uistuff.AppDialog;
import uistuff.DialogListener;
import userObjects.Member;

public class TreeView extends AppCompatActivity implements View.OnClickListener, DialogListener {


    private TextView familyUnit;

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tree);

        familyUnit = (TextView) findViewById(R.id.family_unit);

        familyUnit.setOnClickListener(this);


    } // End of onCreate

    @Override
    public void onClick(View v) {

        member = new Member();


        new AppDialog().show(getSupportFragmentManager(), "TreeView.User");



    } // End of onClick

    @Override
    public void onDialogButtonClick(DialogFragment dialogFragment, int button) {


        if(button == DialogInterface.BUTTON_POSITIVE) {


            switch (dialogFragment.getTag()) {

                case "TreeView.User":

                    AddMemberPositive(dialogFragment);

                    break;

                case "DateChooser":

                    DateChooserPositive(dialogFragment);

                    break;

                case "NewMemberCard":

                    NewMemberCardPositive();

                    break;


            } // End of switch



        } // End of DialogInterface.BUTTON_POSITIVE




        if(button == DialogInterface.BUTTON_NEGATIVE) {


            switch (dialogFragment.getTag()) {

                case "TreeView.User":

                    // Reset JSONObject for reuse upon dismissal of dialog

                    member = null;
                    break;

                case "DateChooser":

                    // DateChooser offers an I DON'T KNOW button.  Here we will give a default birthdate of 0000-00-00 to still allow the addition
                    // of a member if the birthdate is unknown

                    member.birthdate = "0000-00-00";

                    new AppDialog(member).show(getSupportFragmentManager(), "NewMemberCard");

                    break;

                case "NewMemberCard":

                    // Notification to the user that the member will not be added to UnitView.

                    Toast toast = Toast.makeText(getApplicationContext(), member.firstName + " not added", Toast.LENGTH_SHORT);
                    toast.setMargin(50, 50);
                    toast.show();

                    member = null;

                    break;


            }  // End of switch


        } // End of DialogInterface.BUTTON_NEGATIVE

    } // End of onDialogButtonClick

    // Positive Dialog Button CLick

    private void AddMemberPositive(DialogFragment dialogFragment) {


        //////////////// GET USER INPUT ///////////////////////

        /**TO DO
         // Try putting a listener on the TextViews and spinners and using an interface to get the the values from the Views. */


        member.firstName = ((EditText) dialogFragment.getDialog().findViewById(R.id.dialog_add_member_firstname)).getText().toString().trim().toUpperCase();
        member.middleName= ((EditText) dialogFragment.getDialog().findViewById(R.id.dialog_add_member_middlename)).getText().toString().trim().toUpperCase();
        member.lastName = ((EditText) dialogFragment.getDialog().findViewById(R.id.dialog_add_member_lastname)).getText().toString().trim().toUpperCase();
        member.sex = ((Spinner) dialogFragment.getDialog().findViewById(R.id.dialog_add_member_relation_spinner)).getSelectedItem().toString();


        //////////////// VALIDATE INPUT ///////////////////////

        Validator validator = new Validator();

        if (!validator.nameCheck("First Name", member.firstName)) {

            member = null;

            Toast toast = Toast.makeText(getApplicationContext(), "Issue with First Name", Toast.LENGTH_LONG);
            toast.setMargin(50, 50);
            toast.show();

            return;

        }

        if (!validator.nameCheck("Middle Name", member.middleName)) {

            member = null;

            Toast toast = Toast.makeText(getApplicationContext(), "Issue with Middle Name", Toast.LENGTH_LONG);
            toast.setMargin(50, 50);
            toast.show();

            return;

        }

        if (!validator.nameCheck("Last Name", member.lastName)) {

            member = null;

            Toast toast = Toast.makeText(getApplicationContext(), "Issue with Last Name", Toast.LENGTH_LONG);
            toast.setMargin(50, 50);
            toast.show();

            return;

        }

        if(member.sex.length() < 1) {

            member = null;

            Toast toast = Toast.makeText(getApplicationContext(), "Please select how this sibling is related", Toast.LENGTH_LONG);
            toast.setMargin(50, 50);
            toast.show();

            return;

        }


        //////////////// TRANSFORM SPINNER INPUT ///////////////////////

        switch (member.sex) {

            case "Male":

                member.sex = "M";
                break;

            case "Female":

                member.sex = "F";
                break;

        } // End of switch



        //////////////// OPEN NEW DIALOG ///////////////////////

        new AppDialog().show(getSupportFragmentManager(), "DateChooser"); // New Dialog to choose birthdate


    } // End of AddMemberPositive


    // Positive Dialog Button CLick
    private void DateChooserPositive(DialogFragment dialogFragment) {


        DatePicker dp = dialogFragment.getDialog().findViewById(R.id.datePicker);

        int year = dp.getYear();
        int month = dp.getMonth();
        int dayOfMonth = dp.getDayOfMonth();
        month++;  // datepicker returns months 0-11, we increase the month for proper format for the rest of the application and room.database

        // Convert to String for further manipulation
        String y = String.valueOf(year);
        String m = String.valueOf(month);
        String d = String.valueOf(dayOfMonth);

        // Adjusts String for room.database date format
        if (month < 10) {
            m = "0" + m;
        }

        // Adjusts String for room.database date format
        if (dayOfMonth < 10) {
            d = "0" + d;
        }

        String birthdate = y + "-" + m + "-" + d; // Proper application/room.database format


        if (new Validator().birthdateCheck(birthdate)) {

            member.birthdate = birthdate;

            new AppDialog(member).show(getSupportFragmentManager(), "NewMemberCard");

        } else {

            member = null;

            Toast toast = Toast.makeText(getApplicationContext(), "There is an issue with the Birthdate", Toast.LENGTH_SHORT);
            toast.setMargin(50, 50);
            toast.show();

            return;

        }

    } // End of DateChooserPositive

    // Positive Dialog Button CLick
    private void NewMemberCardPositive() {

        // Add member to internal database here

        DbHelper dbHelper = new DbHelper(getApplication());

        Home.userid = dbHelper.addUser(member);

        Toast toast=Toast.makeText(getApplicationContext(), member.firstName + " Added!", Toast.LENGTH_LONG);
        toast.setMargin(50,50);
        toast.show();

        member = null;


        Intent intent = new Intent(this, UnitView.class);

        intent.putExtra("login", 0);

        startActivity(intent);


    }  // End of NewMemberCardPositive

} // End of Class