package com.ankushvpathakgmail.sqlfirebasedemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayAdapter;
    ArrayList<Count> arrayList;
    ArrayList<String> arrayListDisplay;
    boolean isSql;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        init();
    }

    void init()
    {
        listView = findViewById(R.id.listViewCounts);
        arrayList = new ArrayList<>();
        arrayListDisplay = new ArrayList<>();
        sharedPreferences = getSharedPreferences("ACS", MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ACS");
        sqLiteDatabase = openOrCreateDatabase("ACS", MODE_PRIVATE, null);
        isSql = sharedPreferences.getBoolean("ACS", false);

        loadListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LoadActivity.this, CounterActivity.class);
                intent.putExtra("count", arrayList.get(i).getCount());
                startActivity(intent);
                finish();
            }
        });
    }

    void loadListView()
    {
        if(isSql)
        {
            Cursor resultSet = sqLiteDatabase.rawQuery("SELECT * FROM ACS;", null);
            resultSet.moveToFirst();
            Log.e("SQL", "" + resultSet.getCount());
            for(int i = 0;i < resultSet.getCount(); i++)
            {
                Log.e("SQL", resultSet.getColumnCount() + "");
                arrayList.add(new Count(resultSet.getString(0),resultSet.getInt(1)));
                arrayListDisplay.add("Name: " + resultSet.getString(0) + "\nCount: " + resultSet.getInt(1));
                resultSet.moveToNext();
            }

            arrayAdapter = new ArrayAdapter(LoadActivity.this, android.R.layout.simple_list_item_1, arrayListDisplay);
            listView.setAdapter(arrayAdapter);
        }
        else
        {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        arrayList.add(ds.getValue(Count.class));
                        arrayListDisplay.add("Name: " + ds.getValue(Count.class).getName() + "\nCount: " + ds.getValue(Count.class).getCount());
                    }
                    arrayAdapter = new ArrayAdapter(LoadActivity.this, android.R.layout.simple_list_item_1, arrayListDisplay);
                    listView.setAdapter(arrayAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
