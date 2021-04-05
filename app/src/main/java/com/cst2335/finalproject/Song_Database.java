package com.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Song_Database extends SQLiteOpenHelper {
    /*
    declaring the variables including the table and column names
     */
    protected final static String DATABASE_NAME="songster.db";
    public final static int VERSION_NUM=13;
    public final static String TABLE_NAME="SAVE_DATA";
    public final static String COL_SONG_NAME="TITLE";
    public final static String COL_ID="id";
    public final static String COL_ARTIST_ID="ARTIST";
    public final static String COL_SONG_ID="SONG";

    /*This constructor will look data directory where the
    database file are located
     */
    public Song_Database(Context context){
        super(context,DATABASE_NAME,null,VERSION_NUM);
    }

    /*This method will create the table as soon as the class is called
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT,"
        +COL_SONG_ID+ " integer ,"
        +COL_ARTIST_ID+ " integer ,"
        +COL_SONG_NAME+ " text);");

    }

    /* This method will delete the older version of the database
    as soon as the version number is increased
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    /*This method will delete the older version of the database
    as soon as the version number is decreased
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
