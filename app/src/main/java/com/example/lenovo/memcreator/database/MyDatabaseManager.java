package com.example.lenovo.memcreator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovo.memcreator.objects.Memory;

import java.util.ArrayList;

/**
 * Created by Lenovo on 1/20/2017.
 */

public class MyDatabaseManager extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "memory.db";
    private static final String TABLE_MEMORY = "memory";
    private static final String COLUMN_ID ="id";
    private static final String COLUMN_NAME ="name";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_PIC = "picture";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";

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
                COLUMN_PIC + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMORY);
        onCreate(db);
    }

    public void addMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, memory.getName());
        values.put(COLUMN_DATE, memory.getDate());
        values.put(COLUMN_TIME, memory.getTime());
        values.put(COLUMN_TEXT, memory.getText());
        values.put(COLUMN_PIC, memory.getPics());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MEMORY, null, values);
        db.close();
    }

    public void deleteMemory(String time) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MEMORY + " WHERE " + COLUMN_TIME + " = '" + time + "';");
    }

    public ArrayList<Memory> getMemory() {
        ArrayList<Memory> memories = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_MEMORY +" WHERE 1;" ;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Memory memory;

        try {
            while (cursor.moveToNext()) {
                memory = new Memory();
                memory.setId(cursor.getColumnIndex(COLUMN_ID));
                memory.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                memory.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                memory.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                memory.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
                memory.setPics(cursor.getString(cursor.getColumnIndex(COLUMN_PIC)));
                memories.add(memory);
                //System.out.println(memory);
            }
        } finally {
            cursor.close();
        }


        System.out.println(memories.size());

        return memories;
    }
}
