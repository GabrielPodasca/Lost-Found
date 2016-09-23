package com.cg.lostfoundapp.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.cg.lostfoundapp.model.Item;
import com.cg.lostfoundapp.model.PlacesAutocomplete;
import com.cg.lostfoundapp.model.PlacesDetails;
import com.cg.lostfoundapp.model.User;
import com.cg.lostfoundapp.service.ItemWSController;
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
import java.util.Date;


public class LostOrFoundActivity extends AppCompatActivity {



    private DelayedAutocompleteTextView autocompletePlaces;

    private PlacesAutoCompleteAdapter autoCompleteAdapter;


    private EditText iFoundEditText;
    private Button btnPickLocation;
    private Button btnHere;
    private EditText descriptionText;
    private Button postButton;
    private EditText txtSetDateAndTime;
    private ImageView deleteImage;
    private View progressOverlay;
    private View progressOverlayBig;

    private String itemName, itemDescription;
    private String itemType;
    private int year, month, day, hour, minute;
    private User user;
    private PlacesDetails placesDetails;




    private GoogleApiClient googleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();

        setContentView(R.layout.activity_lost_or_found);

        initComponents();

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
            itemType = extras.getString("itemType");
            user = (User) extras.getSerializable("user");
            System.out.println(user);
        } else {
            finish();
        }



        iFoundEditText = (EditText) findViewById(R.id.iFoundEditText);
        initPlaces();
        initTime();
        descriptionText = (EditText) findViewById(R.id.descriptionText);
        postButton = (Button) findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postItem();
            }
        });
        progressOverlay = findViewById(R.id.progress_overlay);
        progressOverlayBig = findViewById(R.id.progress_overlay_big);

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
                ViewUtils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(PlacesUtils.BOUNDS_ROMANIA);
                    Intent intent = intentBuilder.build(LostOrFoundActivity.this);
                    startActivityForResult(intent, PlacesUtils.PLACE_PICKER_REQUEST);

                } catch (Exception e) {
                    ViewUtils.animateView(progressOverlay, View.GONE, 0, 200);
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
                    ViewUtils.animateView(progressOverlay, View.GONE, 0, 200);
                    Toast.makeText(LostOrFoundActivity.this, "Please enable Location on device!", Toast.LENGTH_SHORT).show();
                    return;
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

    private void initTime() {

        txtSetDateAndTime = (EditText) findViewById(R.id.txtSetDateAndTime);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PlacesUtils.PLACE_PICKER_REQUEST) {

            ViewUtils.animateView(progressOverlay, View.GONE, 0, 200);

            if (resultCode == Activity.RESULT_OK) {
                final Place place = PlacePicker.getPlace(data,this);

                String address = place.getAddress().toString();

                autocompletePlaces.setSearchEnabled(false);
                autocompletePlaces.setText(address);
                autocompletePlaces.setSearchEnabled(true);

                placesDetails = new PlacesDetails(place.getLatLng(), address);
            }



        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void postItem() {

        itemName = iFoundEditText.getText().toString();
        itemDescription = descriptionText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Date when = calendar.getTime();

        try {
            validate();
        }
        catch(Exception e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }


        Item item = new Item(itemName, itemDescription, placesDetails.latLng.latitude,
                placesDetails.latLng.longitude, placesDetails.address, when, itemType, user);

        new ItemAddAsyncTask().execute(item);

    }

    private void validate() throws Exception{


        if (itemName == null || itemName.trim().isEmpty()) {
            throw new Exception("Please tell us what you lost/found!");
        }
        if (placesDetails == null) {
            throw new Exception("Please tell us where you lost/found the item!");
        }
        if (year <= 0 || month <= 0 || day <= 0 || hour <= 0 || minute <= 0) {
            throw new Exception("Please tell us when you lost/found the item!");
        }
        if (itemDescription == null || itemDescription.trim().isEmpty()) {
            throw new Exception("Please describe the item!");
        }

    }

    private void startMainActivity(){
        Intent intent = new Intent(LostOrFoundActivity.this, MainActivity.class);
        intent.putExtra("user",this.user);
        startActivity(intent);
        finish();
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

    private class ItemAddAsyncTask extends AsyncTask<Item, Void, String> {

        @Override
        protected void onPreExecute() {
            ViewUtils.animateView(progressOverlayBig, View.VISIBLE, 0.4f, 200);
        }

        @Override
        protected String doInBackground(Item... params) {
            return ItemWSController.getInstance().add(params[0]);
        }

        @Override
        protected void onPostExecute(String response) {

            ViewUtils.animateView(progressOverlayBig, View.GONE, 0, 200);

            if (response!=null) {
                Toast.makeText(LostOrFoundActivity.this, response, Toast.LENGTH_SHORT).show();
                if (response.equals("OK")) {
                    startMainActivity();
                }
            }
        }
    }

}
