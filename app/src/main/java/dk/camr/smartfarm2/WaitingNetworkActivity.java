package dk.camr.smartfarm2;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import java.util.Timer;
import java.util.TimerTask;

import dk.camr.smartfarm2.Data.SensorDataAsyncTask;
import dk.camr.smartfarm2.Network.TestLocalNetworkAsyncTask;

/*Test of network*/
public class WaitingNetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_network);



        final Handler handler = new Handler();
        Timer timerAsync = new Timer();
        TimerTask timerTaskAsync;

        timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            TestLocalNetworkAsyncTask performBackgroundTask = new TestLocalNetworkAsyncTask();
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerAsync.schedule(timerTaskAsync, 0, 1000);


    }
}
