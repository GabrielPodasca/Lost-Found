package com.cg.lostfoundapp.utils;

import android.content.Context;
import android.location.LocationManager;

import com.cg.lostfoundapp.model.PlacesAutocomplete;
import com.cg.lostfoundapp.model.PlacesDetails;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Gabi on 9/17/2016.
 */
public class PlacesUtils {

    public static final LatLngBounds BOUNDS_ROMANIA = new LatLngBounds(
            new LatLng(44.019718, 19.948352), new LatLng(48.456215, 29.584792));

    public static final int SEARCH_THRESHOLD = 4;

    public static final int PLACE_PICKER_REQUEST = 1;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";

    private static final String VALID_REQUEST = "OK";

    private static final String COUNTRY_RESTRICTION = "country:ro";

    private static final String API_KEY = "AIzaSyArkayO3hO12XvnQKDHUiGGShOVbgOrpTw";


    private static final class SingletonHolder {
        private static final PlacesUtils SINGLETON = new PlacesUtils();
    }

    public static PlacesUtils getInstance() {
        return SingletonHolder.SINGLETON;
    }


    public ArrayList<PlacesAutocomplete> autocomplete(String input) {

        ArrayList<PlacesAutocomplete> resultList = null;

        try {

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=address");
            sb.append("&components=" + COUNTRY_RESTRICTION);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            JSONObject jsonObj = queryAPI(sb.toString());

            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(new PlacesAutocomplete(
                         predsJsonArray.getJSONObject(i).getString("place_id")
                        ,predsJsonArray.getJSONObject(i).getString("description")
                )
                        );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public PlacesDetails getDetails(PlacesAutocomplete placesAutocomplete) {

        PlacesDetails placesDetails = null;

        try {

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&placeid=" + placesAutocomplete.placeId);

            JSONObject jsonObj = queryAPI(sb.toString());

            if (!jsonObj.getString("status").equals(VALID_REQUEST)) return placesDetails;

            JSONObject resultJsonObj = jsonObj.getJSONObject("result");
            JSONObject locationJsonObj = resultJsonObj.getJSONObject("geometry").getJSONObject("location");

            placesDetails = new PlacesDetails(
                    new LatLng(locationJsonObj.getDouble("lat"), locationJsonObj.getDouble("lng"))
                    ,resultJsonObj.getString("formatted_address")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return placesDetails;
    }


    private JSONObject queryAPI(String path) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        JSONObject jsonObj = new JSONObject();

        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

            jsonObj = new JSONObject(jsonResults.toString());

        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return jsonObj;
    }

    public boolean checkLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ) return false;
        return true;
    }
}
