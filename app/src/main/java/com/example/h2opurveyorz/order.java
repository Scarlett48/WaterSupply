package com.example.h2opurveyorz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class order extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView setType, setCost;
    Button datepick;
    int mYear, mMonth, mDay;
    String date,cur_date,type,cost;
    String pushDate="";
    float quantity;
    Date date1,date2;
    Spinner spin;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setType = findViewById(R.id.setType);
        datepick=findViewById(R.id.datepick);
        setCost=findViewById(R.id.setCost);
        spin = (Spinner) findViewById(R.id.spn1);
        myDb=new DatabaseHelper(this);

        SharedPreferences sp = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        type = sp.getString("type","NA");
        cost = sp.getString("cost","0");
        setType.setText("TYPE : "+type);
        setCost.setText("Rs."+cost+"/- per litre");
        datepick.setText("CLICK HERE");

        String[] objects = {"0","500","1000","1500","2500","3000","3500","4000","4500","5000"};

        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(),android.R.layout.simple_list_item_1 ,objects);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
    }


    public void selectDate(View v){

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        cur_date=mYear+"-"+mMonth+"-"+mDay;
        datepick.setText(mDay+"-"+mMonth+"-"+mYear);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        pushDate=year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
                        datepick.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        date=year+"-"+monthOfYear+"-"+dayOfMonth;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try{
                            date1 = sdf.parse(date);
                            date2 = sdf.parse(cur_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(date1.before(date2)){
                            showMessage("ERROR","Give a valid date!");
                            datepick.setText("CLICK HERE");
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void done(View v){
        if(datepick.getText().toString()=="CLICK HERE"){
            showMessage("ERROR","Pick a date");
        }
        if(quantity==0){
            showMessage("ERROR","Select quantity");
        }

        SharedPreferences sp = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String mail = sp.getString("email",null);
        String type = sp.getString("type",null);
        Cursor c = myDb.selection(mail);
        c.moveToFirst();
        int ZonalID = c.getInt(6);
        myDb.insertDataRec(pushDate,quantity,ZonalID,type,mail);
        myDb.updateWaterData(quantity,type);
        float amount = Float.parseFloat(cost)*quantity;
        myDb.InvoiceUpdate(mail,amount);
        Intent ob = new Intent(this,profile.class);
        showMessage("Transaction","Total Amount Paid : Rs. "+amount);
        Toast.makeText(getApplicationContext(),"Total AMount Paid : Rs."+amount,Toast.LENGTH_LONG).show();
        for(int i =0; i<10000; i++){};
//
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, mail);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "H2O Purveyorz");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your request has been processed!\n You will receive water tankers on :"+pushDate);
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send mail"));
//            finish();
//            Log.i("Finished sending email", "");
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//        }

        startActivity(ob);
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        quantity = Float.parseFloat(spin.getItemAtPosition(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
