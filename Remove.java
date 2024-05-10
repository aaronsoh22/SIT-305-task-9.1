package com.example.lostandfoundapp;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Remove extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);
        db = new DatabaseHelper(this);

        TextView textView1 = findViewById(R.id.textView);
        Button delete = findViewById(R.id.buttondel);

        StringBuffer buffer = new StringBuffer();
        Cursor matchedData = db.getMatchedData();
        if (matchedData.getCount() == 0) {
            Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();
            textView1.setText("");
            delete.setVisibility(View.GONE);
        } else {
            while (matchedData.moveToNext()) {
                String name = matchedData.getString(0);
                String description = matchedData.getString(2);
                buffer.append("Name: ").append(name).append("\n\n");
            }
            textView1.setText(buffer.toString());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    matchedData.moveToFirst();
                    do {
                        String name = matchedData.getString(0);
                        boolean deleted = db.deletelost(name) && db.deletefound(name);
                        if (!deleted) {
                            Toast.makeText(Remove.this, "Failed to delete entry: " + name, Toast.LENGTH_SHORT).show();
                        }
                    } while (matchedData.moveToNext());
                    Toast.makeText(Remove.this, "Found Things are Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}
