package com.example.lenovo.memcreator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Memory;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class ViewMemory extends AppCompatActivity implements View.OnClickListener {

    private Memory memory;
    private MyDatabaseManager manager;
    private ViewFlipper flipper;
    private TextView memoryText;
    private Button toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getOverflowMenu();
        manager = new MyDatabaseManager(this, null, null, 1);
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
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        memoryText = (TextView) findViewById(R.id.memory_text);
        toggle = (Button) findViewById(R.id.toggle);
        toggle.setOnClickListener(this);
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
        memoryText.setText(memory.getText());
        prepareFlipper();
    }

    private void prepareFlipper() {
        flipper.removeAllViews();
        ArrayList<String> photoList = manager.getMemoryPhotos(memory);
        for (String path : photoList) {
            View view = LayoutInflater.from(this).inflate(R.layout.flip_item, flipper, false);
            ImageView flipImage = (ImageView) view.findViewById(R.id.photo_item);
            Picasso.with(this).load("file:" + path).into(flipImage);
            flipper.addView(view);
        }

        if(photoList.size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.flip_item, flipper, false);
            ImageView flipImage = (ImageView) view.findViewById(R.id.photo_item);
            flipImage.setImageResource(R.drawable.no_image);
            flipper.addView(view);
        }

        flipper.setAutoStart(true);
        flipper.setFlipInterval(5000);
        flipper.startFlipping();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle:
                toggleFlipping();
                break;
        }
    }

    private void toggleFlipping() {
        if(flipper.isFlipping()) {
            flipper.stopFlipping();
            toggle.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
        }
        else {
            flipper.startFlipping();
            toggle.setBackgroundResource(R.drawable.ic_pause_circle_filled_black_24dp);
        }
    }
}
