package com.users.bktc.user.brahmkumaristrafficcontrol.services;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionBroadcastListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that which service class will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                ReceivceMessageService.class.getName());
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        for(int i=0;i<30;i++) {
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                ReceivceMessageService.isNetworkConnected = true;
                intent.putExtra("isNetworkConnected", true);
                context.startService(new Intent(intent.setComponent(comp)));
                break;
            }


            NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi != null && mWifi.isConnectedOrConnecting()) {
                intent.putExtra("isNetworkConnected", true);
                ReceivceMessageService.isNetworkConnected = true;
                context.startService(new Intent(intent.setComponent(comp)));
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
