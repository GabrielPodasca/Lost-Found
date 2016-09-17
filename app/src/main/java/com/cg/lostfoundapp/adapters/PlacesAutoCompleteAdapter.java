package com.cg.lostfoundapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cg.lostfoundapp.model.PlacesAutocomplete;
import com.cg.lostfoundapp.utils.PlacesUtils;

import java.util.ArrayList;

public class PlacesAutoCompleteAdapter
        extends ArrayAdapter<PlacesAutocomplete> implements Filterable {
    
    /**
     * Current results returned by this adapter.
     */
    private ArrayList<PlacesAutocomplete> resultList = new ArrayList<>();

    private Filter filter;


    public PlacesAutoCompleteAdapter(Context context) {
        super(context, 0);
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public PlacesAutocomplete getItem(int position) {
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


                        // Query the autocomplete API for the (constraint) search string.
                        ArrayList<PlacesAutocomplete> placesList = PlacesUtils.getInstance().autocomplete(constraint.toString());
                        if (placesList != null) {

                            resultList.clear();
                            for (PlacesAutocomplete places : placesList) {
                                resultList.add(places);
                            }
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
                    return resultValue == null ? "" : ((PlacesAutocomplete) resultValue).description;
                }
            };

        }
        return filter;
    }



    /** static cache item view */
    private static class ViewHolder {
        TextView text;
    }
}