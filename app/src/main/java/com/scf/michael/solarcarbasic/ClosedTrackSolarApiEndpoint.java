package com.scf.michael.solarcarbasic;

import java.util.List;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
/**
 * Created by tom on 12/1/15.
 */
public interface ClosedTrackSolarApiEndpoint {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter
    @GET("/teams")
    List<Team> getTeams();
    @POST("/teams")
    Call<Team> createTeam(@Body Team team);
    @GET("/teams/{id}")
    Call<Team> getTeam(@Path("id") int teamId);
    @PUT("/teams/{id}")
    Call<Team> updateTeam(@Path("id") int teamId, @Body Team team);
    @GET("/team_locations")
    List<TeamLocation> getTeamLocations();
    @POST("/team_locations")
    Call<TeamLocation> createTeamLocation(@Body TeamLocation team_location);
    @GET("/team_locations/{id}")
    Call<TeamLocation> getTeamLocation(@Path("id") int teamId);
    @PUT("/team_locations/{id}")
    Call<TeamLocation> updateTeamLocation(@Path("id") int teamId, @Body TeamLocation team_location);
}