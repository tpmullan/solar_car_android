package com.scf.michael.solarcarbasic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Created by Michael on 1/19/2017.
 */


    public class myBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, "wifi 1", Toast.LENGTH_SHORT).show();

            context.startService(intent);


            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager !=null){
                Toast.makeText(context, "wifi 2", Toast.LENGTH_SHORT).show();
                //wifiManager.toString();
            }
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            info.getType();
            if (networkInfo != null) {
                Toast.makeText(context, "wifi 3", Toast.LENGTH_SHORT).show();
                //Log.d(AppConstants.TAG, "Type : " + networkInfo.getType()
                //      + "State : " + networkInfo.getState());

                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    Toast.makeText(context, "wifi 4", Toast.LENGTH_SHORT).show();

                    //get the different network states
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTING || networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        Toast.makeText(context, "wifi 5", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }


