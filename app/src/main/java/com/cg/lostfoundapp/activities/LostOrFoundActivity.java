package com.cg.lostfoundapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cg.lostfoundapp.R;
import com.cg.lostfoundapp.adapters.PlacesAutoCompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class LostOrFoundActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private EditText iFoundEditText;
    private EditText whenLostText;
    private EditText descriptionLostText;
    private Button postLostButton;
    private String itemStatus;

    protected GoogleApiClient googleApiClient;
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(43.992815, 19.863281), new LatLng(48.195387, 30.761719));

    private static final int SEARCH_THRESHOLD = 4;

    private AutoCompleteTextView autocompletePlaces;

    private PlacesAutoCompleteAdapter autoCompleteAdapter;
    ImageView deleteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();

        setContentView(R.layout.activity_lost_or_found);

        initComponents();

        deleteImage.setOnClickListener(this);





    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    private void initComponents(){

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemStatus = extras.getString("itemStatus");
        }


        iFoundEditText = (EditText) findViewById(R.id.iFoundEditText);
        initAutocomplete();
        whenLostText = (EditText) findViewById(R.id.whenTxt);
        descriptionLostText = (EditText) findViewById(R.id.descriptionText);
        postLostButton = (Button) findViewById(R.id.postButton);



    }

    private void initAutocomplete() {
        autocompletePlaces = (AutoCompleteTextView)findViewById(R.id.autocompletePlaces);
        deleteImage=(ImageView)findViewById(R.id.cross);

        AutocompleteFilter.Builder builder = new AutocompleteFilter.Builder();
        builder.setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS);
        AutocompleteFilter autocompleteFilter = builder.build();

        autoCompleteAdapter =  new PlacesAutoCompleteAdapter(
                this, googleApiClient, BOUNDS, autocompleteFilter
        );

        autocompletePlaces.setAdapter(autoCompleteAdapter);
        autocompletePlaces.setThreshold(SEARCH_THRESHOLD);
    }


    @Override
    public void onConnected(Bundle bundle){
        Toast.makeText(this,"GoogleAPI Conected!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Google API not connected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v == deleteImage){
            autocompletePlaces.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()){
            googleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
    }
}
