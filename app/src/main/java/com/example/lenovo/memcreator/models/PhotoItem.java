package com.example.lenovo.memcreator.models;

/**
 * Created by Lenovo on 2/3/2017.
 */

public class PhotoItem {
    private String path;
    private boolean isSelected;
    private String pathUri;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPathUri() {
        return pathUri;
    }

    public void setPathUri(String pathUri) {
        this.pathUri = pathUri;
    }
}
