package com.example.lenovo.memcreator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.models.Memory;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;


public class ViewMemory extends AppCompatActivity {

    private ImageView memoryIcon;
    private TextView memoryDate;
    private TextView memoryTime;
    private TextView memoryText;
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
        getOverflowMenu();

        initExtras();
        initViews();
        manipulateViews();

        setTitle(memory.getName());

    }

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initExtras() {
        memory = getIntent().getParcelableExtra("memory_object");
    }

    private void initViews() {
        memoryIcon = (ImageView) findViewById(R.id.view_memory_icon);
        memoryDate = (TextView) findViewById(R.id.view_memory_date);
        memoryTime = (TextView) findViewById(R.id.view_memory_time);
        memoryText = (TextView) findViewById(R.id.view_memory_text);
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
            Picasso.with(this).load("file:" + memory.getIcon()).into(memoryIcon);
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
            case R.id.menu_delete:
                promptConfirmation();
                return true;
            case R.id.menu_edit:
                editMemory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.memory_context_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
