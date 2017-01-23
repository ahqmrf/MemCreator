package com.example.lenovo.memcreator.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 1/20/2017.
 */

public class Memory implements Parcelable{
    private int id;
    private String name;

    protected Memory(Parcel in) {
        id = in.readInt();
        name = in.readString();
        time = in.readString();
        date = in.readString();
        text = in.readString();
        pics = in.readString();
    }

    public static final Creator<Memory> CREATOR = new Creator<Memory>() {
        @Override
        public Memory createFromParcel(Parcel in) {
            return new Memory(in);
        }

        @Override
        public Memory[] newArray(int size) {
            return new Memory[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String time;
    private String date;
    private String text;
    private String pics;

    public Memory() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id + "\n");
        builder.append(name + "\n");
        builder.append(date + "\n");
        builder.append(time + "\n");
        builder.append(text + "\n");
        builder.append(pics + "\n");

        return builder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(text);
        dest.writeString(pics);
    }
}
