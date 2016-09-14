package com.cg.lostfoundapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.lostfoundapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder> implements Filterable {

    //private static final String TAG = "PlacesAutoCompleteAdapter";
    private ArrayList<PlaceAutocomplete> resultList;
    private GoogleApiClient googleApiClient;
    private LatLngBounds bounds;
    private AutocompleteFilter placeFilter;

    private Context context;
    private int layout;

    public PlacesAutoCompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient,
                                     LatLngBounds bounds, AutocompleteFilter filter){
        this.context = context;
        layout = resource;
        this.googleApiClient = googleApiClient;
        this.bounds = bounds;
        placeFilter = filter;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    resultList = getAutocomplete(constraint);
                    if (resultList != null) {
                        results.values = resultList;
                        results.count = resultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        if (googleApiClient.isConnected()) {
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi.getAutocompletePredictions(
                            googleApiClient, constraint.toString(), bounds, placeFilter
                    );

            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);

            final Status STATUS = autocompletePredictions.getStatus();
            if (!STATUS.isSuccess()) {
                Toast.makeText(context,"Error contacting API: " + STATUS.toString(),Toast.LENGTH_SHORT).show();
                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),prediction.getDescription()));
            }

            autocompletePredictions.release();

            return resultList;
        }

        return null;
    }

    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, viewGroup, false);
        PredictionHolder predictionHolder = new PredictionHolder(convertView);
        return predictionHolder;
    }

    @Override
    public void onBindViewHolder(PredictionHolder predictionHolder, final int i) {
        predictionHolder.prediction.setText(resultList.get(i).description);
    }

    @Override
    public int getItemCount() {
        if(resultList != null) {
            return resultList.size();
        }else {
            return 0;
        }
    }

    public PlaceAutocomplete getItem(int position) {
        return resultList.get(position);
    }

    public class PredictionHolder extends RecyclerView.ViewHolder {
        private TextView prediction;
        private RelativeLayout row;
        public PredictionHolder(View itemView){
            super(itemView);
            prediction = (TextView) itemView.findViewById(R.id.address);
            row =(RelativeLayout)itemView.findViewById(R.id.predictedRow);
        }

    }

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

}
