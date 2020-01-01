package com.example.h2opurveyorz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class loginpage extends AppCompatActivity {

    DatabaseHelper myDb;
    SQLiteDatabase db;
    EditText email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        myDb = new DatabaseHelper(this);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);

    }

    public void logging(View v){
        Cursor c = myDb.selection(email.getText().toString());
        if (c.moveToFirst()) {
            if(c.getString(4).equals(pass.getText().toString())){
                SharedPreferences sp = getSharedPreferences("credentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("name",c.getString(1));
                edit.putString("email",email.getText().toString());
                edit.commit();
                Intent ob = new Intent(this, profile.class);
                clearText();
                startActivity(ob);
            }
            else{
                showMessage("Error","Wrong Password");
            }
        }
        else {
            showMessage("Error", "Email id does not exist in the database");
        }

    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText(){

        email.setText("");
        pass.setText("");
    }

}
