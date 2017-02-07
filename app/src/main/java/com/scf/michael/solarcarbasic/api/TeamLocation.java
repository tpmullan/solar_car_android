package com.scf.michael.solarcarbasic.api;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;


@Generated("org.jsonschema2pojo")
@Table(name = "TeamLocations")
public class TeamLocation extends Model {
    @Column(name = "remote_id")
    @SerializedName("remote_id")
    @Expose
    private Integer remote_id;
    @Column(name = "address")
    @SerializedName("address")
    @Expose
    private String address;
    @Column(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @Column(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @Column(name = "team_id")
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @Column(name = "created_at")
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @Column(name = "updated_at")
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @Column(name = "altitude")
    @SerializedName("altitude")
    @Expose
    private Double altitude;
    @Column(name = "accuracy")
    @SerializedName("accuracy")
    @Expose
    private Float accuracy;
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