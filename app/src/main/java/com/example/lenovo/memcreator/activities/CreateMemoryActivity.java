package com.example.lenovo.memcreator.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.text.SimpleDateFormat;

/**
 * Created by Lenovo on 1/30/2017.
 */

public class CreateMemoryActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int BROWSE_GALLERY_REQUEST_CODE = 100;
    private Button selectImageBtn;
    private EditText memoryText, memoryName;
    private ImageView avatarImageView;
    private Button captureBtn;
    private Button createMemBtn;
    private MyDatabaseManager manager;
    private Memory memory = new Memory();
    ;
    private String avatarIconPath;
    public ImageLoader imageLoader = ImageLoader.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

        selectImageBtn = (Button) findViewById(R.id.btn_add_image);
        selectImageBtn.setOnClickListener(this);


        avatarImageView = (ImageView) findViewById(R.id.image);
        memoryText = (EditText) findViewById(R.id.memory_text);
        memoryText.setOnFocusChangeListener(this);
        memoryName = (EditText) findViewById(R.id.memory_name);
        memoryName.setOnFocusChangeListener(this);
        captureBtn = (Button) findViewById(R.id.btn_capture_image);
        captureBtn.setOnClickListener(this);

        createMemBtn = (Button) findViewById(R.id.btn_create_memory);
        createMemBtn.setOnClickListener(this);

        manager = new MyDatabaseManager(this, null, null, 1);

        if(getSupportActionBar() != null)getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avatarIconPath != null) {
                    Intent intent = new Intent(CreateMemoryActivity.this, FullImageActivity.class);
                    intent.putExtra("image_path", avatarIconPath);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_image:
                openImageGallery();
                break;
            case R.id.btn_capture_image:
                takePicture();
                break;
            case R.id.btn_create_memory:
                saveMemory();
                break;
        }
    }

    private void saveMemory() {
        String text = memoryText.getText().toString();
        String memName = memoryName.getText().toString();

        if (memName.equals("") | memName.length() == 0) {
            Toast.makeText(this, "Memory name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (text.equals("") | text.length() == 0) {
            Toast.makeText(this, "Write something about this memory!", Toast.LENGTH_SHORT).show();
            return;
        }

        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        memory.setName(memName);
        memory.setDate(sdf.format(date).toString());
        sdf = new SimpleDateFormat("HH:mm:ss");
        memory.setTime(sdf.format(date).toString());
        memory.setText(text);
        manager.addMemory(memory);
        Intent intent = new Intent(this, AddPhotosActivity.class);
        intent.putExtra("memory", memory);
        startActivity(intent);
        finish();
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        if (getPackageManager().resolveActivity(intent, 0) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), BROWSE_GALLERY_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BROWSE_GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    String path = getRealPathFromURI(this, selectedImageUri);
                    avatarIconPath = path;
                    String text = "Selected: " + path;
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                    avatarImageView.setImageBitmap(BitmapFactory.decodeFile(path));
                    memory.setIcon(path);
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            
            avatarIconPath = getRealPathFromURI(tempUri);
            if (avatarIconPath != null) {
                avatarImageView.setImageBitmap(BitmapFactory.decodeFile(avatarIconPath));
                memory.setIcon(avatarIconPath);
            }

        }

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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.memory_text || v.getId() == R.id.memory_name) {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
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
