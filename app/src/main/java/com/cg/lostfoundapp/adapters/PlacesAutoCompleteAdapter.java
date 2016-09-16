package com.cg.lostfoundapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlacesAutoCompleteAdapter
        extends ArrayAdapter<PlacesAutoCompleteAdapter.PlaceAutocomplete> implements Filterable {

    private static final String TAG = "PlaceAutocomplete";
    /**
     * Current results returned by this adapter.
     */
    private ArrayList<PlaceAutocomplete> resultList = new ArrayList<>();

    /**
     * Handles autocomplete requests.
     */
    private GoogleApiClient googleApiClient;

    /**
     * The bounds used for Places Geo Data autocomplete API requests.
     */
    private LatLngBounds bounds;

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    private AutocompleteFilter autocompleteFilter;


    private Filter filter;


    public PlacesAutoCompleteAdapter(Context context,  GoogleApiClient googleApiClient,
                                     LatLngBounds bounds, AutocompleteFilter autocompleteFilter) {
        super(context, 0);
        this.googleApiClient = googleApiClient;
        this.bounds = bounds;
        this.autocompleteFilter = autocompleteFilter;
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        return resultList.get(position);
    }


    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        ViewHolder holder;

        if( convertView == null ) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from( getContext() ).inflate( android.R.layout.simple_list_item_1, parent, false  );
            holder.text = (TextView) convertView.findViewById( android.R.id.text1 );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText( getItem( position ).description );

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
             filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    // Skip the autocomplete query if no constraints are given.
                    if (constraint != null) {

                        if (googleApiClient == null || !googleApiClient.isConnected()) {
                            Log.e(TAG, "Google API client is not connected for autocomplete query.");
                            return null;
                        }


                        // Query the autocomplete API for the (constraint) search string.
                        getAutocomplete(constraint.toString());
                        if (resultList != null) {
                            // The API successfully returned results.
                            results.values = resultList;
                            results.count = resultList.size();
                        }
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        // The API returned at least one result, update the data.
                        notifyDataSetChanged();
                    } else {
                        // The API did not return any results, invalidate the data set.
                        notifyDataSetInvalidated();
                    }
                }
                @Override
                public CharSequence convertResultToString(final Object resultValue) {
                    return resultValue == null ? "" : ((PlaceAutocomplete) resultValue).description;
                }
            };

        }
        return filter;
    }

    private void getAutocomplete(String constraint) {

        Log.i(TAG, "Starting autocomplete query for: " + constraint);


        // Submit the query to the autocomplete API and retrieve a PendingResult that will
        // contain the results when the query completes.

        PendingResult<AutocompletePredictionBuffer> results =
                Places.GeoDataApi
                        .getAutocompletePredictions(googleApiClient, constraint,
                                bounds, autocompleteFilter);

        //Block and wait for at most 60s
        // for a result from the API.
        AutocompletePredictionBuffer autocompletePredictions = results
                .await(60, TimeUnit.SECONDS);

        // Confirm that the query completed successfully, otherwise return null
        final Status status = autocompletePredictions.getStatus();
        if (autocompletePredictions == null || !status.isSuccess()) {
            Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
            autocompletePredictions.release();
            return;
        }

        Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
                + " predictions.");

        // Copy the results into our own data structure, because we can't hold onto the buffer.
        // AutocompletePrediction objects encapsulate the API response (place ID and description).

        resultList.clear();

        for( AutocompletePrediction prediction : autocompletePredictions ) {
            //Add as a new item to avoid IllegalArgumentsException when buffer is released
            resultList.add( new PlaceAutocomplete( prediction.getPlaceId(), prediction.getFullText(null) ) );
        }

        // Release the buffer now that all data has been copied.
        autocompletePredictions.release();

        return;

    }

    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }


    /** static cache item view */
    private static class ViewHolder {
        TextView text;
    }
}