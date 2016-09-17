package com.cg.lostfoundapp.utils;

import com.cg.lostfoundapp.model.PlacesAutocomplete;

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


    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

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

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=address");
            sb.append("&components=" + COUNTRY_RESTRICTION);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        }catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            System.out.println(jsonObj);
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
}
