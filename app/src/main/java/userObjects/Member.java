package userObjects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;

public class Member implements Serializable {

    private static final String TAG = "Member";

    public String firstName;
    public String middleName;
    public String lastName;
    public String sex;
    public String birthdate;

    public int daysAlive;
    public int user_id;
    public int parent_id;
    public int fu_id;
    public int age;
    public int childNumber;


    public String heighth;
    public String birthPlace;
    public String tribe;

    public int pair_id;
    public int birthMonth;
    public int birthDay;
    public int birthYear;

    boolean deceased;

    public int local_id;
    public int memberType;
    public boolean isParent;
    public boolean isChild;


    public  Member() {}

    // used for internal database member construction
    public  Member(String firstName, String middleName, String lastName, String birthdate, String sex) {  Log.d(TAG, "Member 5 parameter constructor");

        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.sex = sex;

        this.fu_id = 1;


        if(birthdate != null) {

            setAge();
            setDaysAlive();

        } // End of if

    }  // End of Constructor

    // used for external database member construction
    public Member(JSONObject jsonObject) { Log.d(TAG, "Member jsonObject parameter constructor");

        try {

            if(jsonObject.has("user_id")){this.user_id = jsonObject.getInt("user_id");}
            if(jsonObject.has("parent_id")){this.parent_id = jsonObject.getInt("parent_id");}
            if(jsonObject.has("fu_id")){this.fu_id = jsonObject.getInt("fu_id");}

            if(jsonObject.has("first_name")){this.firstName = jsonObject.getString("first_name");}
            if(jsonObject.has("last_name")){this.lastName = jsonObject.getString("last_name");}
            if(jsonObject.has("birthdate")){this.birthdate = jsonObject.getString("birthdate");}
            if(jsonObject.has("sex")){this.sex = jsonObject.getString("sex");}


        } catch (JSONException e) {

            e.printStackTrace();

        }

        if(birthdate != null) {

            setAge();
            setDaysAlive();

        } // End of if



    } // End of Constructor

    // USE 1: created from the temporary member created in AddMember Class.  All values are validated before creation of this.
    public Member(Member member){ Log.d(TAG, "Member member constructor");

        this.fu_id = member.fu_id;
        this.firstName = member.firstName;
        this.middleName = member.middleName;
        this.lastName = member.lastName;
        this.birthdate = member.birthdate;;
        this.sex = member.sex;


    } // End of Constructor


    private void setAge() { Log.d(TAG, "Setting Age");

            age = Period.between(LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now()).getYears();

    } // End of setAges

    private void setDaysAlive() {  Log.d(TAG, "Setting Days Alive");

        daysAlive = (Period.between(LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now()).getYears() * 365) + (Period.between(LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now()).getMonths() * 30) + (Period.between(LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now()).getDays());


            int years = Period.between(LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now()).getYears();
            int months = Period.between(LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now()).getMonths();
            int days = Period.between(LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE), LocalDate.now()).getDays();


    } // End of setDaysAlive


} // End of Member Class
