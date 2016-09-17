package com.cg.lostfoundapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cg.lostfoundapp.R;
import com.cg.lostfoundapp.adapters.PlacesAutoCompleteAdapter;


public class LostOrFoundActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText iFoundEditText;
    private EditText whenLostText;
    private EditText descriptionLostText;
    private Button postLostButton;
    private String itemStatus;

    private static final int SEARCH_THRESHOLD = 4;

    private AutoCompleteTextView autocompletePlaces;

    private PlacesAutoCompleteAdapter autoCompleteAdapter;
    ImageView deleteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lost_or_found);

        initComponents();

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




        autoCompleteAdapter =  new PlacesAutoCompleteAdapter(this);

        autocompletePlaces.setAdapter(autoCompleteAdapter);
        autocompletePlaces.setThreshold(SEARCH_THRESHOLD);

        deleteImage=(ImageView)findViewById(R.id.cross);
        deleteImage.setOnClickListener(this);
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
