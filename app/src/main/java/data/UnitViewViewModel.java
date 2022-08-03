package data;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.familytree.Home;

import org.json.JSONException;
import org.json.JSONObject;

import lite.sqlite.DbHelper;
import userObjects.FamilyUnit;

public class UnitViewViewModel extends AndroidViewModel {

    private static final String TAG = "UnitViewViewModel";

    private MutableLiveData<Integer> index;

    public  MutableLiveData<FamilyUnit> familyUnit;

    private MutableLiveData<String> status;

    private DbHelper dbhelper;

    public UnitViewViewModel(@NonNull Application application) {

        super(application);

        getFamilyUnit();

    } // End of Constructor


    public MutableLiveData<Integer> getIndex() {

        if(index == null) {

            index = new MutableLiveData<Integer>();

        } // End of if

        return index;

    }  // End of getIndex


    public MutableLiveData<String> getStatus() {

        if(status == null) {

            status = new MutableLiveData<String>();

        } // End of if

        return status;

    }  // End of getStatus


    private MutableLiveData<FamilyUnit> getFamilyUnit() {

        if(familyUnit == null) {

            familyUnit = new MutableLiveData<FamilyUnit>();

            loadFamilyUnitInternal();

        } // End of if

        return familyUnit;

    }  // End of getFamilyUnit


    public void loadFamilyUnitInternal(){

        Log.d(TAG, "Loading Internal FamilyUnit");

        dbhelper = new DbHelper(getApplication());

        familyUnit.setValue(new FamilyUnit(dbhelper.getMembers(1)));

    } // End of loadFamilyUnitLocal


    public void loadFamilyUnitExternal(){

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("user_id", Home.userid);
            jsonObject.put("function", "FamilyUnit");

            new Thread(new JSONRequestor(jsonObject, getApplication())).start();

        } catch (JSONException e) { Log.d(TAG, e.toString());} // End of try/catch


    } // End of loadFamilyUnitExternal




} // End of Active
