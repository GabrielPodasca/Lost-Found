package com.cg.lostfoundapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cg.lostfoundapp.R;
import com.cg.lostfoundapp.adapters.PlacesAutoCompleteAdapter;
import com.cg.lostfoundapp.listeners.RecyclerItemClickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
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
    private static final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(43,20),new LatLng(49,30));

    private EditText autocompletePlaces;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PlacesAutoCompleteAdapter autoCompleteAdapter;
    ImageView deleteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_lost_or_found);

        initComponents();

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(autoCompleteAdapter);

        deleteImage.setOnClickListener(this);

        autocompletePlaces.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (!s.toString().equals("") && googleApiClient.isConnected()) {
                    autoCompleteAdapter.getFilter().filter(s.toString());
                }else if(!googleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(), "Google API not connected",Toast.LENGTH_SHORT).show();
                }
            }

                public void beforeTextChanged(CharSequence s, int start, int count, int after){}

                public void afterTextChanged(Editable s){}
            });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = autoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){
                                    //handle click on certain place
                                    String placeName = places.get(0).getName().toString();
                                    String placeAdress = places.get(0).getAddress().toString();
                                    autocompletePlaces.setText(placeName+", "+placeAdress);
                                }else{
                                    Toast.makeText(getApplicationContext(),"OOPs!!! Something went wrong...",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
        );

    }

    private void initComponents(){
        iFoundEditText = (EditText) findViewById(R.id.iFoundEditText);
        whenLostText = (EditText) findViewById(R.id.whenTxt);
        descriptionLostText = (EditText) findViewById(R.id.descriptionText);
        postLostButton = (Button) findViewById(R.id.postButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemStatus = extras.getString("itemStatus");
        }

        autocompletePlaces = (EditText)findViewById(R.id.autocompletePlaces);
        deleteImage=(ImageView)findViewById(R.id.cross);
        autoCompleteAdapter =  new PlacesAutoCompleteAdapter(this, R.layout.searchview_adapter, googleApiClient, BOUNDS, null);
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
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
