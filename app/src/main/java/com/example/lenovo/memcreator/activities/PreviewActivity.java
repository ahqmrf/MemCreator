package com.example.lenovo.memcreator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.adapters.CandidatePhotoListAdapter;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    private RecyclerView candidatePhotoList;
    private CandidatePhotoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        candidatePhotoList = (RecyclerView) findViewById(R.id.pic_list);
        candidatePhotoList.setLayoutManager(new GridLayoutManager(this, 3));

        ArrayList<String> pathList = getIntent().getStringArrayListExtra("paths");
        adapter = new CandidatePhotoListAdapter(this, pathList);
        candidatePhotoList.setAdapter(adapter);
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
