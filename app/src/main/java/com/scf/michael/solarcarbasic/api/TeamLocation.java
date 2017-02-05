package com.scf.michael.solarcarbasic.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;
@Generated("org.jsonschema2pojo")
public class TeamLocation {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
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
    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }
    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
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
     * The teamId
     */
    public Integer getTeamId() {
        return teamId;
    }
    /**
     *
     * @param teamId
     * The team_id
     */
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
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