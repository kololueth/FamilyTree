package com.example.familytree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import data.UnitViewViewModel;

import userObjects.Member;


public class UnitView extends AppCompatActivity implements View.OnTouchListener, Observer {


    private static final String TAG = "UnitView";

    public UnitViewViewModel unitViewViewModel;

    private Timer timer;

    private View activity;

    public static TextView circle;

    public TextView maternal;
    public TextView paternal;
    public TextView name;
    public TextView age;
    public TextView relation;
    public TextView mother_label;
    public TextView father_label;
    public TextView infoDisplay;
    public TextView familyname;

    private float x1 = 0.0f;  //swipe variable
    private float x2 = 0.0f;  //swipe variable
    private float y1 = 0.0f;  //swipe variable
    private float y2 = 0.0f;  //swipe variable

    private int touchId;




    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**** LIFE CYCLE EVENTS ****/


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_view);

        //* The instantiation of the UnitViewViewModel calls getFamilyUnit() which eventually loads the family unit from the server room.database.

            unitViewViewModel = new ViewModelProvider(this).get(UnitViewViewModel.class);

            //- Add observers (event listeners)

                unitViewViewModel.getIndex().observe(this, this::onChanged);

                unitViewViewModel.getStatus().observe(this, this::onChanged);

                unitViewViewModel.familyUnit.observe(this, this::onChanged);

        //* Set GUI Components

            activity = (View) findViewById(R.id.activity_unit_view);
            circle = (TextView) findViewById(R.id.circle);
            maternal = (TextView) findViewById(R.id.maternal);
            paternal = (TextView) findViewById(R.id.paternal);
            name = (TextView) findViewById(R.id.name_tree);
            age = (TextView) findViewById(R.id.age);
            mother_label = (TextView) findViewById(R.id.mother_name);
            father_label = (TextView) findViewById(R.id.father_name);
            relation = (TextView) findViewById(R.id.tree_relation);
            infoDisplay = (TextView) findViewById(R.id.infoDisplay);
            familyname = (TextView) findViewById(R.id.tree_familyname);

        //* Set Event Listeners

            activity.setOnTouchListener(this);
            circle.setOnTouchListener(this);
            maternal.setOnTouchListener(this);
            paternal.setOnTouchListener(this);
            name.setOnTouchListener(this);


    } // End of onCreate

    @Override
    protected void onStart() {

        super.onStart();

        Log.d(TAG, "On Start");

            if(getIntent().hasExtra("index")) {

                Log.d(TAG, "In Intent index");

                unitViewViewModel.getIndex().setValue(getIntent().getIntExtra("index", 0));

            } // End of getIntent

    } // End of onStart


    /**** LIFE CYCLE EVENTS ****/
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**** EVENTS ****/


    /*************************************************************************************
    // onChanged is called when UnitViewViewModel index, familyUnit, or status changes value.
    // Using class type because I don't know another way!  As of now, UnitViewViewModel
    // contains only one class of type Integer, String, and FamilyUnit.
    /////////////////////////////////////////////////////////////////////////////////////*/

    @Override
    public void onChanged(Object o) {


        // Updates GUI

            if(o.getClass().getSimpleName().equals("Integer")) { // UnitViewViewModel.index

                Log.d(TAG, "Integer Changed");

                Member member = (unitViewViewModel.familyUnit.getValue()).children.get(unitViewViewModel.getIndex().getValue());

                // Update Circle

                    name.setText(member.firstName);
                    age.setText(String.valueOf(member.age));
                    relation.setText((unitViewViewModel.familyUnit.getValue()).children.getRelation(member));

                // Update Parents

                    mother_label.setText((unitViewViewModel.familyUnit.getValue()).parents.motherName);
                    father_label.setText((unitViewViewModel.familyUnit.getValue()).parents.fatherName);

                // Update Family Name

                    familyname.setText((unitViewViewModel.familyUnit.getValue()).familyName);

                // Update Info Display

                    infoDisplay.setText("index: " + unitViewViewModel.getIndex().getValue().toString() + "  | familyunit: " + (unitViewViewModel.familyUnit.getValue()).unitNumber + "  | parents: " + (unitViewViewModel.familyUnit.getValue()).parents.size() + "  | children: " + (unitViewViewModel.familyUnit.getValue()).childrenNumber);

            } // End of if Integer


        // This statement (if class is FamilyUnit and intent has "login") should only be called once upon the start of the FamilyTree app.
        // The statement searches for the user in the family tree and sets the UnitViewViewModel's index as the array index number of the user.
        // UnitViewViewModel.index is observed and will call the (if Integer statement) in this method to display the member and parents.

            if(o.getClass().getSimpleName().equals("FamilyUnit") && getIntent().hasExtra("login")) { // UnitViewViewModel.familyUnit

                Log.d(TAG, "FamilyUnit Changed");

                for(int i = 0; i < unitViewViewModel.familyUnit.getValue().children.size(); i++) {

                    if(unitViewViewModel.familyUnit.getValue().children.get(i).local_id == Home.userid) {

                            unitViewViewModel.getIndex().postValue(i);

                            break;

                    } // End of if

                } // End of for

            } // End of FamilyUnit


        // Using status for Error Handling Notifications

            if(o.getClass().getSimpleName().equals("String")) { // UnitViewViewModel.status

                Toast toast=Toast.makeText(getApplicationContext(), unitViewViewModel.getStatus().getValue(), Toast.LENGTH_LONG);
                toast.setMargin(50,50);
                toast.show();

            } // End of if String


    } // End of onChanged


    public boolean onTouch(View view, MotionEvent event) {


        /* Presses */
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        if (event.getAction() == MotionEvent.ACTION_DOWN) {  // if PRESSED DOWN

            x1 = event.getX();
            y1 = event.getY();

            setTouchId(view);

            timer = new Timer("TimerTaskThread", true);

            if(touchId == circle.getId()) {


                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), MemberCard.class);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();

                        ObjectOutputStream oos = null;
                        ObjectOutputStream oos2 = null;

                            try {

                                oos = new ObjectOutputStream(bos);
                                oos.writeObject(unitViewViewModel.familyUnit.getValue().children.get(unitViewViewModel.getIndex().getValue()));
                                oos.flush();

                                intent.putExtra("MemberBytes", bos.toByteArray());

                                oos2 = new ObjectOutputStream(bos2);
                                oos2.writeObject(unitViewViewModel.familyUnit.getValue());
                                oos2.flush();

                                intent.putExtra("FamilyUnitBytes", bos2.toByteArray());


                            } catch (IOException e) { Log.d(TAG, e.toString()); }


                        intent.putExtra("index", unitViewViewModel.getIndex().getValue());

                        startActivity(intent);

                    } // End of run

                        }, 650);

            } // End of if

        }  // End of PRESSED DOWN


        /* Releases */
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(event.getAction() == MotionEvent.ACTION_UP) { // if RELEASED

            timer.cancel();
            timer.purge();

            x2 = event.getX();
            y2 = event.getY();

            /*************** SWIPE LEFT   ***************/


            if (x2 - x1 < -300 && y2 - y1 < 350 && y2 - y1 > -350) {


                if(touchId == activity.getId()) {

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = null;

                    try {

                        oos = new ObjectOutputStream(bos);
                        oos.writeObject(unitViewViewModel.familyUnit.getValue());
                        oos.flush();


                        Intent intent = new Intent(getApplicationContext(), AddMember.class);

                        intent.putExtra("FamilyUnitBytes", bos.toByteArray());
                        intent.putExtra("index", unitViewViewModel.getIndex().getValue());

                        startActivity(intent);

                    } catch (IOException e) { Log.d(TAG, e.toString()); }


                }  // End of if Activity

                if(touchId == circle.getId()) {

                    unitViewViewModel.getIndex().setValue(unitViewViewModel.familyUnit.getValue().children.getNextYoungest(unitViewViewModel.getIndex().getValue()));

                }

                if(touchId == maternal.getId()) {



                }

                if(touchId == paternal.getId()) {



                }


            }  // End of SWIPE LEFT


            /*************** SWIPE RIGHT   ***************/


            if (x2 - x1 >= 300 && y2 - y1 < 350 && y2 - y1 > -350) {


                if(touchId == activity.getId()) {

                    Intent intent = new Intent(this, TreeView.class);

                    startActivity(intent);
                }


                if(touchId == circle.getId()) {

                    unitViewViewModel.getIndex().setValue(unitViewViewModel.familyUnit.getValue().children.getNextEldest(unitViewViewModel.getIndex().getValue()));

                } // End of circle


                if(touchId == maternal.getId()) {



                }

                if(touchId == paternal.getId()) {



                }


            }  // End of SWIPE RIGHT


            /*************** SWIPE UP   ***************/


            if (y2 - y1 < -300) {



            }  //End of SWIPE UP


            /*************** SWIPE DOWN   ***************/


            if (y2 - y1 < 300) {



            }  //End of SWIPE DOWN

            /*************** Clicks   ***************/


            x1 = 0.0f;
            x2 = 0.0f;
            y1 = 0.0f;
            y2 = 0.0f;

            touchId = 0;


        }  // End of RELEASED


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        return true;


    } // End of onTouch


    /**** EVENTS ****/
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**** METHODS ****/


    private void setTouchId(View view) {

        ViewGroup rootView = (ViewGroup) this.findViewById(R.id.activity_unit_view);

        for(int i = 0; i < rootView.getChildCount(); i++) {

            if(view.getId() == rootView.getId()) {

                touchId = rootView.getId();

                break;

            } else if(view.getId() == rootView.getChildAt(i).getId()) {

                touchId = rootView.getChildAt(i).getId();

                break;

            }// End of if

        } // End of for


    } // End of setTouchID


    /**** METHODS ****/
    ////////////////////////////////////////////////////////////////////////////////////////////////

} // End of UnitView Class