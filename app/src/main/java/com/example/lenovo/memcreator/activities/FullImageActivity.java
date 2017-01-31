package com.example.lenovo.memcreator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.lenovo.memcreator.R;
import com.squareup.picasso.Picasso;

public class FullImageActivity extends AppCompatActivity {

    private ImageView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fullImage = (ImageView) findViewById(R.id.iv_full_image);
        String path = getIntent().getStringExtra("image_path");
        Picasso.with(this).load("file:" + path).into(fullImage);
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
