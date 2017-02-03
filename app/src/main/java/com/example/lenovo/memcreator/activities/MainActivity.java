package com.example.lenovo.memcreator.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.adapters.FeedsAdapter;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Memory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private MyDatabaseManager manager;
    private RecyclerView feeds;
    private FeedsAdapter adapter;
    private ArrayList<Memory> itemList;
    private FloatingActionButton createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        manipulateViews();

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        manager = new MyDatabaseManager(this, null, null, 1);
        feeds = (RecyclerView) findViewById(R.id.rv_feeds);

        itemList = manager.getMemory();

        feeds.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FeedsAdapter(this, itemList);
        feeds.setAdapter(adapter);

        createBtn = (FloatingActionButton) findViewById(R.id.btn_go_create_memory);
        createBtn.setOnClickListener(this);
    }

    private void manipulateViews() {
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

    }

    private void update() {
        itemList = manager.getMemory();
        adapter.updateList(itemList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showProfile(MenuItem item) {
        mDrawerLayout.closeDrawers();
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void showSettings(MenuItem item) {
        mDrawerLayout.closeDrawers();
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void exitApp(MenuItem item) {
        System.exit(0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_go_create_memory) {
            startActivity(new Intent(MainActivity.this, CreateMemoryActivity.class));
        }
    }
}
