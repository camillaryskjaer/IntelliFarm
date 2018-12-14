package dk.camr.smartfarm2.Data;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import dk.camr.smartfarm2.Constant;
import dk.camr.smartfarm2.SensorActivityPresenter;
import dk.camr.smartfarm2.Sensors.Sensor;

public class SensorDataAsyncTask extends AsyncTask<String, String, Sensor> {


    SensorActivityPresenter presenter;
    SensorActivityPresenter.View view;
    String param = "";

    public SensorDataAsyncTask(SensorActivityPresenter presenter, SensorActivityPresenter.View view) {
        this.presenter = presenter;
        this.view = view;
    }

    public SensorDataAsyncTask(SensorActivityPresenter presenter, SensorActivityPresenter.View view, String param) {
        this.presenter = presenter;
        this.view = view;
        this.param = param;
    }

    @Override
    protected Sensor doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        Sensor s = null;
        try {

            URL url = new URL(Constant.URL + "sensorreader/" + Constant.SENSOR_ID + param);

            System.err.println(Constant.URL + "sensorreader/" + Constant.SENSOR_ID + param);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder str = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);

            }

            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create();

            s = gson.fromJson(str.toString(), Sensor.class);

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

        view.setSensorInformation(sensor);
        if (sensor.getOperational() != null)
            view.setOperational(sensor.getOperational().isAutomatic());
        view.setChartData(sensor.getLogs(), sensor.getPredictions());
        System.out.println("Done!");
    }
}