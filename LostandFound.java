package com.example.lostandfoundapp;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LostandFound extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostand_found);
        db = new DatabaseHelper(this);
        //Button remove = findViewById(R.id.buttondel);
        Cursor resLost = db.getdatalost();
        Cursor resFound = db.getdatafound();

        if (resLost.getCount() == 0 && resFound.getCount() == 0) {
            Toast.makeText(this, "No Entries Exist", Toast.LENGTH_SHORT).show();
            return;
        }

//        remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Cursor matchedData = db.getMatchedData();
//                startActivity(new Intent(LostandFound .this, Remove.class));
//                if (matchedData.getCount() == 0) {
//                    Toast.makeText(LostandFound.this, "No Matching Entries Found", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                StringBuffer buffer = new StringBuffer();
//                while (matchedData.moveToNext()) {
//                    buffer.append("Title: ").append(matchedData.getString(0)).append("\n");
//                    buffer.append("Location: ").append(matchedData.getString(1)).append("\n");
//                    buffer.append("Date: ").append(matchedData.getString(2)).append("\n\n");
//                }
//                AlertDialog.Builder builder = new AlertDialog.Builder(LostandFound.this);
//                builder.setCancelable(true);
//                builder.setTitle("Found Data");
//                builder.setMessage(buffer.toString());
//                builder.show();
//            }
//        });



        StringBuffer buffer = new StringBuffer();
        int count = 1;
        buffer.append("LOST\n\n");
        while (resLost.moveToNext()) {
            buffer.append(count++).append(". Title: ").append(resLost.getString(0)).append("\n");
            buffer.append("Phone: " + resLost.getString(1) + "\n");
            buffer.append("Description: " + resLost.getString(2) + "\n");
            buffer.append("Date: " + resLost.getString(3) + "\n");
            buffer.append("Location: " + resLost.getString(4) + "\n\n");
        }

        buffer.append("\nFOUND\n\n");
        count = 1; // Reset the count for found items
        while (resFound.moveToNext()) {
            buffer.append(count++).append(". Title: ").append(resFound.getString(0)).append("\n");
            buffer.append("Phone: " + resFound.getString(1) + "\n");
            buffer.append("Description: " + resFound.getString(2) + "\n");
            buffer.append("Date: " + resFound.getString(3) + "\n");
            buffer.append("Location: " + resFound.getString(4) + "\n\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Lost and Found Items");
        builder.setMessage(buffer.toString());
        builder.show();
    }
}
