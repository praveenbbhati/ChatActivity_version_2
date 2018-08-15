package com.users.bktc.user.brahmkumaristrafficcontrol.network.database;

public class Contact {
    public String id;
    public String name;
    public String msgSend;
    public String phone;
    public String timeStamp;


    public Contact(String name, String msgSend, String phone, String timeStamp) {
        this.name = name;
        this.msgSend = msgSend;
        this.phone = phone;
        this.timeStamp = timeStamp;
    }

    public Contact(String id, String name, String msgSend, String phone, String timeStamp) {
        this.id = id;
        this.name = name;
        this.msgSend = msgSend;
        this.phone = phone;
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String isMsgSend() {
        return msgSend;
    }

    public Contact() {
    }

    public void setMsgSend(String msgSend) {
        this.msgSend = msgSend;
    }

    public String getMsgSend() {
        return msgSend;
    }
}
