package dk.camr.smartfarm2.Network;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import java.util.List;

import dk.camr.smartfarm2.SensorActivity;
import dk.camr.smartfarm2.SensorActivityPresenter;
import dk.camr.smartfarm2.Sensors.Sensor;
import dk.camr.smartfarm2.view.WifiActivityPresenter;


public class WirelessScanAsyncTask extends AsyncTask<String, String, ScanResult> {


    Context cc;
    SensorActivityPresenter.View v;

    public WirelessScanAsyncTask(Context cc, SensorActivityPresenter.View v) {
        System.err.println("CONST");
        this.cc = cc;
        this.v = v;

    }

    @Override

    protected ScanResult doInBackground(String... strings) {
        System.err.println("Doing stuff");

        WifiManager wifiManager = (WifiManager) cc.getSystemService(Context.WIFI_SERVICE);
        ;
        List<ScanResult> scanResults = wifiManager.getScanResults();

        for (int i = 0; i < scanResults.size(); i++) {

            System.err.println(scanResults.get(i).SSID);
            if (scanResults.get(i).SSID.equals("MKR1000AP2")) {


                System.out.println(scanResults.get(i));
                return scanResults.get(i);
            }
        }
        return null;
    }


    protected void onPostExecute(ScanResult sc) {

        if (sc != null) {
            System.out.println(sc.SSID);
            v.setWifi();
        }
        System.out.println("scan Done!");
    }
}
