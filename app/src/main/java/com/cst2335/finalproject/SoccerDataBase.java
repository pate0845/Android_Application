package com.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SoccerDataBase extends SQLiteOpenHelper {
    protected final static String DataBase_Name = "Soccer_DB";
    protected final static int VERSION_NUM = 1;
    public final static String Table_Name = "Soccer_Favorites";
    public final static String Col_id = "_id";
    public final static String Col_title = "title";
    public final static String Col_date = "date";
    public final static String Col_image = "image";

    public SoccerDataBase(Context ctx) {
        super(ctx, DataBase_Name, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table_Name + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Col_title + " text,"
                + Col_date + " text,"
                + Col_image + " text);");
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
}
