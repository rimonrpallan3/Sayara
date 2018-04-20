package com.voyager.sayara.landingpage.model.geogetpath;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rimon on 17-02-2018.
 */

public class GetPaths {

    @SerializedName("geocoded_waypoints")
    List<GeocodedWaypoint> geocodedWaypointses;
    List<Route> routes;
    String status;

    public List<GeocodedWaypoint> getGeocodedWaypointses() {
        return geocodedWaypointses;
    }

    public void setGeocodedWaypointses(List<GeocodedWaypoint> geocodedWaypointses) {
        this.geocodedWaypointses = geocodedWaypointses;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
