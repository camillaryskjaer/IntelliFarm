package dk.camr.smartfarm2.Data;

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

import dk.camr.smartfarm2.SensorActivityPresenter;
import dk.camr.smartfarm2.Sensors.Sensor;

public class SensorAutomaticOverrideAsyncTask extends AsyncTask<String, String, Sensor> {


    SensorActivityPresenter presenter;
    SensorActivityPresenter.View view;

    public SensorAutomaticOverrideAsyncTask(SensorActivityPresenter presenter, SensorActivityPresenter.View view) {
        this.presenter = presenter;
        this.view = view;
    }

    @Override
    protected Sensor doInBackground(String... params) {
        // RequestQueue queue = Volley.newRequestQueue(view);
        HttpURLConnection httpURLConnection = null;
        Sensor s = null;
        try {

            URL url = new URL("http://192.168.1.206:45455/api/action/13");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder str = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);

            }

            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create();
            //Gson gson = new Gson();
            //        GsonBuilder gsonBuilder = GsonBuilder.registerTypeAdapter(Date.class, null).create();

            //   s = gson.fromJson(str.toString(), Sensor.class);

            in.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            httpURLConnection.disconnect();
        }

        return s;
    }

    protected void onPreExecute() {
        //Setup precondition to execute some task
    }


    @Override
    protected void onPostExecute(Sensor sensor) {
        System.out.println("new Done!");
    }
}