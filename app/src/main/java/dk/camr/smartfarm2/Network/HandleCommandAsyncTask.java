package dk.camr.smartfarm2.Network;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

import dk.camr.smartfarm2.Sensors.Sensor;

public class HandleCommandAsyncTask extends AsyncTask<String, String, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {


        HttpURLConnection httpURLConnection = null;
       System.err.println("HANDLE");
        try {

            URL url = new URL("http://10.0.0.207:45455/api/sensoractivation/12");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            int statusCode = http.getResponseCode();

            System.err.println("RESP " +statusCode);

            if (statusCode == 200) {
                //Alt ok - vi afbryder
                return true;
            }

        } catch (Exception e) {
e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }

        return false;
    }

    protected void onPreExecute() {
        //Setup precondition to execute some task
    }


    protected void onPostExecute(boolean ok) {
        System.out.println("Done!" +ok);
    }

}
