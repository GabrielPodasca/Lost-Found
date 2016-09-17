package com.cg.lostfoundapp.model;

/**
 * Created by Gabi on 9/17/2016.
 */
public class PlacesAutocomplete {

    public CharSequence placeId;
    public CharSequence description;

    public PlacesAutocomplete(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }

    @Override
    public String toString() {
        return description.toString();
    }
}
