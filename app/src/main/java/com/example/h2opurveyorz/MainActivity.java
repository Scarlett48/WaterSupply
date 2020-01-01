package com.example.h2opurveyorz;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void golog(View view){
        Intent ob = new Intent(this, loginpage.class);
        startActivity(ob);
    }

    public void gosign(View view){
        Intent ob = new Intent(this,signupage.class);
        startActivity(ob);
    }

    public void abus(View view){
        Intent ob = new Intent(this,aboutus.class);
        startActivity(ob);
    }

    public void contus(View view){
        Intent ob = new Intent(this,contactus.class);
        startActivity(ob);
    }
}