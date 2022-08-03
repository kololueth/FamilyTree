package data;



import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import lite.sqlite.DbHelper;
import userObjects.ChildSet;
import userObjects.Member;
import userObjects.ParentSet;

public class JSONProcessor implements Runnable {

    /**  This class does not necessarily need to be a Runnable.  Evaluate the necessity of
     *   this class as a Runnable one day*/

    private static final String TAG = "JSONProcessor";

    JSONObject jsonObject;

    Context context;

    public JSONProcessor(JSONObject jsonObject, Context context){

        this.context = context;

        this.jsonObject = jsonObject;

        Log.d(TAG, jsonObject.toString());

    } // End of Constructor

    @Override
    public void run() {

        try {

                switch(jsonObject.getString("function")) {

                    case "FamilyUnit":

                        createFamilyUnit();
                        break;

                    case "AddMember":

                        addMember();
                        break;

                    case "Delete":

                        deleteMember();
                        break;

                } // End of Switch


        } catch (JSONException e) {

            Log.d(TAG, "JSONProcessor run - " + e.toString());

        } // End of try/catch

    } // End of run

    public void createFamilyUnit(){

        DbHelper dbHelper = new DbHelper(context);

        ParentSet parents = new ParentSet();
        ChildSet children = new ChildSet();

             try {

                    for(int i = 0; i < jsonObject.getJSONArray("parents").length(); i++) {

                        parents.add(new Member(jsonObject.getJSONArray("parents").getJSONObject(i)));

                    } // End of for


                    for(int i = 0; i < jsonObject.getJSONArray("siblings").length(); i++) {

                        children.add(new Member(jsonObject.getJSONArray("siblings").getJSONObject(i)));

                    } // End of for


             } catch (JSONException e) {

                 Log.d(TAG, e.toString());

             }


    } // End of fillFamilyUnit

    public void addMember() {


        if(jsonObject.has("error")){

            try {

                Log.d(TAG, jsonObject.getString("error"));

            } catch (JSONException e) {

                Log.d(TAG, e.toString());

            } // End of try


        } else {

            /** Can one build an interface here to notify the AddMember Activity if it is not destroyed. Try Using onAttach */


            try {


                if (jsonObject.getInt("result") == 1) { // 1 = success

               //     AddMember.addMemberViewModel.getResponse().postValue(jsonObject.getInt("result"));

                }

                if (jsonObject.getInt("result") == 0) { // 0 = failure

             //       AddMember.addMemberViewModel.getResponse().postValue(jsonObject.getInt("result"));

                }


                if (jsonObject.getInt("result") > 1) { // 2 = unkown issue

               //     AddMember.addMemberViewModel.getResponse().postValue(jsonObject.getInt("result"));

                }

            } catch (JSONException e) {

                Log.d(TAG, e.toString());

            }

        } // End of if / else


    } // End of addMember

    public void deleteMember() {

        Log.d(TAG, "Deleted Member");

    } // End of deleteMember



} // End of Class
