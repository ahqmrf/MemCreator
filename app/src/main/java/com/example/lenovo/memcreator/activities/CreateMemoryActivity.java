package com.example.lenovo.memcreator.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Memory;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Lenovo on 1/30/2017.
 */

public class CreateMemoryActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 101;
    private static final int BROWSE_GALLERY_REQUEST_CODE = 100;
    private Button selectImageBtn;
    private EditText memoryText, memoryName;
    private ImageView image;
    private Button captureBtn;
    private File imageName;
    private Button createMemBtn;
    private MyDatabaseManager manager;
    private Memory memory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

        selectImageBtn = (Button) findViewById(R.id.btn_add_image);
        selectImageBtn.setOnClickListener(this);


        image = (ImageView) findViewById(R.id.image);
        memoryText = (EditText) findViewById(R.id.memory_text);
        memoryText.setOnFocusChangeListener(this);
        memoryName = (EditText) findViewById(R.id.memory_name);
        memoryName.setOnFocusChangeListener(this);
        captureBtn = (Button) findViewById(R.id.btn_capture_image);
        captureBtn.setOnClickListener(this);

        createMemBtn = (Button) findViewById(R.id.btn_create_memory);
        createMemBtn.setOnClickListener(this);

        manager = new MyDatabaseManager(this, null, null, 1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_image :
                openImageGallery();
                break;
            case R.id.btn_capture_image :
                openCamera();
                break;
            case R.id.btn_create_memory:
                saveMemory();
                break;
        }
    }

    private void saveMemory() {
        String text = memoryText.getText().toString();
        String memName = memoryName.getText().toString();

        if(memName == null | memName.equals("") | memName.length() == 0) {
            Toast.makeText(this, "Memory name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(text == null | text.equals("") | text.length() == 0) {
            Toast.makeText(this, "Write something about this memory!", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println(memName);

        String pics = null;
        if(imageName != null) pics = imageName.getAbsolutePath();
        else {
            Toast.makeText(this, "You did not select/capture any image.", Toast.LENGTH_SHORT).show();
        }

        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        memory = new Memory();
        memory.setName(memName);
        memory.setDate(sdf.format(date).toString());
        sdf = new SimpleDateFormat("HH:mm:ss");
        memory.setTime(sdf.format(date).toString());
        memory.setIcon(pics);
        memory.setText(text);
        manager.addMemory(memory);
        Intent intent = new Intent(this, AddPhotosActivity.class);
        intent.putExtra("memory", memory);
        startActivity(intent);
        initInputs();
    }

    private void initInputs() {
        memoryName.setText("");
        memoryText.setText("");
        image.setImageResource(R.drawable.no_image);
    }

    private void openCamera() {
        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MemCreator");
        if(!imagesFolder.exists()) imagesFolder.mkdirs();

        imageName = new File(imagesFolder, "MemCreator_" + timeStamp + ".png");
        Uri uriSavedImage = Uri.fromFile(imageName);

        if(uriSavedImage != null) {

            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            System.out.println(imageName.getAbsolutePath());
            image.setImageBitmap(BitmapFactory.decodeFile(imageName.getAbsolutePath()));
        }
    }

    private void openImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), BROWSE_GALLERY_REQUEST_CODE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BROWSE_GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    String [] tokens = path.split("/");
                    String imageNameOriginal = tokens[tokens.length - 1];
                    String text = "Selected: " + imageNameOriginal;
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MemCreator");
                    if(!imagesFolder.exists()) imagesFolder.mkdirs();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    imageName = new File(imagesFolder, "MemCreator_" + timeStamp + "_" + imageNameOriginal);

                    try {
                        FileUtils.copyFile(new File(path), imageName);
                        Toast.makeText(this, "A copy of the selected image is saved to " + imageName.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        Picasso.with(this).load("file:" + imageName.getAbsolutePath()).into(image);
                    }  catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        else if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            // TO DO
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Captured image saved to " + imageName.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                image.setImageBitmap(BitmapFactory.decodeFile(imageName.getAbsolutePath()));
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
    }

    /* Get the real path from the URI */
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


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId() == R.id.memory_text || v.getId() == R.id.memory_name) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
