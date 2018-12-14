package dk.camr.smartfarm2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import dk.camr.smartfarm2.Sensors.Log;
import dk.camr.smartfarm2.Sensors.Prediction;
import dk.camr.smartfarm2.Sensors.Sensor;
import dk.camr.smartfarm2.view.FontAwesome;
import dk.camr.smartfarm2.view.WifiActivityPresenter;


public class SensorActivity extends AppCompatActivity implements SensorActivityPresenter.View, WifiActivityPresenter.View {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    SensorActivityPresenter presenter;


    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        presenter = new SensorActivityPresenter(this, getApplicationContext());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }


        Switch sw = (Switch) findViewById(R.id.operational);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //send besked afsted til server
                presenter.update("?automated=" + isChecked);
                System.out.println("Switch State= " + isChecked);
            }

        });

        chart = findViewById(R.id.chart1);
        chart.setViewPortOffsets(0, 0, 0, 0);


        //akse
        YAxis y = chart.getAxisLeft();
        // y.setTypeface(tfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setDrawLabels(false);
        y.setDrawGridLines(false);
        y.setDrawAxisLine(false);
        y.setAxisLineColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setBorderColor(0);
        chart.setDescription(null);
        XAxis x = chart.getXAxis();
        x.setDrawGridLines(false);
        x.setDrawAxisLine(false);
        x.setAxisLineWidth(0);
        x.setDrawLabels(true);
        chart.getLegend().setEnabled(true);


        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * (1 + 1)) + 20;
            values.add(new Entry(i, val));
        }

        LineDataSet set1 = new LineDataSet(values, "Test1");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.4f);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.parseColor("#56B7F1"));
        set1.setFillAlpha(100);
        set1.setColor(Color.parseColor("#56B7F1"));
        set1.setDrawCircles(false);
        LineData ld = new LineData(set1);
        chart.setData(ld);
        chart.getLegend().setEnabled(true);
        chart.animateXY(2000, 2000);
        // don't forget to refresh the drawing
        chart.invalidate();


    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    System.err.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public void setSensorInformation(Sensor s) {
        TextView sensorType = (TextView) findViewById(R.id.sensorType);
        sensorType.setText(s.getType());
        Switch active = (Switch) findViewById(R.id.active);
        FontAwesome fa = (FontAwesome) findViewById(R.id.sensorIcon);


        fa.setIcon(s.getIcon());
        active.setOnCheckedChangeListener(null);
        active.setChecked(s.isActive());
        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                //send besked afsted til server
                presenter.command();
                System.out.println("Switch State= " + isChecked);
            }

        });

        FontAwesome si = (FontAwesome) findViewById(R.id.statusIcon);

        if (s.getStatus().toLowerCase().equals(("normal")))
            si.setTextColor(Color.parseColor("#75b327"));
        else if (s.getStatus().toLowerCase().equals(("moderate")))
            si.setTextColor(Color.parseColor("#e9c411"));
        else if (s.getStatus().toLowerCase().equals(("critical")))
            si.setTextColor(Color.parseColor("#d80b1b"));
        else
            si.setTextColor(Color.parseColor("#c6c7c8"));
    }


    public void setChartData(List<Log> logs, List<Prediction> predictions) {


        chart.clear();
        ArrayList<Entry> values = new ArrayList<>();


        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        for (int i = 0; i < logs.size(); i++) {
            Entry e = new Entry(i, logs.get(i).getValue());
            e.setData(format.format(logs.get(i).getTime()));
            values.add(e);
        }

        LineDataSet set1 = new LineDataSet(values, "Temperatur");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.4f);
        set1.setDrawFilled(true);
        set1.setCircleColor(Color.RED);
        set1.setFillColor(Color.parseColor("#56B7F1"));
        set1.setFillAlpha(100);
        set1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "Kl. " + entry.getData().toString() + ":" + value;
            }
        });
        set1.setCircleRadius(4f);
        set1.setColor(Color.parseColor("#56B7F1"));
        set1.setDrawCircles(true);
        List<Integer> co = new ArrayList<>();
        co.add(Color.parseColor("#75b327"));
        co.add(Color.parseColor("#c6c7c8"));


        set1.setCircleColors(co);
        LineData ld = new LineData(set1);
        chart.setData(ld);

        ArrayList<Entry> values2 = new ArrayList<>();

        if (predictions != null)
            for (int i = 0; i < predictions.size(); i++) {
                Entry e = new Entry(i, predictions.get(i).getValue());
                e.setData(format.format(predictions.get(i).getTime()));
                values2.add(e);
            }

        LineDataSet set2 = new LineDataSet(values2, "Forudsigelse");

        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setCubicIntensity(0.4f);
        set2.setDrawFilled(true);
        set2.setCircleColor(Color.RED);
        set2.setFillColor(Color.parseColor("#F29400"));
        set2.setFillAlpha(100);
        set2.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ":" + value;
            }
        });
        set2.setCircleRadius(4f);
        set2.setColor(Color.parseColor("#F29400"));
        set2.setDrawCircles(true);

        co.add(Color.parseColor("#75b327"));
        co.add(Color.parseColor("#c6c7c8"));


        set2.setCircleColors(co);
        LineData ld2 = new LineData(set2);
        chart.setData(ld2);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);
        chart.setData(data);

        // don't forget to refresh the drawing
        chart.invalidate();


     /*   ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
        mCubicValueLineChart.clearChart();
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);


        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        for(int i =0; i < logs.size(); i++){

            ValueLinePoint vp =new ValueLinePoint(format.format(logs.get(i).getTime()), logs.get(i).getValue());
            vp.setLegendLabel(format.format(logs.get(i).getTime()));
            series.addPoint(vp);
        }
        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.setShowIndicator(false);
        mCubicValueLineChart.setShowStandardValues(false);
        mCubicValueLineChart.startAnimation();
*/
    }

    @Override
    public void setOperational(boolean isOperational) {
        //in here we set the form for operating the smart unit
        if (isOperational) {
            Switch sw = (Switch) findViewById(R.id.operational);
            sw.setChecked(isOperational);
            sw.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void setWifi() {
        final Activity v = this;
        System.err.println("WIFI");
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Ønsker du at konfigurere denne?")
                .setTitle("Der er fundet en smart unit");
        builder.setPositiveButton("Ja",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //change network SSID
                        WifiConfiguration conf = new WifiConfiguration();
                        String networkPass = "MKR1000AP2";
                        conf.SSID = "\"" + networkPass + "\"";
                        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                        wifiManager.addNetwork(conf);
                        int netId = wifiManager.addNetwork(conf);
                        wifiManager.disconnect();

                        System.err.println("New network?" + netId);

                        wifiManager.enableNetwork(netId, true);
                        wifiManager.reconnect();


                        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                        for (WifiConfiguration i : list) {
                            System.err.println(i.SSID);
                            /*
                            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {

                                wifiManager.enableNetwork(i.networkId, true);
                                wifiManager.reconnect();

                                break;
                            }*/
                        }
                        //open a browser


                     /*  Intent in = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://192.168.1.1/"));
                        startActivity(in);
*/
                        Intent myIntent = new Intent(v, WaitingNetworkActivity.class);
                        startActivity(myIntent);
                    }
                });

        builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}