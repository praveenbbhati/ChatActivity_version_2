package com.users.bktc.user.brahmkumaristrafficcontrol.network.database;

public class ChattingToUsers {
    String id;
    String phone;
    String lastTimeStamp;
    String lastMessage;
    String newMessageCout;

    public String getNewMessageCout() {
        return newMessageCout;
    }

    public void setNewMessageCout(String newMessageCout) {
        this.newMessageCout = newMessageCout;
    }

    public ChattingToUsers() {
    }


    public ChattingToUsers(String phone, String lastTimeStamp, String lastMessage, String newMessageCout) {
        this.phone = phone;
        this.lastTimeStamp = lastTimeStamp;
        this.lastMessage = lastMessage;
        this.newMessageCout = newMessageCout;
    }

    public ChattingToUsers(String id, String phone, String lastTimeStamp, String lastMessage, String newMessageCout) {
        this.id = id;
        this.phone = phone;
        this.lastTimeStamp = lastTimeStamp;
        this.lastMessage = lastMessage;
        this.newMessageCout = newMessageCout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(String lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
