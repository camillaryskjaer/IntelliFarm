package dk.camr.smartfarm2.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Operational {

    @SerializedName("automatic")
    @Expose
    private Boolean automatic;

    public Boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }
}