package data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import userObjects.FamilyUnit;

public class AddMemberViewModel extends ViewModel {

    public static MutableLiveData<FamilyUnit> familyUnit;

    private MutableLiveData<String> status;

    private MutableLiveData<Integer> response;


    public AddMemberViewModel() {

        // This should be removed when we are able to create a local room.database.  Load the family unit from the local room.database.
        // Right now you're using The UnitViewViewModel as the authentic local room.database

        getFamilyUnit();

    } // End of Constructor


    public MutableLiveData<FamilyUnit> getFamilyUnit() {

        if(familyUnit == null) { familyUnit = new MutableLiveData<FamilyUnit>(); }

        return familyUnit;

    }  // End of getFamilyUnit


    public MutableLiveData<String> getStatus() {

        if(status == null) { status = new MutableLiveData<String>(); } // End of if

        return status;

    }  // End of getStatus


    public MutableLiveData<Integer> getResponse() {

        if(response == null) { response = new MutableLiveData<Integer>(); } // End of if

        return response;

    }  // End of getResponse


} // End of Class
