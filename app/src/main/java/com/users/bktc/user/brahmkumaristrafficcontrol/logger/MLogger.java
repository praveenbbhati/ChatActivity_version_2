package com.users.bktc.user.brahmkumaristrafficcontrol.logger;


import android.util.Log;


/**
 * Created by ARPAN on 9/14/2016.
 */
public class MLogger {

    private static final boolean isLogEnabled = true;

    public static void debug(String pTag, String pMessage) {
        if (isLogEnabled) {
            if (pTag == null)
                pTag = "";
            Log.d("TAG DEBUG : " + pTag, " message : " + pMessage);
        }
    }

    public static void error(String pTag, Exception pException) {
        if (isLogEnabled) {
            if (pTag == null)
                pTag = "";
            Log.e("TAG ERROR : " + pTag, " message : " + pException != null ? "" + pException.getMessage() : "");
        }
    }

    public static void error(String pTag, String pMessage) {
        if (isLogEnabled) {
            if (pTag == null)
                pTag = "";
            Log.e("TAG ERROR : " + pTag, " message : " + pMessage);
        }
    }

    public static void error(String pTag, String mes, Exception pException) {
        if (isLogEnabled) {
            if (pTag == null)
                pTag = "";
            Log.e("TAG ERROR : " + pTag, "ErrorMessage : " + mes, pException);
        }
    }

    public static void info(String pTag, String pMessage) {
        if (isLogEnabled) {
            if (pTag == null)
                pTag = "";
            Log.i("TAG INFO : " + pTag, " message : " + pMessage);
        }
    }

    public static void verbose(String pTag, String pMessage) {
        if (isLogEnabled) {
            if (pTag == null)
                pTag = "";
            Log.v("TAG VERBOSE : " + pTag, " message : " + pMessage);
        }
    }

    public static void warn(String pTag, String pMessage) {
        if (isLogEnabled) {
            if (pTag == null)
                pTag = "";
            Log.w("TAG WARN : " + pTag, " message : " + pMessage);
        }
    }
}

