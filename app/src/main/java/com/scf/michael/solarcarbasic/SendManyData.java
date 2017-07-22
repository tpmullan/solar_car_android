package com.scf.michael.solarcarbasic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.scf.michael.solarcarbasic.api.ClosedTrackSolarApiEndpoint;
import com.scf.michael.solarcarbasic.api.ServiceGenerator;
import com.scf.michael.solarcarbasic.api.StatusMessage;
import com.scf.michael.solarcarbasic.api.TeamLocation;
import com.scf.michael.solarcarbasic.api.TeamLocationsWrapper;

import java.util.Enumeration;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Michael on 7/20/2017.
 */

public class SendManyData {

    private Handler sendMassDataHandler;
    private Runnable runnableCodeMassData;
    private Boolean shouldRunAgain;

    public SendManyData(Handler myHandler, Runnable myRun) {
        this.runnableCodeMassData = myRun;
        this.sendMassDataHandler = myHandler;
        this.shouldRunAgain = true;
    }

    public void SendData(String Phone_ID){

        ClosedTrackSolarApiEndpoint apiService = ServiceGenerator.createService(ClosedTrackSolarApiEndpoint.class);
        final TeamLocationsWrapper newLocWrapper = new TeamLocationsWrapper();
        //final List<Integer> RemoteIDsSent= TeamLocation.findWithQuery(Integer.class,"Select id from team_location where remoteid is null");
        //final List<TeamLocation> RemoteIDsSent= TeamLocation.findWithQuery(TeamLocation.class,"Select id from team_location where remoteid is null");
        //final List<TeamLocation> TmpLocation = TeamLocation.findWithQuery(TeamLocation.class,"Select * from team_location where remoteid is null");
        final List<TeamLocation> tmpLocations = Select.from(TeamLocation.class).limit("2000").where(Condition.prop("remoteid").eq(null)).list();
        //newLocWrapper.setTeamLocations(TeamLocation.findWithQuery(TeamLocation.class,"Select * from team_location where remoteid is null limit 10"));
        //newLocWrapper.setTeamLocations(Select.from(TeamLocation.class).where(Condition.prop("id").eq(RemoteIDsSent)).list());

        newLocWrapper.setTeamLocations(tmpLocations);
        newLocWrapper.setUuid(Phone_ID);
        //newLocWrapper.setUuid("e3a0a9e86fd000b7");

        // Send data to Shane's server
        Call<StatusMessage> call = apiService.createTeamLocations(newLocWrapper);
        call.enqueue(new Callback<StatusMessage>() {
            @Override /*If you get a response, do this*/
            public void onResponse(Call<StatusMessage> call, Response<StatusMessage> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    StatusMessage receivedMessage = response.body();
                    //TeamLocation.executeQuery("Update Remote_Id =1 on TeamLocation Where ID In (?)", TextUtils.join(",",RemoteIDsSent));
                    //TeamLocation.executeQuery("update team_location set remoteid=1 Where id In (?)", TextUtils.join(",",RemoteIDsSent));
                    for (TeamLocation loc:tmpLocations) {
                        loc.setRemoteId(69);
                        loc.save();
                    }
                } else {
                    //Toast.makeText(mContext.getApplicationContext(), "Failed POST!", Toast.LENGTH_LONG).show();
                }
                runAgain();

            }

            @Override
            public void onFailure(Call<StatusMessage> call, Throwable t) {
                runAgain();
            }

        });
    };

    private void runAgain(){
        if (shouldRunAgain) {
            sendMassDataHandler.postDelayed(runnableCodeMassData, 30000);
        }
    }

    public Boolean getShouldRunAgain() {
        return shouldRunAgain;
    }

    public void setShouldRunAgain(Boolean shouldRunAgain) {
        this.shouldRunAgain = shouldRunAgain;
    }

}

