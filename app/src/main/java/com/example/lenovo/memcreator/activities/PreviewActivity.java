package com.example.lenovo.memcreator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.adapters.PreviewPhotoListAdapter;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Memory;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView previewPhotoList;
    private PreviewPhotoListAdapter adapter;
    private ArrayList<String> pathList;
    private Memory memory;
    private Button doneBtn;
    private Button backBtn;
    private MyDatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        previewPhotoList = (RecyclerView) findViewById(R.id.pic_list);
        previewPhotoList.setLayoutManager(new GridLayoutManager(this, 3));
        Bundle extras = getIntent().getExtras();

        memory = extras.getParcelable("memory");
        pathList = extras.getStringArrayList("photos");

        adapter = new PreviewPhotoListAdapter(this, pathList);
        previewPhotoList.setAdapter(adapter);

        doneBtn = (Button) findViewById(R.id.btn_done);
        doneBtn.setOnClickListener(this);

        backBtn = (Button) findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);

        manager = new MyDatabaseManager(this, null, null, 1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                updateMemory(pathList);
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    private void updateMemory(ArrayList<String> pathList) {
        for(String path : pathList) {
            memory.addPhoto(path);
        }
        manager.addPhotoToMemory(memory);
    }
}
