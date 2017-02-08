package com.scf.michael.solarcarbasic.api;

import com.scf.michael.solarcarbasic.api.Team;
import com.scf.michael.solarcarbasic.api.TeamLocation;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
/**
 * Created by tom on 12/1/15.
 */
public interface ClosedTrackSolarApiEndpoint {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter
    @GET("teams")
    List<Team> getTeams();
    @POST("teams")
    Call<Team> createTeam(@Body Team team);
    @GET("teams/{id}")
    Call<Team> getTeam(@Path("id") int teamId);
    @PUT("teams/{id}")
    Call<Team> updateTeam(@Path("id") int teamId, @Body Team team);
    @GET("team_locations")
    Call<List<TeamLocation>> getTeamLocations();
    @POST("team_locations")
    Call<TeamLocation> createTeamLocation(@Body TeamLocation team_location);
    @GET("team_locations/{id}")
    Call<TeamLocation> getTeamLocation(@Path("id") int teamId);
    @PUT("team_locations/{id}")
    Call<TeamLocation> updateTeamLocation(@Path("id") int teamId, @Body TeamLocation team_location);
    @POST("auth")
    Call<Auth> login(@Body Auth user);
}