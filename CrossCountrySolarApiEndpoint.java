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
public interface CrossCountrySolarApiEndpoint {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("/teams.json")
    List<Team> getTeams();

    @POST("/teams.json")
    Call<Team> createTeam(@Body Team team);

    @GET("/teams/{id}.json")
    Call<Team> getTeam(@Path("id") int teamId);

    @PUT("/teams/{id}.json")
    Call<Team> updateTeam(@Path("id") int teamId, @Body Team team);


    @GET("/team_locations.json")
    List<TeamLocation> getTeamLocations();

    @POST("/team_locations.json")
    Call<TeamLocation> createTeamLocation(@Body TeamLocation team_location);

    @GET("/team_locations/{id}.json")
    Call<TeamLocation> getTeamLocation(@Path("id") int teamId);

    @PUT("/team_locations/{id}.json")
    Call<TeamLocation> updateTeamLocation(@Path("id") int teamId, @Body TeamLocation team_location);

}
