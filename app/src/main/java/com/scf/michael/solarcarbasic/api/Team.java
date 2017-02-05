package com.scf.michael.solarcarbasic.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.annotation.Generated;
@Generated("org.jsonschema2pojo")
public class Team {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }
    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * @return The name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return The year
     */
    public String getYear() {
        return year;
    }
    /**
     * @param year The year
     */
    public void setYear(String year) {
        this.year = year;
    }
    /**
     * @return The number
     */
    public Integer getNumber() {
        return number;
    }
    /**
     * @param number The number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }
    /**
     * @return The school
     */
    public String getSchool() {
        return school;
    }
    /**
     * @param school The school
     */
    public void setSchool(String school) {
        this.school = school;
    }
    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }
    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}