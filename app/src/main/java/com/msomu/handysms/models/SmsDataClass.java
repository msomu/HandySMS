package com.msomu.handysms.models;

/**
 * Created by msomu on 02/03/16.
 */
public class SmsDataClass {
    private String address;
    private String body;
    private String date;
    private String type;
    private String providerName;
    private int providerId;
    private String typeOfCateogry;
    private int id;

    public SmsDataClass() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeOfCateogry() {
        return typeOfCateogry;
    }

    public void setTypeOfCateogry(String typeOfCateogry) {
        this.typeOfCateogry = typeOfCateogry;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
