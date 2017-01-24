package com.example.lenovo.memcreator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lenovo.memcreator.R;

public class EditingMemoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_memory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
