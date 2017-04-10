package com.whaletail.app;

/**
 * @author Whaletail
 */
public class County {
    private long id;
    private int status;
    private String name;
    private double lng;
    private double lat;

    public County(long id, int status, String name, double lng, double lat) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.lng = lng;
        this.lat = lat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "County{" +
                "id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
