package com.scf.michael.solarcarbasic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.scf.michael.solarcarbasic.api.ClosedTrackSolarApiEndpoint;
import com.scf.michael.solarcarbasic.api.ServiceGenerator;
import com.scf.michael.solarcarbasic.api.TeamLocation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

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
    private ClosedTrackSolarApiEndpoint apiService;

        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            context.startService(intent);
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager !=null){
                //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
            }
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null) {
                //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                    if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        Toast.makeText(context, "super tubes disconnected", Toast.LENGTH_SHORT).show();
                    }

                    //get the different network states
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        Toast.makeText(context, "super tubes connected", Toast.LENGTH_SHORT).show();
                        try {
                            checkLogs(context, intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "couldn't send logs", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                }
            }

        }

        public void checkLogs(Context context, Intent intent){
            //final TeamLocation newLoc = new TeamLocation();
            //final Team newTeam = new Team();
            //Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
            int tmpint=1;
            int senttoservercnt=0;  //we only want to send the first 700 items at a time to prevent overloading the app with stuff to do
            Toast.makeText(context, "starting sending", Toast.LENGTH_SHORT).show();
            TeamLocation newLoc = TeamLocation.findById(TeamLocation.class,1);
            //while (newLoc.getUpdatedAt() != null) {
            while (newLoc.getUpdatedAt() != null && senttoservercnt<700) {
                //Toast.makeText(context, "here", Toast.LENGTH_SHORT).show();
                if (newLoc.getRemoteId() == null ){
                    //Toast.makeText(context, "not sent: ".concat(Integer.toString(tmpint)).concat(" "), Toast.LENGTH_SHORT).show();
                    updateServer(tmpint, context);
                    senttoservercnt++;
                } else {
                    //Toast.makeText(context, "already sent: ".concat(Integer.toString(tmpint)).concat(" ").concat(newLoc.getUpdatedAt()), Toast.LENGTH_SHORT).show();
                }
                tmpint++;
                try {
                    newLoc = TeamLocation.findById(TeamLocation.class,tmpint);
                }catch (Exception e){
                    return;
                }


            }
            Toast.makeText(context, "done sending", Toast.LENGTH_SHORT).show();
            //newLoc.getUpdatedAt();
            //Toast.makeText(context, newLoc.getUpdatedAt().toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "super tubers2", Toast.LENGTH_SHORT).show();

        }

        public void updateServer(Integer tmpint, Context context){
            apiService = ServiceGenerator.createService(ClosedTrackSolarApiEndpoint.class);
            final TeamLocation newLoc = TeamLocation.findById(TeamLocation.class,tmpint);

            if (newLoc.getUpdatedAt() == null) {
            // if (newLoc.getLongitude().isNaN()) {
                Toast.makeText(context, "no created time", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, newLoc.getUpdatedAt(), Toast.LENGTH_SHORT).show();
            }

            Call<TeamLocation> call = apiService.createTeamLocation(newLoc);

            // Send data to Shane's server

            call.enqueue(new Callback<TeamLocation>() {
                @Override /*If you get a response, do this*/
                public void onResponse(Call<TeamLocation> call, Response<TeamLocation> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        TeamLocation receivedLoc = response.body();
                        newLoc.setRemoteId(receivedLoc.getRemoteId());
                        newLoc.save();
                    } else {
                        //Toast.makeText(mContext.getApplicationContext(), "Failed POST!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override /*if you get an error, do this*/
                public void onFailure(Call<TeamLocation> call, Throwable t) {
                    // Log error here since request failed
                    //Log.e(TAG, "Failed to post location");
                    //Toast.makeText(mContext.getApplicationContext(), "Failed to post location", Toast.LENGTH_LONG).show();
                }
            });

        }

    }


