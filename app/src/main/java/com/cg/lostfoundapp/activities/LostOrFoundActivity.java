package com.cg.lostfoundapp.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cg.lostfoundapp.R;
import com.cg.lostfoundapp.adapters.PlacesAutoCompleteAdapter;
import com.cg.lostfoundapp.fragments.DatePickerFragment;
import com.cg.lostfoundapp.fragments.TimePickerFragment;
import com.cg.lostfoundapp.model.PlacesAutocomplete;
import com.cg.lostfoundapp.model.PlacesDetails;
import com.cg.lostfoundapp.utils.PlacesUtils;
import com.cg.lostfoundapp.utils.ViewUtils;
import com.cg.lostfoundapp.widget.DelayedAutocompleteTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlacesStatusCodes;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;


public class LostOrFoundActivity extends AppCompatActivity {



    private DelayedAutocompleteTextView autocompletePlaces;
    private Button btnPickLocation;
    private Button btnHere;

    private PlacesAutoCompleteAdapter autoCompleteAdapter;
    ImageView deleteImage;

    private EditText iFoundEditText;
    private EditText whenLostText;
    private EditText descriptionLostText;
    private Button postLostButton;
    private String itemStatus;
    private EditText txtSetDateAndTime;

    private int year, month, day, hour, minute;

    View progressOverlay;



    private PlacesDetails placesDetails;

    private GoogleApiClient googleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();

        setContentView(R.layout.activity_lost_or_found);

        initComponents();

        txtSetDateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateFragment = new DatePickerFragment(){
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        year = y;
                        month = m + 1;
                        day = d;

                        DialogFragment timeFragment = new TimePickerFragment(){
                            @Override
                            public void onTimeSet(TimePicker view, int h, int m) {
                                if (isToday()){
                                    Calendar calendar = Calendar.getInstance();
                                    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                                    int currentMinute = calendar.get(Calendar.MINUTE);

                                    if(h > currentHour){
                                        hour = currentHour;
                                        minute = currentMinute;
                                    }

                                    if(h < currentHour){
                                        hour = h;
                                        minute = m;
                                    }

                                    if(h == currentHour){
                                        hour = currentHour;
                                        if(m > currentMinute){
                                            minute = currentMinute;
                                        }else{
                                            minute = m;
                                        }
                                    }
                                }else{
                                    hour = h;
                                    minute = m;
                                }

                                txtSetDateAndTime.setText(zeroFormat(day) + "-" +
                                        zeroFormat(month) + "-" +
                                        year + "   @   " +
                                        zeroFormat(hour) + " : " +
                                        zeroFormat(minute));
                            }
                        };
                        timeFragment.show(getFragmentManager(), "Time Picker");
                    }
                };
                dateFragment.show(getFragmentManager(), "Date Picker");
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                };
        GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                };
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

    }




    private void initComponents(){

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemStatus = extras.getString("itemStatus");
        }


        iFoundEditText = (EditText) findViewById(R.id.iFoundEditText);
        initPlaces();
        descriptionLostText = (EditText) findViewById(R.id.descriptionText);
        postLostButton = (Button) findViewById(R.id.postButton);

        txtSetDateAndTime = (EditText) findViewById(R.id.txtSetDateAndTime);


        progressOverlay = findViewById(R.id.progress_overlay);

    }

    private void initPlaces() {
        autocompletePlaces = (DelayedAutocompleteTextView) findViewById(R.id.autocompletePlaces);


        autoCompleteAdapter =  new PlacesAutoCompleteAdapter(this);

        autocompletePlaces.setAdapter(autoCompleteAdapter);
        autocompletePlaces.setThreshold(PlacesUtils.SEARCH_THRESHOLD);

        autocompletePlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlacesAutocomplete placesAutocomplete = autoCompleteAdapter.getItem(position);
                new PlacesDetailsAsyncTask().execute(placesAutocomplete);
            }
        });

        deleteImage=(ImageView)findViewById(R.id.cross);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autocompletePlaces.setText("");
            }
        });

        placesDetails = null;

        btnPickLocation = (Button) findViewById(R.id.btnPickLocation);
        btnHere = (Button) findViewById(R.id.btnHere);

        btnPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(PlacesUtils.BOUNDS_ROMANIA);
                    Intent intent = intentBuilder.build(LostOrFoundActivity.this);
                    startActivityForResult(intent, PlacesUtils.PLACE_PICKER_REQUEST);

                } catch (Exception e) {
                    Toast.makeText(LostOrFoundActivity.this, "Google Play services error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleApiClient!=null && googleApiClient.isConnected()) {

                    if (!PlacesUtils.getInstance().checkLocationEnabled(LostOrFoundActivity.this)) {
                        Toast.makeText(LostOrFoundActivity.this, "Please enable Location on device!", Toast.LENGTH_SHORT).show();
                    } else {

                        callPlaceDetectionApi();
                    }


                } else {
                    Toast.makeText(LostOrFoundActivity.this, "Google Play services error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callPlaceDetectionApi() throws SecurityException {

        ViewUtils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                if (likelyPlaces.getStatus().getStatusCode()!= PlacesStatusCodes.SUCCESS) {
                    Toast.makeText(LostOrFoundActivity.this, "Please enable Location on device!", Toast.LENGTH_SHORT).show();
                }

                float maxLikelihood = 0.0f, currentLikelihood;
                PlaceLikelihood selectedLikelihood = null;
                Place selectedPlace = null;

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    currentLikelihood = placeLikelihood.getLikelihood();
                    if (currentLikelihood > maxLikelihood) {
                        maxLikelihood = currentLikelihood;
                        selectedLikelihood = placeLikelihood;
                    }
                }
                if (selectedLikelihood!=null) {
                    selectedPlace = selectedLikelihood.getPlace();
                    selectedLikelihood = null;
                }


                if (selectedPlace!=null) {
                    String address = selectedPlace.getAddress().toString();
                    placesDetails = new PlacesDetails(selectedPlace.getLatLng(), address);
                    autocompletePlaces.setSearchEnabled(false);
                    autocompletePlaces.setText(address);
                    autocompletePlaces.setSearchEnabled(true);
                }

                likelyPlaces.release();

                ViewUtils.animateView(progressOverlay, View.GONE, 0, 200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PlacesUtils.PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(this, data);

            String address = place.getAddress().toString();

            autocompletePlaces.setSearchEnabled(false);
            autocompletePlaces.setText(address);
            autocompletePlaces.setSearchEnabled(true);

            placesDetails = new PlacesDetails(place.getLatLng(), address);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient!=null && !googleApiClient.isConnected() && !googleApiClient.isConnecting()){
            googleApiClient.connect();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(googleApiClient!=null && googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }

    }

    private class PlacesDetailsAsyncTask extends AsyncTask<PlacesAutocomplete, Void, PlacesDetails> {

        @Override
        protected PlacesDetails doInBackground(PlacesAutocomplete... params) {
            PlacesAutocomplete placesAutocomplete = params[0];
            return PlacesUtils.getInstance().getDetails(placesAutocomplete);
        }

        @Override
        protected void onPostExecute(PlacesDetails placesDetails) {
            if (placesDetails!=null) {
                LostOrFoundActivity.this.placesDetails = placesDetails;
            } else {
                Toast.makeText(LostOrFoundActivity.this, "Google Play services error!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public boolean isToday(){
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        if((year == y)&&(month == (m+1))&&(day == d)){
            return true;
        }else{
            return false;
        }
    }

    public String zeroFormat(int x){
        if (x <=9){
            return "0"+x;
        }else{
            return String.valueOf(x);
        }
    }

}
