package com.example.familytree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.Validator;
import lite.sqlite.DbHelper;
import uistuff.AppDialog;
import uistuff.DialogListener;
import uistuff.GridViewAdapter;

import userObjects.ChildSet;
import userObjects.FamilyUnit;
import userObjects.Member;
import userObjects.ParentSet;


public class AddMember extends AppCompatActivity implements View.OnTouchListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener, DialogListener, Observer {

    private static final String TAG = "AddMember";

    // public static AddMemberViewModel addMemberViewModel;

    private FamilyUnit familyUnit;

    private Member member;

    private View activity;

    private GridView parentGridView;
    private GridView childGridView;

    private TextView addMemberTitle;

    float x1 = 0.0f;  //swipe variable
    float x2 = 0.0f;  //swipe variable
    float y1 = 0.0f;  //swipe variable
    float y2 = 0.0f;  //swipe variable

    private int touchId;





    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**** LIFE CYCLE EVENTS ****/


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        /** AddMemberViewModel copies the TreeViewModal familyUnit when instantiated **/

         //   addMemberViewModel = new ViewModelProvider(this).get(AddMemberViewModel.class);

            /** Add observers (event listeners) **/

            //    addMemberViewModel.getResponse().observe(this, this::onChanged);
            //    addMemberViewModel.getStatus().observe(this, this::onChanged);
            //    addMemberViewModel.familyUnit.observe(this, this::onChanged);


        /** Set GUI Components **/

            activity = (View) findViewById(R.id.activity_add_member);
            activity.setOnTouchListener(this);

            addMemberTitle = (TextView) findViewById(R.id.AddMember_Title);

            parentGridView = (GridView) findViewById(R.id.parentGridView);
            childGridView = (GridView) findViewById(R.id.childGridView);

        /** Set Listeners **/

            parentGridView.setOnItemClickListener(this::onItemClick);
            childGridView.setOnItemClickListener(this::onItemClick);

            parentGridView.setOnItemLongClickListener(this::onItemLongClick);
            childGridView.setOnItemLongClickListener(this::onItemLongClick);

        /** Register observers. This is commented out because calling notifyDataSetChanged() causes a refresh of GridView without registering an observer **/

