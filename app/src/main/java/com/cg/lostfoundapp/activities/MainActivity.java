package com.cg.lostfoundapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.cg.lostfoundapp.R;
import com.cg.lostfoundapp.adapters.ItemFoundAdapter;
import com.cg.lostfoundapp.manager.PreferencesManager;
import com.cg.lostfoundapp.model.Item;
import com.cg.lostfoundapp.model.FoundItem;
import com.cg.lostfoundapp.model.KVMList;
import com.cg.lostfoundapp.model.KVMListItem;
import com.cg.lostfoundapp.model.User;
import com.cg.lostfoundapp.service.ItemWSController;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView foundList;
    private ArrayList<FoundItem> foundItemList = new ArrayList<>();
    private ItemFoundAdapter foundListAdapter = new ItemFoundAdapter(this,foundItemList);

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeaderView;
    private TextView usernameMainAppText;
    private TextView phoneNumberMainAppText;

    private String username;
    private String phoneNumber;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        foundList.setAdapter(foundListAdapter);
        fillFoundItemList();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
            if (user!=null) {
                username = user.getUsername();
                phoneNumber = user.getPhoneNumber();
            }

        }


        if (user == null) {
            removeRemember();
            finish();
        }


 
        phoneNumberMainAppText.setText(phoneNumber);
        usernameMainAppText.setText(username);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        new ItemtListAsyncTask().execute();

    }

    public void initComponents(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        foundList = (ListView) findViewById(R.id.foundList);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        usernameMainAppText = (TextView) navHeaderView.findViewById(R.id.usernameMainAppText);
        phoneNumberMainAppText = (TextView) navHeaderView.findViewById(R.id.phoneNumberMainAppText);
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

        if(id == R.id.menuItemLost){
            //i lost on intent extra
            Intent intent = new Intent(MainActivity.this,LostOrFoundActivity.class);
            intent.putExtra("itemType", Item.LOST);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        if(id == R.id.menuItemFound){
            //i found on intent extra
            Intent intent = new Intent(MainActivity.this,LostOrFoundActivity.class);
            intent.putExtra("itemType", Item.FOUND);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        if(id == R.id.navMyItems){
            Toast.makeText(MainActivity.this,"MY ITEMS LIST ON",Toast.LENGTH_LONG).show();
        }

        if(id == R.id.navSwitchLists){
            String title = item.getTitle().toString();
            if(title.equals("Switch List(LOST ON)")){
                title = "Switch List(FOUND ON)";
                Toast.makeText(MainActivity.this,"FOUND LIST ON",Toast.LENGTH_LONG).show();
            }else{
                title = "Switch List(LOST ON)";
                Toast.makeText(MainActivity.this,"LOST LIST ON",Toast.LENGTH_LONG).show();
            }
            item.setTitle(title);
        }

        if(id == R.id.navLogout){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            removeRemember();
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeRemember() {
        PreferencesManager preferencesManager = new PreferencesManager(this);
        preferencesManager
                .remove("username")
                .remove("password")
                .commit();
    }

    //se va inlocui cu metoda care populeaza lista de obiecte gasite
    public void fillFoundItemList(){
        foundItemList.add(new FoundItem("Caine", "Azi @ 08:00"));
        foundItemList.add(new FoundItem("Portofel", "10-09-2016 @ 12:00"));
        foundItemList.add(new FoundItem("Legitimatie", "Ieri @ 12:43"));
        foundItemList.add(new FoundItem("Caine", "Azi @ 08:00"));
        foundItemList.add(new FoundItem("Portofel", "10-09-2016 @ 12:00"));
        foundItemList.add(new FoundItem("Legitimatie", "Ieri @ 12:43"));
        foundItemList.add(new FoundItem("Caine", "Azi @ 08:00"));
        foundItemList.add(new FoundItem("Portofel", "10-09-2016 @ 12:00"));
        foundItemList.add(new FoundItem("Legitimatie", "Ieri @ 12:43"));

        foundListAdapter.notifyDataSetChanged();
    }

    private class ItemtListAsyncTask extends AsyncTask<Void, Void, KVMListItem> {

        @Override
        protected KVMListItem doInBackground(Void... params) {
            return ItemWSController.getInstance().get();
        }

        @Override
        protected void onPostExecute(KVMListItem items) {
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }
}