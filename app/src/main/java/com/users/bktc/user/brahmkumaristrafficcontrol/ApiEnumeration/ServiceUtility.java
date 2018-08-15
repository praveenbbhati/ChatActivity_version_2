package com.users.bktc.user.brahmkumaristrafficcontrol.ApiEnumeration;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.Utilities.Utils;
import com.users.bktc.user.brahmkumaristrafficcontrol.logger.MLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public abstract class ServiceUtility {
    private int mInitialTimeoutMS = 0;

    protected boolean isNetworkAvailable(Context context, ApiEnumeration apiRequestCode, VolleyResponse listener) {
        if (Utils.haveNetworkConnection(context)) {
            return true;
        } else {
            if (listener != null) {
                String message = context.getString(R.string.internet_check);
                listener.onFailure(message, apiRequestCode);
            }
            return false;
        }
    }
    protected void handleDefaultScenario(BaseParserInterface parsedObject, VolleyResponse listener, ApiEnumeration apiEnumeration) {


            try {
                listener.onResult(parsedObject, apiEnumeration);
            } catch (Exception e) {
                MLogger.error("", e.getMessage());
            }

    }
    protected boolean isNetworkNotOnProxy(Context context, ApiEnumeration apiEnumeration, VolleyResponse listener) {
        if (Utils.checkIfRunningOnProxy(URI.create(apiEnumeration.getApiServiceName()))) {
//            if (listener != null) {
//                String message = context.getString(R.string.proxy_check_message);
//                listener.onFailure(message, apiEnumeration);
//            }
            return true;
        } else {
            return true;
        }
    }
    protected <T> void sendRequest(Context context, VolleyResponse listener, ApiEnumeration apiEnumeration,
                                   ApiMarkerInterface commonServiceRequestModel,
                                   boolean isProgressShow, String message,String pdtitle) throws JSONException {
        if (isNetworkAvailable(context, apiEnumeration, listener) && isNetworkNotOnProxy(context, apiEnumeration, listener)) {
            if (isProgressShow) {
                Utils.displayProgressDialog(message, context,pdtitle);
            }
            switch (apiEnumeration.getApiType()) {
                case Request.Method.POST:
                    sendPostRequest(context, listener, commonServiceRequestModel, apiEnumeration, isProgressShow, message);
                    break;
                case Request.Method.GET:
                    sendGetRequest(context, listener, apiEnumeration, isProgressShow, message);
                    break;
            }
        }
    }

    protected void sendGetRequest(final Context mContext, final VolleyResponse listener,
                                  final ApiEnumeration apiEnumeration, final Boolean isShowDialog, final String loaderMessage) throws JSONException {
        StringRequest jsObjRequest = new StringRequest(apiEnumeration.getApiServiceName(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (listener != null) {
                    String apiDetails = "Requested Url: \n" + apiEnumeration.getApiServiceName()
                            + "\n\n" + "Requested Body: \n" + ""
                            + "\n\n" + "Headers: \n" + getRequestHeaders(mContext, apiEnumeration)
                            + "\n\n" + "Response Received: \n" + response.toString();
                    MLogger.debug("ApiDetails", apiDetails);

                        onResponseReceived(mContext, RequestResponse.SUCCESS, response, null,
                                null, listener, apiEnumeration, isShowDialog, loaderMessage);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    String apiDetails = "Requested Url: \n" + apiEnumeration.getApiServiceName()
                            + "\n\n" + "Requested Body: \n" + ""
                            + "\n\n" + "Headers: \n" + getRequestHeaders(mContext, apiEnumeration)
                            + "\n\n" + "Response Received: \n" + error.toString();
                    MLogger.debug("ApiDetails", apiDetails);
                    onResponseReceived(mContext, RequestResponse.FAILURE, null, null, null,
                            listener, apiEnumeration, isShowDialog, loaderMessage);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getRequestHeaders(mContext, apiEnumeration);
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(mInitialTimeoutMS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsObjRequest.setShouldCache(false);

//        Volley.newRequestQueue(mContext).add(jsObjRequest);
        addRequestQueue(mContext, jsObjRequest);
    }

    private void addRequestQueue(Context pContext, Request jsObjRequest) {

//        Volley.newRequestQueue(pContext, new HurlStack(null, pinnedSSLSocketFactory)).add(jsObjRequest);
        Volley.newRequestQueue(pContext).add(jsObjRequest);
    }

    protected <T> void sendPostRequest(final Context mContext, final VolleyResponse listener,
                                       final ApiMarkerInterface commonServiceRequestModel,
                                       final ApiEnumeration apiEnumeration, final Boolean isShowDialog,
                                       final String loaderMessage) throws JSONException {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(apiEnumeration.getApiType(),
                apiEnumeration.getApiServiceName(), getJsonObjectFromPojo(commonServiceRequestModel),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (listener != null) {
                            String apiDetails = "Requested Url: \n" + apiEnumeration.getApiServiceName()
                                    + "\n\n" + "Requested Body: \n" + getJsonObjectFromPojo(commonServiceRequestModel)
                                    + "\n\n" + "Headers: \n" +getRequestHeaders(mContext, apiEnumeration)
                                    + "\n\n" + "Response Received: \n" + response.toString();
                            MLogger.debug("ApiDetails", apiDetails);

                                onResponseReceived(mContext, RequestResponse.SUCCESS, response.toString(), null,
                                        commonServiceRequestModel, listener, apiEnumeration, isShowDialog, loaderMessage);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    String apiDetails = "Requested Url: \n" + apiEnumeration.getApiServiceName()
                            + "\n\n" + "Requested Body: \n" + getJsonObjectFromPojo(commonServiceRequestModel)
                            + "\n\n" + "Headers: \n" + getRequestHeaders(mContext, apiEnumeration)
                            + "\n\n" + "Response Received: \n" + error.toString();
                    MLogger.debug("ApiDetails", apiDetails);
                    onResponseReceived(mContext, RequestResponse.FAILURE, null, error, commonServiceRequestModel, listener,
                            apiEnumeration, isShowDialog, loaderMessage);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getRequestHeaders(mContext, apiEnumeration);
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(mInitialTimeoutMS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsObjRequest.setShouldCache(false);

//        Volley.newRequestQueue(mContext).add(jsObjRequest);
        addRequestQueue(mContext, jsObjRequest);
    }
    public <T> JSONObject getJsonObjectFromPojo(ApiMarkerInterface requestModel) {
        JSONObject jsonObject = null;
        Type type = new TypeToken<T>() {
        }.getType();
        String json = new Gson().toJson(requestModel, type);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    protected void onResponseReceived(Context context, RequestResponse successOrFailure, String response, Object volleyError,
                                      ApiMarkerInterface commonServiceRequestModel, VolleyResponse listener,
                                      ApiEnumeration apiEnumeration, Boolean isShowDialog, String loaderMessage) {
        try {
            Utils.hideProgressDialog();
            switch (successOrFailure) {
                case SUCCESS:
                    if (!TextUtils.isEmpty(response)) {
                        BaseParserInterface parsedObject = new Gson().fromJson(response, (Type) apiEnumeration.getReferenceClass());
                        if (parsedObject != null) {
                            handleHttpSuccessDataFromServer(parsedObject, response, commonServiceRequestModel, listener,
                                    apiEnumeration, isShowDialog, loaderMessage);
                        } else {
                            listener.onFailure(context.getString(R.string.no_response), apiEnumeration);
                        }
                    } else {
                        listener.onFailure(context.getString(R.string.no_response), apiEnumeration);
                    }
                    break;
                case FAILURE:
                    String message = setFailureMessage(context, volleyError, "");
                    listener.onFailure(message, apiEnumeration);
                    break;
            }
        } catch (Exception e) {
            MLogger.error("error", e);
        }
    }
    protected void handleHttpSuccessDataFromServer(BaseParserInterface parsedObject, String response,
                                                   ApiMarkerInterface commonServiceRequestModel,
                                                   VolleyResponse listener, ApiEnumeration apiEnumeration,
                                                   Boolean isShowDialog, String loaderMessage) throws JSONException {
    }
    protected String setFailureMessage(Context context, Object volleyError, String message) {
        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
            message = context.getString(R.string.error_message_time_out);
        } else if (volleyError instanceof AuthFailureError) {
            message = context.getString(R.string.error_message_auth_fail);
        } else if (volleyError instanceof ServerError) {
            message = context.getString(R.string.error_message_server_error);
        } else if (volleyError instanceof NetworkError) {
            message = context.getString(R.string.error_message_network_error);
        } else if (volleyError instanceof ParseError) {
            message = context.getString(R.string.error_message_parse_error);
        }
        return message;
    }
    protected Map<String, String> getRequestHeaders(Context context, ApiEnumeration apiEnumeration) {
        HashMap<String, String> header = new HashMap();
     //set headers here
//            header.put(RequestConstants.HK_APP_VERSION, "" + Utils.getPackageVersion(context));
//            header.put(RequestConstants.HK_PLATFORM, "Android");
//            header.put(RequestConstants.HK_RESOLUTION, Utils.getScreenDensity(context));
//            header.put(RequestConstants.HK_LANGUAGE, Utils.getDefaultLanguageCode());

        return header;
    }
}
