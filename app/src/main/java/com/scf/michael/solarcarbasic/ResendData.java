package com.scf.michael.solarcarbasic;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import android.content.Context;

import com.scf.michael.solarcarbasic.api.ClosedTrackSolarApiEndpoint;
import com.scf.michael.solarcarbasic.api.ServiceGenerator;
import com.scf.michael.solarcarbasic.api.TeamLocation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Michael on 7/16/2017.
 */

public class ResendData extends Service{
    private Context mContext;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate(){

        checkLogs(getApplicationContext());

        //Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_SHORT).show();
    }
    private ClosedTrackSolarApiEndpoint apiService;

    public void checkLogs(Context context){
        Toast.makeText(context, "here", Toast.LENGTH_SHORT).show();
        //final TeamLocation newLoc = new TeamLocation();
        //final Team newTeam = new Team();
        //Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
        int tmpint=1;
        int senttoservercnt=0;  //we only want to send the first 700 items at a time to prevent overloading the app with stuff to do
        Toast.makeText(context, "starting sending", Toast.LENGTH_SHORT).show();
        TeamLocation newLoc = TeamLocation.findById(TeamLocation.class,1);
        //while (newLoc.getUpdatedAt() != null) {
        while (newLoc.getUpdatedAt() != null && senttoservercnt<2) {
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
                Toast.makeText(context, "fail!", Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(context, "no created time", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(context, newLoc.getUpdatedAt(), Toast.LENGTH_SHORT).show();
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
