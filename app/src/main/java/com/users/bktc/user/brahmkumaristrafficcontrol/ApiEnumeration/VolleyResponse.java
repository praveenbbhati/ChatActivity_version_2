package com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration;

public interface VolleyResponse<T> {
    void onResult(T resultModel, ApiEnumeration apiEnumeration);
    void onFailure(String errorMessage, ApiEnumeration apiEnumeration);
}
