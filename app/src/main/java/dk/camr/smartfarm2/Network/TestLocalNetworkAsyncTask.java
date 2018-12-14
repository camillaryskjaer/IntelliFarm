package dk.camr.smartfarm2.Network;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import dk.camr.smartfarm2.Data.DateDeserializer;
import dk.camr.smartfarm2.SensorActivityPresenter;
import dk.camr.smartfarm2.Sensors.Sensor;

public class TestLocalNetworkAsyncTask extends AsyncTask<String, String, Boolean> {


    /*  public SensorDataAsyncTask(SensorActivityPresenter presenter, SensorActivityPresenter.View view) {
          this.presenter = presenter;
          this.view = view;
      }

      public SensorDataAsyncTask(SensorActivityPresenter presenter, SensorActivityPresenter.View view, String param) {
          this.presenter = presenter;
          this.view = view;
          this.param = param;
      }
  */
    @Override
    protected Boolean doInBackground(String... params) {

        // Vi skal forsøge at teste på om vi kan koble op på det interne netværk
        //vi skal derfor have status kode 200 fra SU
        //kun 10 gange

        HttpURLConnection httpURLConnection = null;
        Sensor s = null;
        try {

            URL url = new URL("http://192.168.1.1");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            int statusCode = http.getResponseCode();
            if (statusCode == 200) {
                //Alt ok - vi afbryder
                return true;
            }

            System.err.println("CODE" + statusCode);
        } catch (Exception e) {
            //
            System.err.println("NO CONNECT");
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

        if (ok) {
            //så er vi koblet på

        }
        System.out.println("Done!");
    }
}
