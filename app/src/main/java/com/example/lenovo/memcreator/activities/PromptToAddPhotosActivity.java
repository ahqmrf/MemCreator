package com.example.lenovo.memcreator.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.models.Memory;

public class PromptToAddPhotosActivity extends AppCompatActivity implements View.OnClickListener {

    private Button yesBtn;
    private Button noBtn;

    private MyDatabaseManager manager;
    private Memory memory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_to_add_photos);

        manager = new MyDatabaseManager(this, null, null, 1);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width * .9), (int)(height * .3));

        yesBtn = (Button) findViewById(R.id.btn_yes);
        yesBtn.setOnClickListener(this);

        noBtn = (Button) findViewById(R.id.btn_no);
        noBtn.setOnClickListener(this);

        memory = getIntent().getParcelableExtra("memory");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Intent intent = new Intent(PromptToAddPhotosActivity.this, AddPhotosToMemoryActivity.class);
                intent.putExtra("memory", memory);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_no:
                Intent intent2 = new Intent(PromptToAddPhotosActivity.this, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                finish();
                break;
        }
    }
}
