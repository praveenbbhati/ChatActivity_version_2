package com.users.bktc.user.brahmkumaristrafficcontrol.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.BaseParserInterface;

public class ResponseModel implements BaseParserInterface {
    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int statusCode() {
        return 0;
    }

    @Override
    public String errorMessage() {
        return null;
    }
}
