package data;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkConnection {

    HttpURLConnection httpc;
    String filename;
    String methodType;

    public NetworkConnection(String filename, String methodType){

        this.filename = filename;
        this.methodType = methodType;

    }

    public void open() {

        try {

            URL url = new URL("http", VALUES.IP, VALUES.HTTPPORT, filename);

            httpc = (HttpURLConnection) url.openConnection();
            httpc.setRequestMethod(methodType);
            httpc.setRequestProperty("Content-Type", "application/json; utf-8");
            httpc.setRequestProperty("Accept", "application/json");
            httpc.setDoOutput(true);
            httpc.setDoInput(true);
            httpc.setUseCaches(false);



        } catch(IOException e) { }  // End of try/catch


    }  // End of open


} // End of Class











