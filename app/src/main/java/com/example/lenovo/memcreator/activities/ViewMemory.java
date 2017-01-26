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
import com.example.lenovo.memcreator.models.Memory;


public class ViewMemory extends AppCompatActivity implements View.OnClickListener {

    private TextView memoryName;
    private ImageView memoryIcon;
    private TextView memoryDate;
    private TextView memoryTime;
    private TextView memoryText;
    private Button deleteBtn;
    private Button editBtn;
    private Memory memory;

    private int width = 350;
    private int height = 350;
    private int preferredWidth = 350;
    private int preferredHeight = 350;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initExtras();
        initViews();
        setUpListeners();
        manipulateViews();

    }

    private void initExtras() {
        memory = getIntent().getParcelableExtra("memory_object");
    }

    private void initViews() {
        memoryName = (TextView) findViewById(R.id.view_memory_name);
        memoryIcon = (ImageView) findViewById(R.id.view_memory_icon);
        memoryDate = (TextView) findViewById(R.id.view_memory_date);
        memoryTime = (TextView) findViewById(R.id.view_memory_time);
        memoryText = (TextView) findViewById(R.id.view_memory_text);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        editBtn = (Button) findViewById(R.id.btn_edit);
    }

    private void setUpListeners() {
        deleteBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                promptConfirmation();
                break;
            case R.id.btn_edit:
                editMemory();
                break;
        }
    }

    private void editMemory() {
        Intent intent = new Intent(ViewMemory.this, EditingMemoryActivity.class);
        intent.putExtra("memory", memory);
        startActivity(intent);
    }

    private void promptConfirmation() {
        Intent intent = new Intent(ViewMemory.this, PromptDeleteConfirmationActivity.class);
        intent.putExtra("memory", memory);
        startActivity(intent);
    }

    private void manipulateViews() {
        memoryName.setText(memory.getName());
        memoryDate.setText("Date: " + memory.getDate());
        memoryText.setText(memory.getText());
        memoryTime.setText("Time: " + memory.getTime());

        measureSizeForImageView();
        setMemoryIcon();
        setMemoryPhotos();
    }

    private void measureSizeForImageView() {
        Display display = getWindowManager().getDefaultDisplay();

        height = memoryIcon.getDrawable().getIntrinsicHeight();
        width = memoryIcon.getDrawable().getIntrinsicWidth();

        int screenWidth = display.getWidth();
        int screenHeight = display.getWidth();

        if(width >= height) {
            float ratio = 1f * screenWidth / width;
            screenHeight = (int)(1f * height * ratio);
        }
        else {
            float ratio = 1f * screenHeight / height;
            screenWidth = (int)(1f * width * ratio);
        }

        preferredHeight = screenHeight;
        preferredWidth = screenWidth;
    }

    private void setMemoryIcon() {
        if(memory.getIcon() != null) {
            memoryIcon.setImageBitmap(BitmapFactory.decodeFile(memory.getIcon()));
        } else {
            memoryIcon.setImageResource(R.drawable.moments);
        }

        memoryIcon.getLayoutParams().height = preferredHeight;
        memoryIcon.getLayoutParams().width = preferredWidth;
    }

    private void setMemoryPhotos() {
        // TO DO
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
