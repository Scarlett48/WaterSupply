package com.example.h2opurveyorz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class request extends AppCompatActivity {

    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        welcome = findViewById(R.id.welcome);

        SharedPreferences sp = getSharedPreferences("credentials",
                Context.MODE_PRIVATE);
        String name = sp.getString("name",null);
        welcome.setText("Welcome "+name+"!");
    }

    public void gradeA(View v){
        Intent ob = new Intent(this,order.class);
        SharedPreferences sp = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("type","Grade A");
        edit.putString("cost","0.5");
        edit.commit();
        startActivity(ob);
    }

    public void gradeB(View v){
        Intent ob = new Intent(this, order.class);
        SharedPreferences sp = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("type","Grade B");
        edit.putString("cost","0.3");
        edit.commit();
        startActivity(ob);
    }

    public void harvesting(View v){
        Intent ob = new Intent(this, order.class);
        SharedPreferences sp = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("type","Harvest");
        edit.putString("cost","0.9");
        edit.commit();
        startActivity(ob);
    }

    public void supplyWater(View v){
        Intent ob = new Intent(this, supply.class);
        startActivity(ob);
    }
}
