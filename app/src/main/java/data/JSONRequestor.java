package data;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JSONRequestor implements Runnable {

    private static final String TAG = "JSONRequestor";

    private String location = "FamoAgain/Chief";
    private byte[] osBytes;
    private Context context;


    // This constructor sets the class variables.  The JSONObject argument is converted to a byte array
    // for the outputstream of the http connection.


    public JSONRequestor(JSONObject jsonObject, Context context){

        this.context = context;

        Log.d(TAG, "Request JSON: " + jsonObject.toString());

        try {

            this.osBytes = jsonObject.toString().getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) { Log.d(TAG, e.toString()); }

    } // End of Constructor


    /** This method (run()) opens an http connection, sends a JSON String as a byte array, receives the
    // JSON content from the http response, and updates the specified view. */


    @Override
    public void run() {


        try {

            // Write output stream to the server
            NetworkConnection nc = new NetworkConnection(location,"POST");
            nc.open();

                Log.d(TAG, "Network Connection Opened");

            nc.httpc.getOutputStream().write(osBytes, 0, osBytes.length);

                Log.d(TAG, "Writing Output Stream");


            if(nc.httpc.getResponseCode() == 200) {

                    Log.d(TAG, "Good Server Response");

                String bodyContentString = getBodyContent(nc);

                    Log.d(TAG, "Response JSON: " + bodyContentString);

                    if(!bodyContentString.equals("")) { new JSONProcessor(new JSONObject(bodyContentString), context).run(); }

            } else {

                Log.d(TAG, "Connection Issue Http Response Code " + nc.httpc.getResponseCode());


            }// End of if

            nc.httpc.disconnect();


        }  catch (JSONException je) {

            Log.d(TAG, "Catch 1 " + je.toString());


        } catch (IOException e) {

            Log.d(TAG, "Catch 2 " + e.toString());


        } // End of try/catch


    } // End of run



    public String getBodyContent(NetworkConnection nc) {

        String bodyContentString = "";


        try {

            InputStreamReader characters = new InputStreamReader(nc.httpc.getInputStream(), "UTF-8");

            BufferedReader bufferedReader= new BufferedReader(characters);

            String string;

                // The expected response should be a JSON object.  This loops allows for the
                // JSONObject String to spans multiple lines without breaking the application.

                while((string = bufferedReader.readLine()) != null) {

                    bodyContentString = bodyContentString + string;

                } // End of while

            characters.close();
            bufferedReader.close();
            nc.httpc.getInputStream().close();

            Log.d(TAG, "Body Content Retrieved");

        } catch (IOException e) {

            Log.d(TAG, "Body Content Not Retrieved" + e.toString());

        }

       return bodyContentString;


    } // End of getBodyContent


} // End of class
