package userObjects;

import android.util.Log;

import com.example.familytree.Home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class ChildSet extends ArrayList<Member> implements Serializable {

    private static final String TAG = "ChildSet";

    @Override
    public boolean add(Member member) { Log.d(TAG, "Adding Child");

        Boolean superResult = super.add(member);

        member.isChild = true;


        if(member.birthdate != null) {

            setChildNumber();

        }

        return superResult;

    } // End of add

    private void setChildNumber(){ Log.d(TAG, "Setting Child Number");

        /** This method determines the child number (1 being the eldest of children) of the newly added child
            and any existing child. If the amount of time (currently daysAlive, but should determined down to seconds or minutes)
            alive of the newly added child is greater than the child compared to, the childNumber of the of the elder is decreased
            by one.
            - Let's Try and use some recursion here instead of iteration. -
         */


        Iterator<Member> it = iterator();


        while(it.hasNext()){

            int childNumber = size();

            Member member = it.next();

                for(int i = 0; i < size(); i++) {

                    if(member.daysAlive > get(i).daysAlive) {

                        childNumber--;

                    } // End of if

                }  // End of for

            member.childNumber = childNumber;

        } // End of while


    } // End of setChildNumber

    public int getNextEldest(int activeIndex){  Log.d(TAG, "Getting Next Eldest");

        for(int i = 0; i < size(); i++){

            if((get(activeIndex).childNumber + 1) == get(i).childNumber){

                return i;

            } // End of if

        }  // End of for

        return  activeIndex;

    } // End of getNextEldest


    public int getNextYoungest(int activeIndex){  Log.d(TAG, "Getting Next Youngest");

        for(int i = 0; i < size(); i++){

            if((get(activeIndex).childNumber - 1) == get(i).childNumber){

                return i;

            } // End of if

        }  // End of for

        return  activeIndex;

    } // End of getNextEldest

    public String getRelation(Member member) {  Log.d(TAG, "Getting Relation");

        if(Home.userid == member.user_id){

            return "Yourself";

        } // End of if

        switch(member.sex){

            case "M" :
                return "Brother";

            case "F" :

                return "Sister";

        } // End of switch

        return "unknown";

    }




} // End of SiblingSet
