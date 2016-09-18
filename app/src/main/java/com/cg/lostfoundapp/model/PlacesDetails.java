package com.cg.lostfoundapp.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Gabi on 9/17/2016.
 */
public class PlacesDetails {

    public LatLng latLng;

    public String address;


    public PlacesDetails(LatLng latLng, String address) {
        this.latLng = latLng;
        this.address = address;
    }

    @Override
    public String toString() {
        return latLng.toString() + " " + address + " ";
    }
}
