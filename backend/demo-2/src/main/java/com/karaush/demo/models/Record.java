package com.karaush.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.karaush.demo.validators.annotations.LocationConstraint;
import com.karaush.demo.validators.annotations.TemperatureConstraint;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "created", nullable = false)
    private Date created;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @LocationConstraint(mode = "latitude")
    @Column(name = "latitude", nullable = false)
    private String latitude;

    @LocationConstraint(mode = "longitude")
    @Column(name = "longitude", nullable = false)
    private String longitude;

    //in celsius
    @TemperatureConstraint
    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Column(name = "city", nullable = true)
    private String city;

    public Record(){}

    public Record(String latitude, String longitude, double temperature){
        this.longitude = longitude;
        this.latitude = latitude;
        this.temperature = temperature;
        this.city = " - ";
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Record(String latitude, String longitude, double temperature, String city){
        this.longitude = longitude;
        this.latitude = latitude;
        this.temperature = temperature;
        this.city = city;
    }

    public String getLongitude(){
        return longitude;
    }

    public String getLatitude(){
        return latitude;
    }

    public double getTemperature() { return temperature;}

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
