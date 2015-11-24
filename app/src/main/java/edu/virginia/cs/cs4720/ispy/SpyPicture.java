package edu.virginia.cs.cs4720.ispy;

import android.database.Cursor;

/**
 * Created by john on 9/29/15.
 */
public class SpyPicture {
    private String id;
    private String path;
    private String color;
    private float x;
    private float y;
    private double latitude;
    private double longitude;

    public SpyPicture(String id, String path, String color, float x, float y, double latitude, double longitude) {
        this.id = id;
        this.path = path;
        this.color = color;
        this.x = x;
        this.y = y;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SpyPicture (Cursor res) {
        this.id = res.getString(res.getColumnIndex(DBHelper.PICTURES_COLUMN_ID));
        this.path = res.getString(res.getColumnIndex(DBHelper.PICTURES_COLUMN_PATH));
        this.color = res.getString(res.getColumnIndex(DBHelper.PICTURES_COLUMN_COLOR));
        this.x = res.getFloat(res.getColumnIndex(DBHelper.PICTURES_COLUMN_X));
        this.y = res.getFloat(res.getColumnIndex(DBHelper.PICTURES_COLUMN_Y));
        this.latitude = res.getDouble(res.getColumnIndex(DBHelper.PICTURES_COLUMN_LATITUDE));
        this.longitude = res.getDouble(res.getColumnIndex(DBHelper.PICTURES_COLUMN_LONGITUDE));
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
