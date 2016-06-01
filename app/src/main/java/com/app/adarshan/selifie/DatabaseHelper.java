package com.app.adarshan.selifie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nepal on 22/02/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "image.db";
    public static final String TABLE_NAME = "store_img";
    public static final String KEY_IMAGE = "name";
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "("+
            KEY_IMAGE + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST" + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert( byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IMAGE, image);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;

    }

    public List<Bitmap> getImage()
    {

        String qu = "select * from "+ TABLE_NAME ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(qu, null);

        for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
        {
            byte[] imgByte = cur.getBlob(cur.getColumnIndex(KEY_IMAGE));
            bitmaps.add(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
        }
        cur.close();
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return bitmaps ;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

}
