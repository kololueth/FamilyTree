package data;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Validator {


    private final String namePattern = "^[A-Za-z!]{1,50}$";
    private final String birthdatePattern = "^\\d{4}-(0[1-9]|1[0|1|2])-([0|1|2|3][0-9])$"; // This is supposed to be YYYY-MM-DD format :)!
    private final String emailPattern = "^[\\w]@[\\w]/.[\\w]$";
    private final String passwordPattern = "^[\\w]{4,20}$";


    public ArrayList<String> errors = new ArrayList<String>();



    public boolean nameCheck(String label, String name) {

        if(Pattern.matches(namePattern, name)){

            return  true;

        } else {

            errors.add(label);

            return false;

        }


    } // End of editTextCheck


    public boolean nameCheck(String name) {

        return Pattern.matches(namePattern , name);

    }  // End of nameCheck


    public boolean birthdateCheck(String birthdate) {

        if(Pattern.matches(birthdatePattern , birthdate)){

            return true;

        } else {

            errors.add("birthdate (" + birthdate + ") has an issue.");

            return false;

        }

    } // End of birthdateCheck


    public boolean emailCheck(String email) {

        return Pattern.matches(emailPattern , email);

    } // End of emailCheck


    public boolean passwordCheck(String pass) {

        return Pattern.matches(passwordPattern , pass);

    } // End of passwordCheck




} // End of Class
