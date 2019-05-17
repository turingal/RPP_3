package com.example.lab3_1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    final String LOG_TAG = "myLogs";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBProvider db_provider = DBProvider.getInstance(this);
        final SQLiteDatabase db = db_provider.getDB();

        writeDatabase(db);

        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);

        final Context ctx = this;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ctx, Records.class);
                ctx.startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ContentValues cv = new ContentValues();
                cv.put("first_name", "Пётр");
                cv.put("last_name", "Петров");
                cv.put("middle_name", "Петрович");
                db.insert("students", null, cv);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // get last insert id
                Cursor c = db.rawQuery("select * from SQLITE_SEQUENCE", null);
                c.moveToFirst();
                long id = c.getInt(c.getColumnIndex("seq"));
                c.close();

                ContentValues cv = new ContentValues();
                cv.put("first_name", "Иван");
                cv.put("last_name", "Иванов");
                cv.put("middle_name", "Иванович");

                db.update(
                        "students", cv, "id = ?",
                        new String[] { String.valueOf(id) }
                        );
            }
        });
    }

    // write data from database to Log
    private void writeDatabase(SQLiteDatabase db) {
        Cursor c = db.rawQuery("select * from students", null);
        logCursor(c, "Table students");
        c.close();
    }

    // write cursor data to Log
    void logCursor(Cursor c, String title) {
        if (c != null) {
            if (c.moveToFirst()) {
                Log.d(LOG_TAG, title + ". " + c.getCount() + " rows");
                StringBuilder sb = new StringBuilder();
                do {
                    sb.setLength(0);
                    for (String cn : c.getColumnNames()) {
                        sb.append(cn + " = "
                                  + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, sb.toString());
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, title + ". Cursor is null");
    }

}