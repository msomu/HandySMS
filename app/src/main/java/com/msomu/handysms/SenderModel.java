package com.msomu.handysms;

/**
 * Created by msomu on 10/03/16.
 */
public class SenderModel {
    private int id;
    private String sender;

    public SenderModel(int id, String sender) {
        this.id = id;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
