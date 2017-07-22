package com.scf.michael.solarcarbasic.api;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamLocationsWrapper {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("team_locations")
    @Expose
    private List<TeamLocation> teamLocations = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<TeamLocation> getTeamLocations() {
        return teamLocations;
    }

    public void setTeamLocations(List<TeamLocation> teamLocations) {
        this.teamLocations = teamLocations;
    }


}
