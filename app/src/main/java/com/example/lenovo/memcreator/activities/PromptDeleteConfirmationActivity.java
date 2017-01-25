package com.example.lenovo.memcreator.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lenovo.memcreator.R;
import com.example.lenovo.memcreator.database.MyDatabaseManager;
import com.example.lenovo.memcreator.objects.Memory;

public class PromptDeleteConfirmationActivity extends Activity implements View.OnClickListener {

    private Button cancelBtn;
    private Button confirmBtn;

    private MyDatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_delete_confirmation);

        manager = new MyDatabaseManager(this, null, null, 1);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width * .9), (int)(height * .3));

        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);

        confirmBtn = (Button) findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                Intent intent = new Intent(PromptDeleteConfirmationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_confirm:
                deleteMemory();
                finish();
                break;
        }
    }

    private void deleteMemory() {
        Memory memory = getIntent().getParcelableExtra("memory");
        manager.deleteMemory(memory);
        Toast.makeText(getApplicationContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PromptDeleteConfirmationActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
