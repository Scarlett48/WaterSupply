package com.example.h2opurveyorz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class supply extends AppCompatActivity {

    Button datepick;
    int mYear, mMonth, mDay;
    String date,cur_date,mail;
    String pushDate="";
    Date date1,date2;
    DatabaseHelper myDb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);
        datepick=findViewById(R.id.collection);
        datepick.setText("CLICK HERE");
        myDb=new DatabaseHelper(this);
        SharedPreferences sp = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        mail = sp.getString("email",null);
    }

    public void selectADate(View v){

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

    public void supplyButton(View v){
        if(datepick.getText().toString()=="CLICK HERE"){
            showMessage("ERROR","Pick a date");
        }
        else
        {
            myDb.updateFringe(mail);
            Toast.makeText(getApplicationContext(),"Congratz! You have received some Fringe Beneifts",Toast.LENGTH_SHORT).show();
        }

    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
