package com.example.lenovo.memcreator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.objects.Memory;

public class AddPhotosToMemoryActivity extends AppCompatActivity {

    private MyDatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos_to_memory);

        Memory memory = getIntent().getParcelableExtra("memory");
        manager = new MyDatabaseManager(this, null, null, 1);

        memory.addPhoto(memory.getIcon());
        manager.addPhotoToMemory(memory);
    }
}
