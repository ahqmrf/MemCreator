package com.example.lenovo.memcreator.activities;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.adapters.CandidatePhotoListAdapter;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Memory;

import java.io.File;
import java.util.ArrayList;

public class AddPhotosToMemoryActivity extends AppCompatActivity {

    private MyDatabaseManager manager;
    private RecyclerView candidatePhotoList;
    private ArrayList<String> listOfImages = new ArrayList<>();
    private Button doneBtn;
    private TextView memoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add photos");
        setContentView(R.layout.activity_add_photos_to_memory);

        memoryTitle = (TextView) findViewById(R.id.tv_memory_title);

        Memory memory = getIntent().getParcelableExtra("memory");
        memoryTitle.setText(memory.getName());
        manager = new MyDatabaseManager(this, null, null, 1);

        candidatePhotoList = (RecyclerView) findViewById(R.id.photos_list);
        candidatePhotoList.setLayoutManager(new GridLayoutManager(this, 2));



        listOfImages.clear();
        getFilePaths();

        final CandidatePhotoListAdapter adapter = new CandidatePhotoListAdapter(this, listOfImages);
        candidatePhotoList.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        doneBtn = (Button) findViewById(R.id.btn_done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You have selected " + adapter.getSelectedIndices().size() + " photos.", Toast.LENGTH_SHORT).show();
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

    public void getFilePaths() {
        Uri uri;
        Cursor cursor;
        int column_index;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA };

        cursor = getContentResolver().query(uri, projection, null,
                null, null);

        column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            F(cursor.getString(column_index));
        }
    }

    private void F(String path) {
        if(path == null) return;
        File file = new File(path);
        if(file.exists()) {
            if(file.isDirectory()) {
                File fileList[] = file.listFiles();
                for (File f : fileList) {
                    F(f.getAbsolutePath());
                }
            }
            else {
                if(path.endsWith(".jpeg") || path.endsWith(".JPEG")
                        || path.endsWith(".png") || path.endsWith(".PNG")
                        || path.endsWith(".bmp") || path.endsWith(".BMP")
                        || path.endsWith(".jpg") || path.endsWith(".JPG")
                        || path.endsWith(".gif") || path.endsWith(".GIF")) {
                    listOfImages.add(path);
                }
                return;
            }
        }
    }
}
