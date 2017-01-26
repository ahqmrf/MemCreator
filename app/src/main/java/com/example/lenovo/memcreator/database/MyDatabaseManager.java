package com.example.lenovo.memcreator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovo.memcreator.models.Memory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Lenovo on 1/20/2017.
 */

public class MyDatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "memory.db";
    private static final String TABLE_MEMORY = "memory";
    private static final String COLUMN_ID = "memory_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_ICON = "icon";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";

    private static final String TABLE_PHOTO = "photos";
    private static final String COLUMN_PHOTO_ID = "photo_id";
    private static final String COLUMN_PHOTO_TIME = "photo_time";
    private static final String COLUMN_PHOTO_VALUE = "photo_value";

    public MyDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_MEMORY + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_ICON + " TEXT " +
                ");";
        db.execSQL(query);
        query = "CREATE TABLE " + TABLE_PHOTO + "(" +
                COLUMN_PHOTO_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_PHOTO_TIME + " TEXT, " +
                COLUMN_PHOTO_VALUE + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
        onCreate(db);
    }

    public void addMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, memory.getName());
        values.put(COLUMN_DATE, memory.getDate());
        values.put(COLUMN_TIME, memory.getTime());
        values.put(COLUMN_TEXT, memory.getText());
        values.put(COLUMN_ICON, memory.getIcon());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MEMORY, null, values);
    }

    public void addPhotoToMemory(Memory memory) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        for (String photo : memory.getPhotos()) {
            values.clear();
            values.put(COLUMN_PHOTO_TIME, memory.getTime());
            values.put(COLUMN_PHOTO_VALUE, photo);
            db.insert(TABLE_PHOTO, null, values);
        }
        db.close();
    }

    public void deleteMemory(Memory memory) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MEMORY + " WHERE " + COLUMN_TIME + " = '" + memory.getTime() + "';");
        db.execSQL("DELETE FROM " + TABLE_PHOTO + " WHERE " + COLUMN_PHOTO_TIME + " = '" + memory.getTime() + "';");
        db.close();
        deleteMemoryIcon(memory);
        deleteMemoryPhotos(memory);
    }

    public void deleteMemoryIcon(Memory memory) {
        if (memory.getIcon() == null || memory.getIcon().equals("") || memory.getIcon().length() == 0)
            return;

        File file = new File(memory.getIcon());
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Success");
            }
        }
    }

    public void deleteMemoryPhotos(Memory memory) {
        for (String path : memory.getPhotos()) {
            if (path == null || path.length() == 0) continue;
            File file = new File(path);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("Success");
                }
            }
        }
    }

    public ArrayList<Memory> getMemory() {
        ArrayList<Memory> memories = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_MEMORY + " WHERE 1;";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Memory memory;

        try {
            while (cursor.moveToNext()) {
                memory = new Memory();
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                memory.setId(cursor.getColumnIndex(COLUMN_ID));
                memory.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                memory.setTime(time);
                memory.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                memory.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
                memory.setIcon(cursor.getString(cursor.getColumnIndex(COLUMN_ICON)));
                memory.setPhotos(getMemoryPhotos(time));
                memories.add(memory);
            }
        } finally {
            cursor.close();
        }

        Collections.reverse(memories);
        return memories;
    }

    private ArrayList<String> getMemoryPhotos(String time) {
        ArrayList<String> photos = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_PHOTO + " WHERE " + COLUMN_PHOTO_TIME +
                " = '" + time + "';";
        Cursor cursorPhoto = db.rawQuery(query, null);

        try {
            while (cursorPhoto.moveToNext()) {
                photos.add(cursorPhoto.getString(cursorPhoto.getColumnIndex(COLUMN_PHOTO_VALUE)));
            }
        } finally {
            cursorPhoto.close();
        }

        return photos;
    }
}
