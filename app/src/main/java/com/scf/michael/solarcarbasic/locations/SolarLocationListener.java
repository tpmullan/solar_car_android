package com.scf.michael.solarcarbasic.locations;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scf.michael.solarcarbasic.api.ClosedTrackSolarApiEndpoint;
import com.scf.michael.solarcarbasic.api.TeamLocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tom on 2/4/17.
 */

public class SolarLocationListener implements android.location.LocationListener {

    private static final String TAG = "LocationListner";
    public static final String BASE_URL = "https://solar.tpmullan.com/api/";

    public Location mLastLocation;
    private Context mContext;

    boolean GPSEnabled = true;

    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private ClosedTrackSolarApiEndpoint apiService = retrofit.create(ClosedTrackSolarApiEndpoint.class);

    TeamLocation oldLoc = new TeamLocation();

    public int TeamIndex=1;
    public SolarLocationListener(String provider, Context context)
    {
        Log.e(TAG, "SolarLocationListener " + provider);
        mLastLocation = new Location(provider);
        mContext = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        // if the accuracy is really bad, back out
        if (location.getAccuracy()>100) {
            //Toast.makeText(getBaseContext(), " ", Toast.LENGTH_SHORT).show();
            return;
        }

        //Display the Lat and Long in the txt boxes
        TeamLocation newLoc = new TeamLocation();
        newLoc.setLongitude(location.getLongitude());
        newLoc.setLatitude(location.getLatitude());
        newLoc.setAltitude(location.getAltitude());
        newLoc.setAccuracy(location.getAccuracy());
        newLoc.setId(TeamIndex);


        Integer TeamIndexWeb=0;

        newLoc.setUpdatedAt(Calendar.getInstance().getTime().toString());

        TeamIndexWeb=TeamIndex+1;

        //send data to server
        Call<TeamLocation> call = apiService.updateTeamLocation(TeamIndexWeb, newLoc);

        String Phone_ID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        Float batteryPct = batteryInfo();

        try {
            //TextView TxtStatus = (TextView) findViewById(R.id.Status);
            String status = "N/A";
            String judge="Unknown";
            String team="Unknown";


            createFile(Phone_ID + ", " + newLoc.getUpdatedAt() + ", " + newLoc.getLatitude().toString() + ", " + newLoc.getLongitude().toString() +", " +batteryPct.toString() + ", " + newLoc.getAltitude()+ ", " + status+ ", " + judge+ ", " + team+", "+ location.getAccuracy()+", "+location.getSpeed() +", "+location.getBearing() ); //CreateFile -writes important data to a csv file
        } catch (Exception e) {
            createFile(Phone_ID + ", " + newLoc.getUpdatedAt() + ", " + newLoc.getLatitude().toString() + ", " + newLoc.getLongitude().toString() +", "+ batteryPct.toString()+ ", 0, N/A, N/A, N/A, 0, 0");
        }

        // Send data to Shane's server
        call.enqueue(new Callback<TeamLocation>() {
            @Override /*If you get a response, do this*/
            public void onResponse(Call<TeamLocation> call, Response<TeamLocation> response) {
                int statusCode = response.code();
                TeamLocation receivedLoc = response.body();
            }

            @Override /*if you get an error, do this*/
            public void onFailure(Call<TeamLocation> call, Throwable t) {
                // Log error here since request failed
            }

        });
        oldLoc=newLoc;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e(TAG, "onProviderEnabled: " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e(TAG, "onProviderDisabled: " + s);
    }

    public void createFile(String string) {

        String Phone_ID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        Calendar c = Calendar.getInstance();
        String today_string =  String.valueOf(c.get(Calendar.YEAR)).concat("-").concat(String.valueOf(c.get(Calendar.MONTH)+1)).concat("-").concat(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

        String filename= "SolarCarTracker ".concat(Phone_ID).concat(" ").concat(today_string) ;


        //String filename = "SolarCarTracker";
        FileOutputStream outputStream;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        final File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


        try {
            File myFile = new File(path+"/"+filename+".txt");

            if(!myDir.exists()){
                myDir.mkdirs();//make the folders where we write folders
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
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();

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
