package edu.virginia.cs.cs4720.ispy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by john on 9/29/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ISpy.db";
    public static final String PICTURES_TABLE_NAME = "pictures";
    public static final String PICTURES_COLUMN_ID = "id";
    public static final String PICTURES_COLUMN_PATH = "path";
    public static final String PICTURES_COLUMN_X = "x";
    public static final String PICTURES_COLUMN_Y = "y";
    public static final String PICTURES_COLUMN_LATITUDE = "latitude";
    public static final String PICTURES_COLUMN_LONGITUDE = "longitude";
    public static final String PICTURES_COLUMN_COLOR = "color";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table pictures " +
                        "(id integer primary key, path text, x integer, y integer, " +
                        "latitude double, longitude double, color text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS pictures");
        onCreate(db);
    }

    public boolean insertPicture (String path, int x, int y, double lat, double lng, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("path", path);
        contentValues.put("x", x);
        contentValues.put("y", y);
        contentValues.put("latitude", lat);
        contentValues.put("longitude", lng);
        contentValues.put("color", color);
        db.insert("pictures", null, contentValues);
        return true;
    }

    public Cursor getPicture(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from pictures where id="+id+"", null);
        return res;
    }
}
