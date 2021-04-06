package com.cst2335.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


    public class SoccerDataBase extends SQLiteOpenHelper {

        protected final static String DATABASE_NAME = "NewsDatabase";
        protected final static int VERSION_NUM = 1;
        public final static String TABLE_NAME = "News";
        public final static String COL_Item = "Item";
        public final static String COL_Title = "Title";
        public final static String COL_PubDate = "PubDate";
        public final static String COL_Image = "Image";
        public final static String COL_ID = "_id";
        public final String[] columns = {COL_ID, COL_Item, COL_Title, COL_PubDate, COL_Image};

        /**public SoccerDataBase(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }*/
        public SoccerDataBase(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_Item + " TEXT,"
                    + COL_Title + " TEXT,"
                    + COL_PubDate + " TEXT,"
                    + COL_Image + " TEXT);");
        }


        /**
         * Called when the database needs to be upgraded. The implementation
         * should use this method to drop tables, add tables, or do anything else it
         * needs to upgrade to the new schema version.
         *
         * <p>
         * The SQLite ALTER TABLE documentation can be found
         * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
         * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
         * you can use ALTER TABLE to rename the old table, then create the new table and then
         * populate the new table with the contents of the old table.
         * </p><p>
         * This method executes within a transaction.  If an exception is thrown, all changes
         * will automatically be rolled back.
         * </p>
         *
         * @param db         The database.
         * @param oldVersion The old database version.
         * @param newVersion The new database version.
         */
        //@Override
        // public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onCreate(db);
        //}
        //}
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }