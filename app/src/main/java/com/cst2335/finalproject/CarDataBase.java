package com.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CarDataBase extends SQLiteOpenHelper {
    protected final static String DataBase_Name = "Car_DB";
    protected final static int VERSION_NUM = 1;
    public final static String Table_Name = "Car_Favorites";
    public final static String Col_id = "MakeId";
    public final static String Col_MakeName = "MakeName";
    public final static String Col_modelId = "ModelId";
    public final static String Col_modelName = "ModelName";

    public CarDataBase(Context ctx) {
        super(ctx, DataBase_Name, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table_Name + " (MakeId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Col_MakeName + " text,"
                + Col_modelId + " Integer,"
                + Col_modelName + " text);");
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