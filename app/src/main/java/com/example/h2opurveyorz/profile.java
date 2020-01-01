package com.example.h2opurveyorz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class profile extends AppCompatActivity {

    TextView welcome;
    TextView cid, mid, cno,zh,hno;
    DatabaseHelper myDb;
    String mail,invo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        welcome = findViewById(R.id.wel);
        cid = findViewById(R.id.customerid);
        mid = findViewById(R.id.customermail);
        cno = findViewById(R.id.customerph);
        zh = findViewById(R.id.zonalhead);
        hno = findViewById(R.id.zonalcontact);
        myDb = new DatabaseHelper(this);

        SharedPreferences sp = getSharedPreferences("credentials",
                Context.MODE_PRIVATE);
        String name = sp.getString("name",null);
        mail = sp.getString("email", null);
        welcome.setText(name);
    }

    public void onResume(){
        super.onResume();
        Cursor c = myDb.selection(mail);
        c.moveToFirst();
        Cursor d = myDb.selectData("ZonalHead","ID", c.getString(6));
        d.moveToFirst();
        Cursor e = myDb.selectData("CustomerPhoneNo","cust_id", c.getString(0));
        e.moveToFirst();
        cid.setText("Your ID: "+c.getString(0));
        cno.setText("Contact : "+e.getString(1));
        mid.setText("Mail ID : "+c.getString(3));
        zh.setText("Zonal Head : "+d.getString(1));
        hno.setText("Zonal head Contact : "+d.getString(2));
    }

    public void gotoReq(View v){
        Intent ob = new Intent(this, request.class);
        startActivity(ob);
    }

    public void gotoInvoice(View v){
        invo="";
        Cursor c = myDb.selectData("Record","mailid", mail);
        if(c.moveToFirst()){
            for(int i = 0; i< c.getCount(); i++){
                invo=invo+"\nRecord id: "+c.getString(0)+"\nDelivery date : "+c.getString(1)+"\nQuantity : "+c.getString(2)+"\nType: "+c.getString(4)+"\n";
                c.moveToNext();
            }
        }
        showMessage("YOUR RECORDS",invo);
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
