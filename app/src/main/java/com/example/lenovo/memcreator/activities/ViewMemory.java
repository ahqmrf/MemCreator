package com.example.lenovo.memcreator.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.objects.Memory;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;

public class ViewMemory extends AppCompatActivity implements View.OnClickListener {

    private TextView memoryName;
    private ImageView imageView;
    private TextView memoryDate;
    private TextView memoryTime;
    private TextView memoryText;
    private Button deleteBtn;

    private int width = 350;
    private int height = 350;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Memory memory = getIntent().getParcelableExtra("memory_object");

        memoryName = (TextView) findViewById(R.id.view_memory_name);
        imageView = (ImageView) findViewById(R.id.view_memory_icon);
        memoryDate = (TextView) findViewById(R.id.view_memory_date);
        memoryTime = (TextView) findViewById(R.id.view_memory_time);
        memoryText = (TextView) findViewById(R.id.view_memory_text);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(this);

        memoryName.setText(memory.getName());
        memoryDate.setText("Date: " + memory.getDate());
        memoryText.setText(memory.getText());
        memoryTime.setText("Time: " + memory.getTime());

        if(memory.getPics() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(memory.getPics()));
        } else {
            imageView.setImageResource(R.drawable.moments);
        }

        Display display = getWindowManager().getDefaultDisplay();

        height = imageView.getDrawable().getIntrinsicHeight();
        width = imageView.getDrawable().getIntrinsicWidth();

        int screenWidth = display.getWidth();
        int screenHeight = display.getWidth();

        System.out.println(screenHeight + " " + screenWidth);

        if(width >= height) {
            float ratio = 1f * screenWidth / width;
            screenHeight = (int)(1f * height * ratio);
        }
        else {
            float ratio = 1f * screenHeight / height;
            screenWidth = (int)(1f * width * ratio);
        }

        imageView.getLayoutParams().height = screenHeight;
        imageView.getLayoutParams().width = screenWidth;

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
            case R.id.btn_delete:
                promptConfirmation();
                break;
        }
    }

    private void promptConfirmation() {
        Intent intent = new Intent(ViewMemory.this, PromptDeleteConfirmationActivity.class);
        startActivity(intent);
    }
}
