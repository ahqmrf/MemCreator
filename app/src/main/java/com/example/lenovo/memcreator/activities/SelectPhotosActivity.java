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
import com.example.lenovo.memcreator.adapters.CandidatePhotoListAdapter;
import com.example.lenovo.memcreator.models.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

public class SelectPhotosActivity extends AppCompatActivity {

    private RecyclerView choiceList;
    private CandidatePhotoListAdapter adapter;
    private ArrayList<String> paths;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photos);
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String folder = getIntent().getStringExtra("folder");

        choiceList = (RecyclerView) findViewById(R.id.choice_list);
        paths = new ArrayList<>();
        File file = new File(folder);
        File fileList[] = file.listFiles();
        for (File pic : fileList) {
            String path = pic.getAbsolutePath();
            if(path.endsWith(".jpeg") || path.endsWith(".JPEG")
                    || path.endsWith(".png") || path.endsWith(".PNG")
                    || path.endsWith(".bmp") || path.endsWith(".BMP")
                    || path.endsWith(".jpg") || path.endsWith(".JPG")
                    || path.endsWith(".gif") || path.endsWith(".GIF")) {
                paths.add(path);
            }

        }

        adapter = new CandidatePhotoListAdapter(this, paths);
        choiceList.setLayoutManager(new GridLayoutManager(this, 3));
        choiceList.setAdapter(adapter);

        doneBtn = (Button) findViewById(R.id.btn_done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResultBack();
                finish();
            }
        });
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


    private ArrayList<String> getList() {
        TreeSet<Integer> set = adapter.getSelectedIndices();
        ArrayList<String> list = new ArrayList<>();
        for (int i : set) {
            list.add(paths.get(i));
        }
        return list;
    }

    private void sendResultBack() {
        Intent intent = new Intent();
        intent.putExtra("list", getList());
        setResult(RESULT_OK, intent);
    }
}
