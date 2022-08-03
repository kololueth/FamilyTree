package userObjects;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import userObjects.Member;

public class ParentSet extends ArrayList<Member> implements Serializable {

    private static final String TAG = "ParentSet";

    public String fatherName;
    public String motherName;

    public int fatherIndex;
    public int motherIndex;
    public int pair_id;

    @Override
    public boolean add(Member member) { Log.d(TAG, "Adding Parent");

        Boolean superResult = super.add(member);

        this.pair_id = member.fu_id;

        member.isParent = true;

        setParent();

        return superResult;

    } // End of add



    private void setParent() { Log.d(TAG, "Setting Parent");


        for(int i = 0; i < size(); i++) {

            if(get(i).sex != null) {


                if(get(i).sex.equals("F")){

                    motherIndex = i;

                    if(get(i).firstName != null){

                        motherName = get(i).firstName;

                    } // End of if

                } else if(get(i).sex.equals("M")){

                    fatherIndex = i;

                    if(get(i).firstName != null){

                        fatherName = get(i).firstName;

                    } // End of if

                } // End of if


            } // End of if

        } // End of for


    }  // End of setParent



} // End of ParentSet
