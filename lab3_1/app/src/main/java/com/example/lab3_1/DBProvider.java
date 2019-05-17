package com.example.lab3_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBProvider {

    final String LOG_TAG = "myLogs";
    final String DB_NAME = "lab3"; // name DB
    final int DB_VERSION = 1; // version DB

    private DBHelper dbh;
    private SQLiteDatabase db;
    private static volatile DBProvider instance;

    public DBProvider(Context ctx){
        this.dbh = new DBHelper(ctx);
        this.db = dbh.getWritableDatabase();
        Log.d(LOG_TAG, " --- Lab3 db v." + db.getVersion() + " --- ");
    }

    public static DBProvider getInstance(Context ctx) {
        DBProvider localInstance = instance;

        if (localInstance == null) {
            instance = localInstance = new DBProvider(ctx);
        }

        return localInstance;
    }

    public SQLiteDatabase getDB(){
        return this.db;
    }

    public void close(){
        this.dbh.close();
    }


    class DBHelper extends SQLiteOpenHelper {
        /** Class which works with database */

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, " --- onCreate database --- ");

            // create table of **students**
            db.execSQL("create table students (" +
                    "id integer  primary key autoincrement," +
                    "full_name text, " +
                    "date_add timestamp default CURRENT_TIMESTAMP" +
                    ");");

            String[] student_names = {
                    "Бугаев Иван Михайлович",
                    "Сидоров Пётр Иванович",
                    "Петров Иван Сидорович",
                    "Романов Дмитрий Александрович",
                    "Дмитриев Дмитрий Дмитриевич"
            };

            ContentValues cv = new ContentValues();

            // fill table of **students**
            for (int i = 0; i < student_names.length; i++) {
                cv.clear();
                cv.put("full_name", student_names[i]);
                db.insert("students", null, cv);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, " --- onUpgrade database from " + oldVersion
                    + " to " + newVersion + " version --- ");
        }
    }
}
