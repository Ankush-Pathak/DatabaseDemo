package com.ankushvpathakgmail.sqlfirebasedemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonFirebase, buttonSql, buttonSharedPref;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isSQL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init()
    {
        buttonFirebase = (Button) findViewById(R.id.buttonFirebase);
        buttonSharedPref = (Button)findViewById(R.id.buttonSharedPref);
        buttonSql = (Button) findViewById(R.id.buttonSQL);
        sharedPreferences = getSharedPreferences("ACS",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isSQL = sharedPreferences.getBoolean("ACS", false);

        buttonSql.setOnClickListener(MainActivity.this);
        buttonSharedPref.setOnClickListener(MainActivity.this);
        buttonFirebase.setOnClickListener(MainActivity.this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.buttonFirebase:
                isSQL = false;
                break;


            case R.id.buttonSQL:
                isSQL = true;
                break;

            case R.id.buttonSharedPref:
                break;

        }
        editor.putBoolean("ACS",isSQL);
        editor.commit();
        intent = new Intent(MainActivity.this, CounterActivity.class);
        startActivity(intent);
        finish();

    }
}
