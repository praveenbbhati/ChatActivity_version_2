package com.users.bktc.user.brahmkumaristrafficcontrol.UI.adapter;

public class ChatUsersListModel {
    String phoneNumber;
    String lastMessage;
    String lastTimeStamp;
    String newMessageCount;


    public ChatUsersListModel(String phoneNumber, String lastMessage, String lastTimeStamp, String newMessageCount) {
        this.phoneNumber = phoneNumber;
        this.lastMessage = lastMessage;
        this.lastTimeStamp = lastTimeStamp;
        this.newMessageCount = newMessageCount;
    }

    public ChatUsersListModel(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewMessageCount() {
        return newMessageCount;
    }

    public void setNewMessageCount(String newMessageCount) {
        this.newMessageCount = newMessageCount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(String lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }
}
