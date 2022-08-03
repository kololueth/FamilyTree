package com.example.familytree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;

import lite.sqlite.DbHelper;
import uistuff.DialogListener;
import userObjects.FamilyUnit;
import userObjects.Member;

public class MemberCard extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, DialogListener {

    private static final String TAG = "MemberCard";

    private View activity;

    TextView fullName;
    TextView birthdate;
    TextView middleName;
    TextView dayOfWeek;
    TextView childNumber;

    Button delete;

    private float x1 = 0.0f;  //swipe variable
    private float x2 = 0.0f;  //swipe variable
    private float y1 = 0.0f;  //swipe variable
    private float y2 = 0.0f;  //swipe variable

    private int touchId;
    private int index;

    Member member;
    FamilyUnit familyUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_card);

        activity = (View) findViewById(R.id.activity_member_card);

        fullName = (TextView) findViewById(R.id.memberCard_fullname);
        birthdate = (TextView) findViewById(R.id.memberCard_birthdate);
        middleName = (TextView) findViewById(R.id.memberCard_middlename);
        dayOfWeek = (TextView) findViewById(R.id.memberCard_dayofweek);
        childNumber = (TextView) findViewById(R.id.memberCard_childnumber);

        delete = (Button) findViewById(R.id.memberCard_delete_button);
        delete.setOnClickListener(this::onClick);


        activity.setOnTouchListener(this);
        birthdate.setOnTouchListener(this);
        fullName.setOnTouchListener(this);


        if(getIntent().hasExtra("MemberBytes") && getIntent().hasExtra("FamilyUnitBytes") && getIntent().hasExtra("index")) { // From AddMember

            try {

                familyUnit = (FamilyUnit) new ObjectInputStream(new ByteArrayInputStream(getIntent().getByteArrayExtra("FamilyUnitBytes"))).readObject();

                member = (Member) new ObjectInputStream(new ByteArrayInputStream(getIntent().getByteArrayExtra("MemberBytes"))).readObject();

                index = getIntent().getIntExtra("index", 0);

            } catch (ClassNotFoundException e) {

                Log.d(TAG, e.toString());

            } catch (IOException e) {

                Log.d(TAG, e.toString());

            } // End of try/catch

        } // End of if


    } // End of onCreate

    @Override
    protected void onStart() {

        super.onStart();

        fillCard();

    } // End of onStart


    private void fillCard() {

        fullName.setText(member.firstName + " " + member.lastName);

        if(member.birthdate != null) {

            LocalDate date = LocalDate.parse(member.birthdate);

            String bd = date.getMonth().toString() + " " + String.valueOf(date.getDayOfMonth()) + ", " + String.valueOf(date.getYear());

            birthdate.setText(bd);
            dayOfWeek.setText(date.getDayOfWeek().toString());


        } else  {

            birthdate.setText("unknown");
            dayOfWeek.setText("unknown");

            dayOfWeek.setTextColor(Color.parseColor("#FFFF00"));
            birthdate.setTextColor(Color.parseColor("#FFFF00"));


        }

        if(member.middleName != null) {

            middleName.setText(member.middleName);

        } else{

            middleName.setText("unknown");
            middleName.setTextColor(Color.parseColor("#FFFF00"));

        }

        if(member.childNumber == 0){

            childNumber.setText("unknown");
            childNumber.setTextColor(Color.parseColor("#FFFF00"));

        } else {

            childNumber.setText(String.valueOf(member.childNumber));

        }


    } // End of fillCard


    @Override
    public boolean onTouch(View view, MotionEvent event) {


        /* Presses */
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        if (event.getAction() == MotionEvent.ACTION_DOWN) {  // if PRESSED DOWN

            x1 = event.getX();
            y1 = event.getY();

            setTouchId(view);



        }  // End of PRESSED DOWN


        /* Releases */
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(event.getAction() == MotionEvent.ACTION_UP) { // if RELEASED


            x2 = event.getX();
            y2 = event.getY();

            /*************** SWIPE LEFT   ***************/


            if (x2 - x1 < -300 && y2 - y1 < 350 && y2 - y1 > -350) {


                if(touchId == activity.getId()) {

                    Toast toast=Toast.makeText(getApplicationContext(),"Nope!", Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();

                } // End of if


            }  // End of SWIPE LEFT


            /*************** SWIPE RIGHT   ***************/


            if (x2 - x1 >= 300 && y2 - y1 < 350 && y2 - y1 > -350) {


                if(touchId == activity.getId()) {

                    Intent intent = new Intent(getApplicationContext(), UnitView.class);


                    if(getIntent().hasExtra("index")) {

                        intent.putExtra("index", index);

                    }


                    startActivity(intent);

                } // End of if activity


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


    private void setTouchId(View view) {

        ViewGroup rootView = (ViewGroup) this.findViewById(R.id.activity_member_card);

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


    @Override
    public void onDialogButtonClick(DialogFragment dialogFragment, int which) {

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == delete.getId()) {

            int result;

            DbHelper dbHelper = new DbHelper(getApplication());

            result = dbHelper.removeMember(member);

                if(result > 0){

                    Toast toast = Toast.makeText(getApplicationContext(), member.firstName + " deleted!", Toast.LENGTH_LONG);
                    toast.setMargin(50, 50);
                    toast.show();


                    Intent intent = new Intent(getApplicationContext(), UnitView.class);

                        if(member.isParent){

                            intent.putExtra("index", index);

                        } else if (member.isChild){

                            intent.putExtra("index",  familyUnit.children.getNextYoungest(index));

                        } // End of if

                    startActivity(intent);

                } else if (result == 0) {

                    Toast toast = Toast.makeText(getApplicationContext(), "There is an issue deleting " + member.firstName, Toast.LENGTH_LONG);
                    toast.setMargin(50, 50);
                    toast.show();

                } // End of if


        } // End of if delete button


    } // End of onCLick

}  // End of Class