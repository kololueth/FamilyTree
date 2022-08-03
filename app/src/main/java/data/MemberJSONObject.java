package data;

import org.json.JSONException;
import org.json.JSONObject;

import userObjects.Member;

public class MemberJSONObject extends JSONObject {

    private static final String TAG = "MemberJSONObject";


    public MemberJSONObject(){


        try {

            put("user_id", null);
            put("fu_id", null);
            put("first_name", null);
            put("middle_name", null);
            put("last_name", null);
            put("birthdate", null);
            put("sex", null);


        } catch (JSONException e) {

            e.printStackTrace();

        }  // End of try/catch


    } // End of Constructor

    // USE 1: created from the temporary member created in AddMember Class.  All values are validated before creation of this.
    public MemberJSONObject(Member member){


        try {

            put("fu_id", member.fu_id);
            put("first_name", member.firstName);
            put("middle_name", member.middleName);
            put("last_name", member.lastName);
            put("birthdate", member.birthdate);
            put("sex", member.sex);


        } catch (JSONException e) {

            e.printStackTrace();

        }  // End of try/catch


    } // End of Constructor



    public void setFirstName(String value){

        try {
            put("first_name", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setMiddleName(String value){

        try {
            put("middle_name", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setLastName(String value){

        try {
            put("last_name", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setbirthdate(String value){

        try {
            put("birthdate", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setSex(String value){

        try {
            put("sex", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



} // End of Class
