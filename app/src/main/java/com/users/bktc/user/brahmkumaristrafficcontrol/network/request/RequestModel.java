package com.users.bktc.user.brahmkumaristrafficcontrol.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.ApiMarkerInterface;

public class RequestModel implements ApiMarkerInterface {
    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("email")
    @Expose
    public String email;


    @SerializedName("pass")
    @Expose
    public String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
