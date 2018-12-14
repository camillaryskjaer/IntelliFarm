package dk.camr.smartfarm2.Sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Prediction {

    @SerializedName("time")
    @Expose
    private Date time;
    @SerializedName("value")
    @Expose
    private float value;
    @SerializedName("sensorId")
    @Expose
    private Integer sensorId;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

}