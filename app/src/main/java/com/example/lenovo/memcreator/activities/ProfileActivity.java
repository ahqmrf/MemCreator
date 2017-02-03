package com.example.lenovo.memcreator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.database.MyDatabaseManager;

public class ProfileActivity extends AppCompatActivity {

    private MyDatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new MyDatabaseManager(this, null, null, 1);
        setContentView(R.layout.activity_profile);

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
