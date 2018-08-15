package com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter;

/**
 * Created by sourabh on 8/22/2017.
 */

public class ChatModel {
    public String massege;
    public Boolean Send;
    public String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ChatModel(String massege, Boolean send, String timeStamp) {
        this.massege = massege;
        Send = send;
        this.timeStamp = timeStamp;
    }

    public ChatModel() {
    }

    public String getMassege() {
        return massege;
    }

    public void setMassege(String massege) {
        this.massege = massege;
    }

    public Boolean getSend() {
        return Send;
    }

    public void setSend(Boolean send) {
        Send = send;
    }
}
