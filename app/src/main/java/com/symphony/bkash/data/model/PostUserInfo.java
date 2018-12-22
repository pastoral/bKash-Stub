package com.symphony.bkash.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostUserInfo {
    @SerializedName("IMEI1")
    @Expose
    private String iMEI1;
    @SerializedName("IMEI2")
    @Expose
    private String iMEI2;
    @SerializedName("MAC")
    @Expose
    private String mAC;
    @SerializedName("ANDROID_ID")
    @Expose
    private String aNDROIDID;
    @SerializedName("SIM_Number")
    @Expose
    private String sIMNumber;
    @SerializedName("Activated")
    @Expose
    private String activated;
    @SerializedName("Model")
    @Expose
    private String model;

    /**
     * No args constructor for use in serialization
     *
     */
    public PostUserInfo() {
    }

    /**
     *
     * @param aNDROIDID
     * @param model
     * @param mAC
     * @param iMEI2
     * @param iMEI1
     * @param activated
     * @param sIMNumber
     */
    public PostUserInfo(String iMEI1, String iMEI2, String mAC, String aNDROIDID, String sIMNumber, String activated, String model) {
        super();
        this.iMEI1 = iMEI1;
        this.iMEI2 = iMEI2;
        this.mAC = mAC;
        this.aNDROIDID = aNDROIDID;
        this.sIMNumber = sIMNumber;
        this.activated = activated;
        this.model = model;
    }

    public String getIMEI1() {
        return iMEI1;
    }

    public void setIMEI1(String iMEI1) {
        this.iMEI1 = iMEI1;
    }

    public String getIMEI2() {
        return iMEI2;
    }

    public void setIMEI2(String iMEI2) {
        this.iMEI2 = iMEI2;
    }

    public String getMAC() {
        return mAC;
    }

    public void setMAC(String mAC) {
        this.mAC = mAC;
    }

    public String getANDROIDID() {
        return aNDROIDID;
    }

    public void setANDROIDID(String aNDROIDID) {
        this.aNDROIDID = aNDROIDID;
    }

    public String getSIMNumber() {
        return sIMNumber;
    }

    public void setSIMNumber(String sIMNumber) {
        this.sIMNumber = sIMNumber;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
