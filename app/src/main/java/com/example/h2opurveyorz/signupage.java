package com.example.h2opurveyorz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class signupage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DatabaseHelper myDb;
    EditText uname,email,pass,phone,address;
    Spinner s2;
    String arean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupage);
        uname = findViewById(R.id.uname);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        myDb = new DatabaseHelper(this);

        String areas[]={"Ettimadai","Gandhipuram","Kavundampalayam","Singanallur","Ukkadam"};
        s2=(Spinner)findViewById(R.id.area);
        ArrayAdapter<String> adap2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,areas);
        s2.setAdapter(adap2);
        s2.setOnItemSelectedListener(this);
    }
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            CheckedTextView txt1 = (CheckedTextView) view;
            arean = txt1.getText().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "No selection yet", Toast.LENGTH_SHORT).show();

    }


    public void sign(View v){
        boolean isinserted=false;
        if(uname.getText().toString().trim().length()==0 || arean.length()==0 || email.getText().toString().trim().length()==0 || pass.getText().toString().trim().length()==0 || address.getText().toString().trim().length()==0) {
            showMessage("Error","enter all values");
        }

        else{
            isinserted = myDb.insertData(uname.getText().toString(), arean, email.getText().toString(), pass.getText().toString(), address.getText().toString());
            myDb.updateData(arean);
            isinserted = myDb.insertData(phone.getText().toString(), email.getText().toString());
        }
        if(isinserted==true) {
            Toast.makeText(getApplicationContext(), "Records inserted successfully!", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences("credentials", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("name",uname.getText().toString());
            edit.putString("email",email.getText().toString());
            edit.commit();
            myDb.InvoiceInsert(email.getText().toString());
            Intent ob = new Intent(this, loginpage.class);
            clearText();
            startActivity(ob);
        }
        else
            Toast.makeText(getApplicationContext(), "Records not inserted!", Toast.LENGTH_SHORT).show();

    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText(){

        uname.setText("");
        email.setText("");
        pass.setText("");
        phone.setText("");
        address.setText("");
    }

}

