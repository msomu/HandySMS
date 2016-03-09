package com.msomu.handysms;

/**
 * Created by msomu on 03/03/16.
 */
public class SMSTemplate {
    private int id;
    private int senderId;
    private String template;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
