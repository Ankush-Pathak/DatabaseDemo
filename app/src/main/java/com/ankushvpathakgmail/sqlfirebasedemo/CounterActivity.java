package com.ankushvpathakgmail.sqlfirebasedemo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CounterActivity extends AppCompatActivity {

    TextView textView, textViewMode;
    Button buttonUp, buttonDown, buttonSave, buttonLoad;
    SharedPreferences sharedPreferences;
    int count = 0;
    boolean isSql;
    DatabaseReference databaseReference;
    SQLiteDatabase sqLiteDatabase;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        count = getIntent().getIntExtra("count", 0);
        init();
    }
    void init()
    {
        sharedPreferences = getSharedPreferences("ACS",MODE_PRIVATE);
        buttonDown = findViewById(R.id.buttonDown);
        buttonLoad = findViewById(R.id.buttonLoad);
        buttonSave = findViewById(R.id.buttonSave);
        textView = findViewById(R.id.textViewCount);
        textViewMode = findViewById(R.id.textViewMode);
        isSql = sharedPreferences.getBoolean("ACS", false);
        if(isSql)
            textViewMode.setText("SQL");
        else
            textViewMode.setText("Firebase");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ACS");
        sqLiteDatabase = openOrCreateDatabase("ACS",MODE_PRIVATE, null);

        buttonUp = (Button) findViewById(R.id.buttonUp);

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                textView.setText(count + "");
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;
                textView.setText(count + "");
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDialog();
            }
        });
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CounterActivity.this, LoadActivity.class));
                finish();
            }
        });
    }

    void showDialog()
    {
        dialog = new Dialog(CounterActivity.this);
        dialog.setContentView(R.layout.dialog_save_count);
        Button buttonSave = dialog.findViewById(R.id.buttonSaveDialog);
        final EditText editText = dialog.findViewById(R.id.editTextSaveDialog);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSql)
                {
                    saveToSql(editText.getText().toString());
                }
                else
                    saveToFB(editText.getText().toString());

                Toast.makeText(CounterActivity.this, "Done.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void saveToSql(String name)
    {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ACS(name VARCHAR, count INT);");
        sqLiteDatabase.execSQL("INSERT INTO ACS(name, count) VALUES (\"" + name +"\", " + count + " );");
    }

    void saveToFB(String name)
    {
        databaseReference.push().setValue(new Count(name, count));
    }
}
