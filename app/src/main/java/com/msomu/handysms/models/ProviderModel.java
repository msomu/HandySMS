package com.msomu.handysms.models;

import java.util.ArrayList;

/**
 * Created by msomu on 10/03/16.
 */
public class ProviderModel {
    private int id;
    private String provider;
    private ArrayList<SmsDataClass> smsDataClassArrayList;
    private int totalSms;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<SmsDataClass> getSmsDataClassArrayList() {
        return smsDataClassArrayList;
    }

    public void setSmsDataClassArrayList(ArrayList<SmsDataClass> smsDataClassArrayList) {
        this.smsDataClassArrayList = smsDataClassArrayList;
    }

    public int getTotalSms() {
        return totalSms;
    }

    public void setTotalSms(int totalSms) {
        this.totalSms = totalSms;
    }
}
