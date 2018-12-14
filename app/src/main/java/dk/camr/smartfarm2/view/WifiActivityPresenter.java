package dk.camr.smartfarm2.view;

import android.content.Context;

import dk.camr.smartfarm2.SensorActivityPresenter;

public class WifiActivityPresenter {

    WifiActivityPresenter.View view;


    public WifiActivityPresenter(final WifiActivityPresenter.View view) {




    }

    public WifiActivityPresenter() {

    }

    public interface View {
        void setWifi();

    }
}
