package com.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Song_Database extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME="songster.db";
    public final static int VERSION_NUM=4;
    public final static String TABLE_NAME="SAVE_DATA";
    public final static String COL_SONG_NAME="TITLE";
    public final static String COL_ID="id";
    public final static String COL_ARTIST_ID="ARTIST";
    public final static String COL_SONG_ID="SONG";

    public Song_Database(Context context){
        super(context,DATABASE_NAME,null,VERSION_NUM);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT,"
        +COL_SONG_ID+ " integer ,"
        +COL_ARTIST_ID+ " integer ,"
        +COL_SONG_NAME+ " text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