         /*       parentGridView.getAdapter().registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();

                        parentGridView.getAdapter().notify();
                        childGridView.getAdapter().notify();
                    }
                });

                childGridView.getAdapter().registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();

                        parentGridView.getAdapter().notify();
                        childGridView.getAdapter().notify();
                    }

                });
        */



    } // End of onCreate

    @Override
    protected void onStart() {

        super.onStart();

        /** Instantiating an AddMember class family unit for manipulation without lasting affects **/

        if(getIntent().hasExtra("FamilyUnitBytes")) { // From AddMember

            try {

                familyUnit = (FamilyUnit) new ObjectInputStream(new ByteArrayInputStream(getIntent().getByteArrayExtra("FamilyUnitBytes"))).readObject();

            } catch (ClassNotFoundException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

        } // End of if

            addMemberTitle.setText("Add to the " + familyUnit.familyName + " Family");

        /** Set Adapters **/

            parentGridView.setAdapter(new GridViewAdapter(getApplicationContext(), getDisplayParentSet(), getResources()));  // uses getDisplayParentSet because this method will create blank members for the add button
            childGridView.setAdapter(new GridViewAdapter(getApplicationContext(), getDisplayChildSet(), getResources()));  // uses getDisplayChildSet because this method will create blank members for the add button

    } // End of onStart


    /**** LIFE CYCLE EVENTS ****/
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**** EVENTS ****/


    /*************************************************************************************
     // onChanged is called when AddMemberViewModel familyUnit or status changes value.
     // Using class type because I don't know another way!  As of now, AddMemberViewModel
     // contains only one class of type FamilyUnit and String.
     /////////////////////////////////////////////////////////////////////////////////////*/

    /** addMemberViewModel **/
    @Override
    public void onChanged(Object o) {

        /** May not need to use this observer if notifyDataSetChanged() works */



        /** Updates GUI when AddMemberViewModel is updated **/

            if(o.getClass().getSimpleName().equals("FamilyUnit")) { // AddMemberViewModel.familyUnit

              //  ((BaseAdapter) parentGridView.getAdapter()).notifyDataSetChanged();
              //  ((BaseAdapter) childGridView.getAdapter()).notifyDataSetChanged();

            } // End of if


        /** Using status for Error Handling Notifications **/

            if(o.getClass().getSimpleName().equals("String")) { // AddMemberViewModel.status

            /*    if (addMemberViewModel.getStatus().getValue().equals("1")){

                    Toast toast = Toast.makeText(getApplicationContext(), addMemberViewModel.getStatus().getValue(), Toast.LENGTH_LONG);
                    toast.setMargin(50, 50);
                    toast.show();

                }

             */

            } // End of if

        /** Using to Handle response from Server **/

        if(o.getClass().getSimpleName().equals("Integer")) { // AddMemberViewModel.status

            /*

            if (addMemberViewModel.getResponse().getValue() == 1){

                Toast toast = Toast.makeText(getApplicationContext(), "Member Added", Toast.LENGTH_LONG);
                toast.setMargin(50, 50);
                toast.show();

            } else if (addMemberViewModel.getResponse().getValue() == 0){

                Toast toast = Toast.makeText(getApplicationContext(), "Member NOT Added", Toast.LENGTH_LONG);
                toast.setMargin(50, 50);
                toast.show();

            } else if (addMemberViewModel.getResponse().getValue() > 1){

                Toast toast = Toast.makeText(getApplicationContext(), "Member NOT Added unknown error", Toast.LENGTH_LONG);
                toast.setMargin(50, 50);
                toast.show();

            } // End of if/else if

            */

        } // End of if


    } // End of onChanged

    /** this (Activity) **/
    @Override
    public boolean onTouch(View view, MotionEvent event) {

        /* Presses */
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (event.getAction() == MotionEvent.ACTION_DOWN) {  // if PRESSED DOWN

            x1 = event.getX();
            y1 = event.getY();


            if(view == activity) {  // if PRESSES DOWN from anywhere besides parent bubble

                touchId = view.getId();

            }  // End of if not from either parent


        }  // End of PRESSED DOWN




        /* Releases */
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(event.getAction() == MotionEvent.ACTION_UP) { // if RELEASED

            x2 = event.getX();
            y2 = event.getY();


            if (x2 - x1 < -300 && y2 - y1 < 350 && y2 - y1 > -350) { // SWIPE LEFT


                if(touchId == activity.getId()) {


                    Toast toast=Toast.makeText(getApplicationContext(),"No Activity!", Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();


                }  // End if



            }  // End of SWIPE LEFT


            if (x2 - x1 >= 300 && y2 - y1 < 350 && y2 - y1 > -350) { // SWIPE RIGHT


                if(touchId == activity.getId()){

                    Intent intent = new Intent(getApplicationContext(), UnitView.class);

                    intent.putExtra("index", getIntent().getIntExtra("index", 0));

                    startActivity(intent);

                }


            }  // End of SWIPE RIGHT


            if (y2 - y1 < -300) { // SWIPE UP



            }  //End of SWIPE UP


            x1 = 0.0f;
            x2 = 0.0f;
            y1 = 0.0f;
            y2 = 0.0f;

            touchId = 0;


        }  // End of RELEASED


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return true;

    } //End of onTouch





    /** Interface Method for Dialog **/
    @Override
    public void onDialogButtonClick(DialogFragment dialogFragment, int button) {


        if(button == DialogInterface.BUTTON_POSITIVE) {


            switch (dialogFragment.getTag()) {

                case "AddMember.Parent": case "AddMember.Child":

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

                case "AddMember.Parent": case "AddMember.Child":

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

    /** GridView **/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        member = new Member();
        member.fu_id = familyUnit.unitNumber;


        /** For Parent View and Child View **/

            if(parent.getId() == parentGridView.getId() && ((TextView) view.findViewById(R.id.adapter_circle)).getText().equals("ADD")) {

                    member.isParent = true;

                    new AppDialog().show(getSupportFragmentManager(), "AddMember.Parent");

            } else if(parent.getId() == childGridView.getId() && ((TextView) view.findViewById(R.id.adapter_circle)).getText().equals("ADD")) {

                    member.isChild = true;

                    new AppDialog().show(getSupportFragmentManager(), "AddMember.Child");

            } else {

                member = null;

            }


    } // End of onItemClick

    /** GridView **/
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


        /** For Parent View */

            if(parent.getId() == parentGridView.getId()) {

                if (!((TextView) view.findViewById(R.id.adapter_circle)).getText().equals("ADD")) {

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ByteArrayOutputStream bos2 = new ByteArrayOutputStream();

                    ObjectOutputStream oos = null;
                    ObjectOutputStream oos2 = null;

                    try {

                        oos = new ObjectOutputStream(bos);
                        oos.writeObject(familyUnit.parents.get(position));
                        oos.flush();

                        oos2 = new ObjectOutputStream(bos2);
                        oos2.writeObject(familyUnit);
                        oos2.flush();


                        Intent intent = new Intent(getApplicationContext(), MemberCard.class);

                        intent.putExtra("MemberBytes", bos.toByteArray());
                        intent.putExtra("FamilyUnitBytes", bos2.toByteArray());
                        intent.putExtra("index", getIntent().getIntExtra("index", 0));

                        startActivity(intent);

                    } catch (IOException e) { Log.d(TAG, e.toString()); }

                  //  startActivity(new Intent(getApplicationContext(), MemberCard.class).putExtra("ParentIndex", position));

                } // End of if

            } // End of if


        /** For Child View */

            if(parent.getId() == childGridView.getId()) {

                if (!((TextView) view.findViewById(R.id.adapter_circle)).getText().equals("ADD")) {


                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ByteArrayOutputStream bos2 = new ByteArrayOutputStream();

                    ObjectOutputStream oos = null;
                    ObjectOutputStream oos2 = null;

                    try {

                        oos = new ObjectOutputStream(bos);
                        oos.writeObject(familyUnit.children.get(position));
                        oos.flush();

                        oos2 = new ObjectOutputStream(bos2);
                        oos2.writeObject(familyUnit);
                        oos2.flush();

                        Intent intent = new Intent(getApplicationContext(), MemberCard.class);

                        intent.putExtra("FamilyUnitBytes", bos2.toByteArray());
                        intent.putExtra("MemberBytes", bos.toByteArray());
                        intent.putExtra("index", getIntent().getIntExtra("index", 0));

                        startActivity(intent);


                    } catch (IOException e) { Log.d(TAG, e.toString()); }





                } // End of if

            } // End of if

        return false;


    } // End of onItemLongClick



    /** Unused **/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    /** Unused **/
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**** EVENTS ****/
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**** METHODS ****/




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

            case "Brother": case "Father":

                member.sex = "M";
                break;

            case "Sister": case "Mother":

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


        // Add member to internal databas

        DbHelper dbHelper = new DbHelper(getApplication());


        if(member.isParent) {

            dbHelper.addMember(member, 0);

            // Removes blank parents


            if(familyUnit.parents.get(1).firstName == null){

                familyUnit.parents.remove(1);

            } // End of if

            if(familyUnit.parents.get(0).firstName == null){

                familyUnit.parents.remove(0);

            } // End of if



            // Adds new parent

            if(familyUnit.parents.size() < 2) {

                familyUnit.parents.add(new Member(member));

            }

            // Adds new blank parent to present an add button

            if(familyUnit.parents.size() == 1) {

                familyUnit.parents.add(new Member());

            }

        } else if(member.isChild) {

            dbHelper.addMember(member, 1);

            familyUnit.children.remove((familyUnit.children.size() - 1));
            familyUnit.children.add(new Member(member));
            familyUnit.children.add(new Member());

        } else {

            // Alert that something went wrong somehow

        }


        // Send JSONObject to server here

        /*

        try {

            MemberJSONObject memberJSONObject = new MemberJSONObject(member);

            memberJSONObject.put("function", "AddMember");

            new Thread(new JSONRequestor(memberJSONObject, this)).start();


        } catch (JSONException e) { Log.d(TAG, e.toString()); }

         */

        /** TO DO
         //problem with adding a member here.  A problem occurs with the ChildSet trying to set a child number */



        ((BaseAdapter) parentGridView.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter) childGridView.getAdapter()).notifyDataSetChanged();

        member = null;

    }  // End of NewMemberCardPositive

    /*************************************************************************************
     // getDisplayChildSet creates a ChildSet with an additional blank member so that the Adapter (GridViewAdapter)
     // will create an a view with custom text ("ADD").  Views with "ADD" as their text will be handled differently
     // compared to views that do not have "ADD" as their text. ~~ OnItemClick Listener ~~ |  ~~ OnItemLongClick Listener ~~
     /////////////////////////////////////////////////////////////////////////////////////*/

    private ChildSet getDisplayChildSet(){

        familyUnit.children.add(new Member()); // add button

        return familyUnit.children;


    } // End of getDisplayChildSet

    /*************************************************************************************
     // getDisplayParentSet can create a ParentSet with an additional blank members so that the Adapter (GridViewAdapter)
     // will create an a view with custom text ("ADD").  Views with "ADD" as their text will be handled differently
     // compared to views that do not have "ADD" as their text. ~~ OnItemClick Listener ~~ |  ~~ OnItemLongClick Listener ~~
     /////////////////////////////////////////////////////////////////////////////////////*/

    private ParentSet getDisplayParentSet(){


            if(familyUnit.parents.size() < 2){

                if(familyUnit.parents.size() == 1){

                    familyUnit.parents.add(new Member());


                } // End of if

                if(familyUnit.parents.size() == 0){

                    familyUnit.parents.add(new Member());
                    familyUnit.parents.add(new Member());


                }  // End of if


            } // End of if parents < 2

        return familyUnit.parents;

    } // End of getDisplayParentSet



    /**** METHODS ****/
    ////////////////////////////////////////////////////////////////////////////////////////////////



}  // End of AddMember