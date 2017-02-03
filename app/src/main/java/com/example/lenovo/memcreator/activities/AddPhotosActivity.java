package com.example.lenovo.memcreator.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.example.lenovo.memcreator.adapters.FolderListAdapter;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Folder;
import com.example.lenovo.memcreator.models.Memory;


import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;


public class AddPhotosActivity extends AppCompatActivity implements View.OnClickListener, FolderListAdapter.Callback {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private MyDatabaseManager manager;
    private TreeSet<String> folderSet;
    private FolderListAdapter adapter;
    private RecyclerView folderListView;
    private ArrayList<Folder> folderList;
    private Button captureBtn;
    private Button nextBtn;
    private Button cancelBtn;
    private ArrayList<String> listOfSelectedImages;
    private TreeSet<String> setOfSelectedImages;
    private final int SELECTED_PHOTO_REQUEST_CODE = 45;
    private Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos_to_memory);


        memory = getIntent().getParcelableExtra("memory");
        manager = new MyDatabaseManager(this, null, null, 1);

        captureBtn = (Button) findViewById(R.id.btn_capture);
        nextBtn = (Button) findViewById(R.id.btn_next);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        captureBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        listOfSelectedImages = new ArrayList<>();
        setOfSelectedImages = new TreeSet<>();

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                takePicture();
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
        listOfSelectedImages.clear();
        for(String key : setOfSelectedImages) {
            listOfSelectedImages.add(key);
        }

        Intent intent = new Intent(this, PreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("memory", memory);
        bundle.putStringArrayList("photos", listOfSelectedImages);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            String imagePath = getRealPathFromURI(tempUri);
            if (imagePath != null) {
                setOfSelectedImages.add(imagePath);
            }

        }

        else if(requestCode == SELECTED_PHOTO_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                ArrayList<String> list = data.getStringArrayListExtra("list");
                for(String s : list) {
                    setOfSelectedImages.add(s);
                }
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
