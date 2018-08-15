package com.users.bktc.user.brahmkumaristrafficcontrol.Utilities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.users.bktc.user.brahmkumaristrafficcontrol.R;
import com.users.bktc.user.brahmkumaristrafficcontrol.logger.MLogger;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;

public class Utils {
    public static ProgressDialog progressDialog;
    public static boolean haveNetworkConnection(Context c) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public static boolean checkIfRunningOnProxy(URI uri) {
        boolean isProxyOn = false;
        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        Proxy proxy = null;
        List<Proxy> proxyList = defaultProxySelector.select(uri);
        if (proxyList.size() > 0) {
            proxy = proxyList.get(0);
            if (proxy != null && proxy.address() != null) {
                Log.d("", "Current Proxy Configuration: " + proxy.toString());
                isProxyOn = true;
            }
        }
        return isProxyOn;
    }
    public static void displayProgressDialog(String message, Context mContext,String title) {
        try {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(message); // Setting Message
            progressDialog.setTitle(title); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

        } catch (Exception e) {
        }
    }
    public static void hideProgressDialog() {
        try {
            if (progressDialog != null)
                progressDialog.dismiss();
        } catch (Exception e) {
        }
    }

    public static String getFormatDateTimeLong(String longV) {
        String dateString = "";
        if (longV != null) {
            try {
                long millisecond = Long.parseLong(longV);
                //TimeZone tz = TimeZone.getDefault();
                //long addgmtvalue = millisecond + tz.getRawOffset();
                SimpleDateFormat formatter=new SimpleDateFormat("dd MMM, yyyy");
                SimpleDateFormat forma=new SimpleDateFormat("hh:mm a");
                dateString = formatter.format(millisecond);
                if(dateString.equals(formatter.format(System.currentTimeMillis()))){
                    return   forma.format(millisecond);
                }
            } catch (Exception e) {
                dateString = longV;
            }
        }
        return dateString;
    }
    public static void showSnackBar(String message, View view) {
        try {
            if (view != null && !TextUtils.isEmpty(message)) {
                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.DKGRAY);
                TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setMaxLines(R.integer.snackbar_max_length);
                snackbar.setDuration(5000);
                snackbar.show();
            }
        } catch (Exception e) {
            MLogger.error("Exception", e.getMessage());
        }
    }
    public static boolean isSyncAccountExists(Context con){
        try {
            AccountManager accountManager =AccountManager.get(con);
            Account a[]= accountManager.getAccountsByType(Constants.SyncAccountType);
            if(a.length >0){
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void createSyncAccount(Context context,String username,String password,String email) throws Exception{


        // Create the account type and default account
        Account newAccount = new Account(Constants.SyncAccountname,Constants.SyncAccountType);

        // Get an instance of the Android account manager
        AccountManager accountManager = AccountManager.get(context);

       // Bundle userData = new Bundle();
     //   userData.putString(Constants.USERDATA_USER_OBJ_ID, puser.getObjectId());
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, "password", null)) {

            ContentResolver.setIsSyncable(newAccount, Constants.SyncAuthority, 1);
            ContentResolver.setMasterSyncAutomatically(true); // enables AutoSync
            ContentResolver.setSyncAutomatically(newAccount,Constants.SyncAuthority, true);
            ContentResolver.addPeriodicSync(newAccount, Constants.SyncAuthority, new Bundle(), 120);

            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            Log.i("acc", "Account Created");
        } else {
            Log.i("acc","Account Can't Created Some Error Occur");
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
    }

}
