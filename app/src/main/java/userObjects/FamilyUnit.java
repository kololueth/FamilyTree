package userObjects;

import android.util.Log;

import java.io.Serializable;

public class FamilyUnit implements Serializable {

    private static final String TAG = "FamilyUnit";

    public ParentSet parents = new ParentSet();
    public ChildSet children = new ChildSet();

    public int unitNumber;
    public int childrenNumber;
    public String familyName;

    public  FamilyUnit() {}

    public FamilyUnit(FamilyUnit familyUnit){  Log.d(TAG, "FamilyUnit parameter constructor FamilyUnit");

        this.parents = familyUnit.parents;
        this.children = familyUnit.children;

        this.unitNumber = familyUnit.unitNumber;
        this.childrenNumber = familyUnit.childrenNumber;
        this.familyName = familyUnit.familyName;

    }

    public FamilyUnit(ParentSet parents, ChildSet children) { Log.d(TAG, "ParentSet, ChildSet parameter constructor FamilyUnit");

        this.parents = parents;
        this.children = children;


            if(parents.size() > 0) {

                this.unitNumber = parents.pair_id;

                this.familyName = parents.get(parents.fatherIndex).lastName;

            } else if (children.size() > 0) {

                this.familyName = children.get(0).lastName;

            } // End of if

            if(children.size() > 0) {

                this.childrenNumber = children.size();

            } // End of if


    } // End of Constructor


} // End of Class
