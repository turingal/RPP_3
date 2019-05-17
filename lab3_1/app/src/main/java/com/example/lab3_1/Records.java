package com.example.lab3_1;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.lab3_1.Adapter.CardAdapter;

import java.util.ArrayList;

public class Records extends Activity {

    private RecyclerView cardRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records);

        // get db
        DBProvider db_provider = DBProvider.getInstance(this);
        SQLiteDatabase db = db_provider.getDB();

        ArrayList<String> students_names = new ArrayList<String>();

        // get students
        Cursor c = db.rawQuery("select * from students", null);
        if (c != null) {
            if (c.moveToFirst()) {
                StringBuilder sb = new StringBuilder();
                do {
                    sb.setLength(0);
                    for (String cn : c.getColumnNames()) {
                        sb.append(cn + " = "
                                + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    students_names.add(String.valueOf(sb));
                } while (c.moveToNext());
            }
        }
        c.close();

        cardRecyclerView = findViewById(R.id.recycle_view);
        CardAdapter adapter = new CardAdapter(students_names.size(), students_names);
        cardRecyclerView.setAdapter(adapter);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
