package dk.camr.smartfarm2.Sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Log {

    @SerializedName("time")
    @Expose
    private Date time;
    @SerializedName("sensorId")
    @Expose
    private int sensorId;
    @SerializedName("boxId")
    @Expose
    private int boxId;
    @SerializedName("value")
    @Expose
    private float value;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}