package com.example.lab3_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;

public class DBProvider {
    /** Singleton keeping db */

    final String LOG_TAG = "myLogs";
    final String DB_NAME = "lab3"; // name DB
    final int DB_VERSION = 2; // version DB

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
                    "first_name text, " +
                    "last_name text, " +
                    "middle_name text, " +
                    "date_add timestamp default CURRENT_TIMESTAMP" +
                    ");");

            String[][] student_names = {
                    {"Бугаев", "Иван", "Михайлович"},
                    {"Сидоров", "Пётр", "Иванович"},
                    {"Петров", "Иван", "Сидорович"},
                    {"Романов", "Дмитрий", "Александрович"},
                    {"Дмитриев", "Дмитрий", "Дмитриевич"}
            };

            ContentValues cv = new ContentValues();

            // fill table of **students**
            for (int i = 0; i < student_names.length; i++) {
                cv.clear();
                cv.put("first_name", student_names[i][1]);
                cv.put("last_name", student_names[i][0]);
                cv.put("middle_name", student_names[i][2]);
                db.insert("students", null, cv);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, " --- onUpgrade database from " + oldVersion
                    + " to " + newVersion + " version --- ");

            if (oldVersion == 1 && newVersion == 2) {

                db.beginTransaction();
                db.execSQL("create temporary table students_backup(id, full_name, date_add)");
                db.execSQL("insert into students_backup select id, full_name, date_add FROM students");
                db.execSQL("drop table students");
                db.execSQL("create table students (" +
                        "id integer primary key autoincrement, " +
                        "first_name text not null, " +
                        "last_name text not null, " +
                        "middle_name text not null, " +
                        "date_add timestamp default CURRENT_TIMESTAMP" +
                        ")");

                Cursor c = db.rawQuery("select * from students_backup", null);

                // migration data
                while (c.moveToNext()) {
                    String[] full_name = c.getString(c.getColumnIndex("full_name"))
                            .split(" ");

                    db.execSQL(
                            "insert into students (" +
                                    "id, first_name, last_name, middle_name, date_add" +
                                    ") VALUES (?, ?, ?, ?, ?)",
                            new String[]{
                                    c.getString(c.getColumnIndex("id")),
                                    full_name[1], full_name[0], full_name[2],
                                    String.valueOf(Timestamp.valueOf(c.getString(c.getColumnIndex("date_add"))))
                            }
                    );
                }

                c.close();

                db.execSQL("drop table students_backup");

                db.setTransactionSuccessful();
                db.endTransaction();
            }
        }
    }
}
