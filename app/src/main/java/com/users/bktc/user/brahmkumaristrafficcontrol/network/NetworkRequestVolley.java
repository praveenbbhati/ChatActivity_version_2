package com.users.bktc.user.brahmkumaristrafficcontrol.network;

import android.content.Context;
import android.text.TextUtils;

import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.ApiEnumeration;
import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.ApiMarkerInterface;
import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.BaseParserInterface;
import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.ServiceUtility;
import com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration.VolleyResponse;

import org.json.JSONException;


public class NetworkRequestVolley extends ServiceUtility {

    private Context mContext;
    private VolleyResponse mListener;
    private boolean isTokenExpired = false;

    public NetworkRequestVolley(VolleyResponse resultContext, Context context) {
        mContext = context;
        mListener = resultContext;
    }

    public <T> void sendRequest( ApiEnumeration apiEnumeration, ApiMarkerInterface commonServiceRequestModel,
                                boolean isProgressShow, String message,String pdtitle) throws JSONException {
       sendRequest(mContext ,mListener,apiEnumeration, commonServiceRequestModel, isProgressShow, message,pdtitle);
    }

    @Override
    protected void handleHttpSuccessDataFromServer(BaseParserInterface parsedObject, String response, ApiMarkerInterface commonServiceRequestModel,
                                                   VolleyResponse listener, ApiEnumeration apiEnumeration,
                                                   Boolean isShowDialog, String loaderMessage) {

        if (listener != null) {
            handleDefaultScenario(parsedObject, listener, apiEnumeration);

        }
    }


}