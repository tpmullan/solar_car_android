package com.scf.michael.solarcarbasic.locations;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.scf.michael.solarcarbasic.api.ClosedTrackSolarApiEndpoint;
import com.scf.michael.solarcarbasic.api.ServiceGenerator;
import com.scf.michael.solarcarbasic.api.TeamLocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tom on 2/4/17.
 */

public class SolarLocationListener implements LocationListener {

    private static final String TAG = "LocationListener";

    public Location mLastLocation;
    private Context mContext;
    private ClosedTrackSolarApiEndpoint apiService;

    public int TeamIndex=1;
    public SolarLocationListener(String provider, Context context)
    {
        //Log.e(TAG, "SolarLocationListener " + provider);
        mLastLocation = new Location(provider);
        mContext = context;
        apiService = ServiceGenerator.createService(ClosedTrackSolarApiEndpoint.class);
    }


    @Override
    public void onLocationChanged(Location location) {
        // if the accuracy is really bad, back out
        if (location.getAccuracy()>100) {
            //Toast.makeText(getBaseContext(), " ", Toast.LENGTH_SHORT).show();
            return;
        }

        final TeamLocation newLoc = new TeamLocation();
        newLoc.setLongitude(location.getLongitude());
        newLoc.setLatitude(location.getLatitude());
        newLoc.setAltitude(location.getAltitude());
        newLoc.setAccuracy(location.getAccuracy());
        newLoc.setTeam(TeamIndex);
        newLoc.setUpdatedAt(Calendar.getInstance().getTime().toString());
        newLoc.save();

        //send data to server
        Call<TeamLocation> call = apiService.createTeamLocation(newLoc);

        String Phone_ID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        Float batteryPct = batteryInfo();
        //Toast.makeText(mContext.getApplicationContext(), realm.copyFromRealm(newLoc).getTeam(), Toast.LENGTH_LONG).show();

        // Send data to Shane's server
        call.enqueue(new Callback<TeamLocation>() {
            @Override /*If you get a response, do this*/
            public void onResponse(Call<TeamLocation> call, Response<TeamLocation> response) {
                int statusCode = response.code();
                if (response.isSuccessful()){
                    TeamLocation receivedLoc = response.body();

                    newLoc.setRemoteId(receivedLoc.getRemoteId());
                    newLoc.save();
                    //Toast.makeText(mContext.getApplicationContext(), "POST!", Toast.LENGTH_LONG).show();
                    //Toast.makeText(mContext.getApplicationContext(), receivedLoc.getTeam(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext.getApplicationContext(), "Failed POST!", Toast.LENGTH_LONG).show();
                }
            }

            @Override /*if you get an error, do this*/
            public void onFailure(Call<TeamLocation> call, Throwable t) {
                // Log error here since request failed
                //Log.e(TAG, "Failed to post location");
                //Toast.makeText(mContext.getApplicationContext(), "Failed to post location", Toast.LENGTH_LONG).show();
            }
        });

        createFile(getFileString(location, newLoc, Phone_ID, batteryPct)); //CreateFile -writes important data to a csv file
    }

    @NonNull
    private String getFileString(Location location, TeamLocation newLoc, String phone_ID, Float batteryPct) {
        String output;
        String status = "N/A";
        String judge="Unknown";
        String team="Unknown";

        try {
            output = phone_ID + ", " + newLoc.getUpdatedAt() + ", " + newLoc.getLatitude().toString() + ", " + newLoc.getLongitude().toString() +", " +batteryPct.toString() + ", " + newLoc.getAltitude()+ ", " + status+ ", " + judge+ ", " + team+", "+ location.getAccuracy()+", "+location.getSpeed() +", "+location.getBearing();
        } catch (Exception e) {
            output = phone_ID + ", " + newLoc.getUpdatedAt() + ", " + newLoc.getLatitude().toString() + ", " + newLoc.getLongitude().toString() +", "+ batteryPct.toString()+ ", 0, N/A, N/A, N/A, 0, 0";
        }

        return output;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Log.e(TAG, "onStatusChanged: " + provider);
    }

    @Override
    public void onProviderEnabled(String s) {
        //Log.e(TAG, "onProviderEnabled: " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        //Log.e(TAG, "onProviderDisabled: " + s);
    }

    public void createFile(String string) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // you can go on

            String Phone_ID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd");

            String filename= "SolarCarTracker-" + Phone_ID + "-" + tf.format(calendar.getTime()) +".csv";

            final File myDir = Environment.getExternalStorageDirectory().getAbsoluteFile();

            try {
                File myFile = new File(myDir.getPath(), filename);

                if(!myDir.exists()){
                    myDir.mkdirs(); //make the folders where we write folders
                }

                if ( !myFile.exists()) {
                    myFile.createNewFile();  //make the file where we write information

                    FileOutputStream fOut = new FileOutputStream(myFile, true); //true means we append
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append("Phone ID, Time, Latitude, Longitude, , Battery Pct, Altitude, Status, Judge, Team, Accuracy, Speed, Bearing");
                    myOutWriter.append("\n");
                    myOutWriter.close();
                    fOut.close();
                }

                FileOutputStream fOut = new FileOutputStream(myFile, true); //true means we append
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(string);
                myOutWriter.append("\n");
                myOutWriter.close();
                fOut.close();

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(mContext.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public float batteryInfo (){

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale;

        return batteryPct;
    }
}
