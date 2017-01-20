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

/*
        <receiver android:name="myBroadcastReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.net.wifi.supplicant.CONNECTION_CHANGE" />
                <action android:name="android.net.wifi.STATE_CHANGE" />
            </intent-filter>
        </receiver>
*/
    public class myBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            context.startService(intent);
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager !=null){
                Toast.makeText(context, "wifi not null", Toast.LENGTH_SHORT).show();
            }
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    //get the different network states
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTING || networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        Toast.makeText(context, "wifi connecting", Toast.LENGTH_SHORT).show();
                    }
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        Toast.makeText(context, "wifi connected", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }


