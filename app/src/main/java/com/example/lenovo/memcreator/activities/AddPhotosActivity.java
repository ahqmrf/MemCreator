package com.example.lenovo.memcreator.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.adapters.CandidatePhotoListAdapter;
import com.example.lenovo.memcreator.adapters.FolderListAdapter;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Folder;
import com.example.lenovo.memcreator.models.Memory;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;


public class AddPhotosActivity extends AppCompatActivity implements View.OnClickListener, FolderListAdapter.Callback {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 101;
    private MyDatabaseManager manager;
    private TreeSet<String> folderSet;
    private FolderListAdapter adapter;
    private RecyclerView folderListView;
    private ArrayList<Folder> folderList;
    private File capturedImagePath;
    private Button captureBtn;
    private Button nextBtn;
    private Button cancelBtn;
    private ArrayList<String> listOfSelectedImages;
    private final int SELECTED_PHOTO_REQUEST_CODE = 45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos_to_memory);


        Memory memory = getIntent().getParcelableExtra("memory");
        manager = new MyDatabaseManager(this, null, null, 1);

        captureBtn = (Button) findViewById(R.id.btn_capture);
        nextBtn = (Button) findViewById(R.id.btn_next);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        captureBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        listOfSelectedImages = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        folderSet = new TreeSet<>();
        folderList = new ArrayList<>();
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(rootPath);
        if(file.exists()) {
            if (file.isDirectory()) {
                File fileList[] = file.listFiles();
                if(fileList != null) {
                    for (File f : fileList) {
                        if(f != null)
                            F(rootPath, f.getAbsolutePath());
                    }
                }
            }
        }

        for(String key : folderSet) {
            File photoFolder = new File(key);
            File photos[] = photoFolder.listFiles();
            if(photos.length > 0) {
                for(File f : photos) {
                    String path = f.getAbsolutePath();
                    if(path.endsWith(".jpeg") || path.endsWith(".JPEG")
                            || path.endsWith(".png") || path.endsWith(".PNG")
                            || path.endsWith(".bmp") || path.endsWith(".BMP")
                            || path.endsWith(".jpg") || path.endsWith(".JPG")
                            || path.endsWith(".gif") || path.endsWith(".GIF")) {
                        folderList.add(new Folder(key, path));
                        System.out.println(path);
                        break;
                    }
                }
            }
        }

        folderListView = (RecyclerView) findViewById(R.id.folder_list);
        adapter = new FolderListAdapter(this, folderList, this);
        folderListView.setLayoutManager(new GridLayoutManager(this, 3));
        folderListView.setAdapter(adapter);

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
            case R.id.btn_capture:
                openCamera();
                break;
            case R.id.btn_next:
                goNext();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void goNext() {

    }

    private void openCamera() {
        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MemCreator");
        if(!imagesFolder.exists()) imagesFolder.mkdirs();

        capturedImagePath = new File(imagesFolder, "MemCreator_" + timeStamp + ".png");
        Uri uriSavedImage = Uri.fromFile(capturedImagePath);

        if(uriSavedImage != null) {

            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            // TO DO
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Captured image saved to " + capturedImagePath.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                listOfSelectedImages.add(capturedImagePath.getAbsolutePath());
            }
            else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                // Image capture failed, advise user
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }

        else if(requestCode == SELECTED_PHOTO_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                ArrayList<String> list = data.getStringArrayListExtra("list");
                Toast.makeText(this, "selected " + list.size() + " photos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void F(String parent, String path) {
        if(path == null) return;
        File file = new File(path);
        if(path.contains("Android")) return;
        if(file.exists()) {
            if(file.isDirectory()) {
                File fileList[] = file.listFiles();
                if(fileList != null) {
                    for (File f : fileList) {
                        if(f != null)
                            F(path, f.getAbsolutePath());
                    }
                }
            }
            else {
                if(path.endsWith(".jpeg") || path.endsWith(".JPEG")
                        || path.endsWith(".png") || path.endsWith(".PNG")
                        || path.endsWith(".bmp") || path.endsWith(".BMP")
                        || path.endsWith(".jpg") || path.endsWith(".JPG")
                        || path.endsWith(".gif") || path.endsWith(".GIF")) {
                    folderSet.add(parent);
                }
                return;
            }
        }
    }

    @Override
    public void onFolderClick(String path) {
        Intent intent = new Intent(this, SelectPhotosActivity.class);
        intent.putExtra("folder", path);
        startActivityForResult(intent, SELECTED_PHOTO_REQUEST_CODE);
    }
}
