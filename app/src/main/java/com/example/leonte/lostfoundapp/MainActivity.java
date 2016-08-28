package com.example.leonte.lostfoundapp;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView showItemsList;
    private ArrayAdapter<String> adapter;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeaderView;
    private TextView usernameMainAppText;
    private TextView phoneNumberMainAppText;
    private String username;
    private String password;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            password = extras.getString("password");
            phoneNumber = extras.getString("phoneNumber");
        }
        Toast.makeText(MainActivity.this,username,Toast.LENGTH_LONG).show();
        phoneNumberMainAppText.setText(phoneNumber);
        usernameMainAppText.setText(username);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        showItemsList.setAdapter(adapter);
        for(int i=0;i<10;i++){
            adapter.add(i+"");
        }
        adapter.notifyDataSetChanged();

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initComponents(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        showItemsList = (ListView) findViewById(R.id.showItemsList);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        usernameMainAppText = (TextView) navHeaderView.findViewById(R.id.usernameMainAppText);
        phoneNumberMainAppText = (TextView) navHeaderView.findViewById(R.id.phoneNumberMainAppText);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            Intent intent = new Intent(MainActivity.this,OptionsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.navMyItems){
            Toast.makeText(MainActivity.this,"MY ITEMS LIST ON",Toast.LENGTH_LONG).show();
        }else if(id == R.id.navSwitchLists){
            String title = item.getTitle().toString();
            if(title.equals("Switch List(LOST ON)")){
                title = "Switch List(FOUND ON)";
                Toast.makeText(MainActivity.this,"FOUND LIST ON",Toast.LENGTH_LONG).show();
            }else{
                title = "Switch List(LOST ON)";
                Toast.makeText(MainActivity.this,"LOST LIST ON",Toast.LENGTH_LONG).show();
            }
            item.setTitle(title);
        }else if(id == R.id.navLogout){
            finish();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}