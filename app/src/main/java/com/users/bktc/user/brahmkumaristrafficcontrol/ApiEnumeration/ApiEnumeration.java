package com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration;

import com.android.volley.Request;
import com.users.bktc.user.brahmkumaristrafficcontrol.network.response.ResponseModel;

public enum ApiEnumeration implements ApiInterface {
RU_SEND_DATA("http://192.168.0.162:8081/firstproject/signup", Request.Method.POST, ResponseModel.class);


    private String url;
    private int apiType;
    private Class<? extends BaseParserInterface> referenceClass;
    private boolean isEditURL;
    private String postFixURL;

    ApiEnumeration(String url, int apiType, Class<? extends BaseParserInterface> referenceClass) {
        this.url = url;
        this.apiType = apiType;
        this.referenceClass = referenceClass;
    }

    ApiEnumeration(String url, int apiType, Class<? extends BaseParserInterface> referenceClass, boolean isEditURL) {
        this.url = url;
        this.apiType = apiType;
        this.referenceClass = referenceClass;
        this.isEditURL = isEditURL;
    }

    public String getApiServiceName() {
        if (isEditURL && postFixURL != null) {
            return url + postFixURL;
        } else {
            return url;
        }
    }

    public int getApiType() {
        return apiType;
    }

    public Class<? extends BaseParserInterface> getReferenceClass() {
        return referenceClass;
    }

    public void setPostFixURL(String postFixURL) {
        this.postFixURL = postFixURL;
    }

}
