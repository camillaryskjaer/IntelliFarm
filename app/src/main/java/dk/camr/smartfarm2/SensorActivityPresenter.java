package dk.camr.smartfarm2;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.view.View;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dk.camr.smartfarm2.Data.OnTaskCompleted;
import dk.camr.smartfarm2.Data.SensorDataAsyncTask;
import dk.camr.smartfarm2.Network.HandleCommandAsyncTask;
import dk.camr.smartfarm2.Network.WirelessScanAsyncTask;
import dk.camr.smartfarm2.Sensors.Log;
import dk.camr.smartfarm2.Sensors.Prediction;
import dk.camr.smartfarm2.Sensors.Sensor;
import dk.camr.smartfarm2.view.WifiActivityPresenter;

public class SensorActivityPresenter implements OnTaskCompleted {

    View view;


    public void update(String param){
        SensorDataAsyncTask performBackgroundTask = new SensorDataAsyncTask(this, view, param);
        performBackgroundTask.execute();

    }



    public SensorActivityPresenter(final View view, final Context ccc) {
        this.view = view;
        //TODO skal flyttes til kun aktivt vindue
        final Handler handler = new Handler();
        Timer timerAsync = new Timer();
        TimerTask timerTaskAsync;
        final View vv = view;
        final SensorActivityPresenter p = this;
        timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            SensorDataAsyncTask performBackgroundTask = new SensorDataAsyncTask(p, vv);
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerAsync.schedule(timerTaskAsync, 0, 4000);


        //netværks skan

/*
        Timer timerAsync2 = new Timer();
        TimerTask timerTaskAsync2;

        System.err.println("********************************************");
        timerTaskAsync2 = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {

                            WirelessScanAsyncTask w = new WirelessScanAsyncTask(ccc,view);
                            w.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerAsync2.schedule(timerTaskAsync2, 0, 60000);
        */
    }

    @Override
    public void taskCompleted() {
        System.err.println("SLUT PRUT");
    }

    public void command() {
        HandleCommandAsyncTask performBackgroundTask = new HandleCommandAsyncTask();
        performBackgroundTask.execute();
    }


    public interface View {
       void setSensorInformation(Sensor s);
        void setOperational(boolean isOperational);
        void setWifi();
        void setChartData(List<Log> logs, List<Prediction> predictions);
    }
}
