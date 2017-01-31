package com.example.lenovo.memcreator.models;

/**
 * Created by Lenovo on 1/31/2017.
 */

public class Folder {
    private String folderPath;
    private String iconPath;
    private String folderName;

    public String getFolderName() {
        String tokens[] = folderPath.split("/");
        this.folderName = tokens[tokens.length - 1];
        return folderName;
    }

    public void setFolderName(String folderName) {
       this.folderName = folderName;
    }

    public Folder(String folderPath, String iconPath) {
        this.folderPath = folderPath;
        this.iconPath = iconPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
