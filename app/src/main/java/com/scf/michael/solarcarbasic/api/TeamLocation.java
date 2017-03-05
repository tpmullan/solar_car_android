package com.scf.michael.solarcarbasic.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;
import com.orm.SugarRecord;

@Generated("org.jsonschema2pojo")
public class TeamLocation extends SugarRecord {
    @SerializedName("id")
    @Expose
    private Integer remote_id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat")
    @Expose
    private Double latitude;
    @SerializedName("lon")
    @Expose
    private Double longitude;
    @SerializedName("team")
    @Expose
    private Integer team;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("altitude")
    @Expose
    private Double altitude;
    @SerializedName("accuracy")
    @Expose
    private Float accuracy;


    public TeamLocation() {}
    /**
     *
     * @return
     * The remote_id
     */
    public Integer getRemoteId() {
        return remote_id;
    }
    /**
     *
     * @param id
     * The remote_id
     */
    public void setRemoteId(Integer id) {
        this.remote_id = id;
    }
    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }
    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     *
     * @return
     * The latitude
     */
    public Double getLatitude() {
        return latitude;
    }
    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    /**
     *
     * @return
     * The longitude
     */
    public Double getLongitude() {
        return longitude;
    }
    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    /**
     *
     * @return
     * The team
     */
    public Integer getTeam() {
        return team;
    }
    /**
     *
     * @param team
     * The team_id
     */
    public void setTeam(Integer team) {
        this.team = team;
    }
    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }
    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    /**
     *
     * @return
     * The altitude
     */
    public Double getAltitude() {
        return altitude;
    }
    /**
     *
     * @param altitude
     * The altitude
     */
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}