package com.example.h2opurveyorz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WaterSupply.db";
    public static final String TABLE_cust = "Customer";
    public static final String cust_ph = "CustomerPhoneNo";
    public static final String TABLE_water = "Water";
    public static final String TABLE_zonal = "ZonalHead";
    public static final String TABLE_record = "Record";
    public static final String TABLE_invoice = "Invoice";
    public static final String TABLE_fringe = "FringeBenefits";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1); //creating database
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_zonal+" (ID INTEGER PRIMARY KEY, Name TEXT, phone_no TEXT, zone TEXT NOT NULL);");

        db.execSQL("create table if not exists "+TABLE_cust+" (cust_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, area TEXT NOT NULL, " +
                "mailid TEXT NOT NULL UNIQUE, password TEXT NOT NULL, address TEXT NOT NULL, ID INTEGER, FOREIGN KEY(ID) REFERENCES "+TABLE_zonal+"(ID));");

        db.execSQL("create table if not exists "+cust_ph+" (cust_id INTEGER NOT NULL, phone_no TEXT, FOREIGN KEY(cust_id) REFERENCES "+TABLE_cust+"(cust_id));");

        db.execSQL("create table if not exists "+TABLE_water+" (type TEXT PRIMARY KEY, current_quantity float, tot_quantity_rec float, tot_quantity_sent FLOAT);");

        db.execSQL("create table if not exists "+TABLE_record+" (rec_id INTEGER PRIMARY KEY AUTOINCREMENT, delivery_date text , quantity  float, ID INTEGER, " +
                "type TEXT, mailid text, FOREIGN KEY(ID) REFERENCES "+TABLE_zonal+"(ID), FOREIGN KEY(type) REFERENCES "+TABLE_water+"(type), " +
                "FOREIGN KEY(mailid) REFERENCES "+TABLE_cust+"(mailid));");

        db.execSQL("create table if not exists "+TABLE_invoice+" (Bill_no INTEGER PRIMARY KEY AUTOINCREMENT, total_cost float, mailid TEXT, " +
                "FOREIGN KEY(mailid) REFERENCES "+TABLE_cust+"(mailid));");

        db.execSQL("create table if not exists "+TABLE_fringe+" (mailid TEXT, benefit INTEGER, FOREIGN KEY(mailid) REFERENCES "+TABLE_cust+"(mailid));");


        db.execSQL("INSERT INTO "+TABLE_zonal+" VALUES(101, 'Mahalakshmi' , '9677484810', 'Ettimadai');");
        db.execSQL("INSERT INTO "+TABLE_zonal+" VALUES(202, 'SaiPriyadarshini' , '8870484810', 'Gandhipuram');");
        db.execSQL("INSERT INTO "+TABLE_zonal+" VALUES(303, 'Shashank' , '8870123810', 'Kavundampalayam');");
        db.execSQL("INSERT INTO "+TABLE_zonal+" VALUES(404, 'Sahit' , '8456784810', 'Singanallur');");
        db.execSQL("INSERT INTO "+TABLE_zonal+" VALUES(505, 'Anupama' , '8870487650', 'Ukkadam');");

        db.execSQL("INSERT INTO "+TABLE_water+" VALUES('Grade A', 20000, 20000, 0)");
        db.execSQL("INSERT INTO "+TABLE_water+" VALUES('Grade B', 20000, 20000, 0)");
        db.execSQL("INSERT INTO "+TABLE_water+" VALUES('Harvest', 10000, 10000, 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertData(String name, String area, String mailid, String pass, String addr){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("area",area);
        cv.put("mailid",mailid);
        cv.put("password",pass);
        cv.put("address",addr);
        long result = db.insert(TABLE_cust, null, cv);
        if(result==-1)
            return false;
        else
            return true;
    }

    public boolean insertData(String phno, String mail){
        SQLiteDatabase db= this.getWritableDatabase();
        if(mail.length()!=0 && phno.length()!=0)
        {
            db.execSQL("Insert into "+cust_ph+" values((select cust_id from "+TABLE_cust+" where mailid = '"+mail+"'),"+phno+");");
            return true;
        }
        return false;
    }

    public void insertDataRec(String date, float quantity, int ID, String type, String mail){
        Log.d("Error",date);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO "+TABLE_record+" VALUES(NULL,'"+date+"',"+quantity+","+ID+",'"+type+"','"+mail+"');");
    }

    public void updateWaterData(float value, String type){  //to update database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_water+" WHERE type = '"+type+"';", null);
        c.moveToFirst();
        float mon = c.getFloat(c.getColumnIndex("current_quantity"))-value;
        float mon2 = c.getFloat(c.getColumnIndex("tot_quantity_sent"))+value;
        db.execSQL("UPDATE Water SET current_quantity = "+mon+" where type = '"+type+"';");
        db.execSQL("UPDATE Water SET tot_quantity_sent = "+mon2+" where type = '"+type+"';");
    }

    public void updateData(String area){  //set the zonal head for each customer
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_cust+" SET id = (select id from "+TABLE_zonal+" where zone = '"+area+"') WHERE area = '"+area+"';");
    }

    public Cursor selection(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Customer WHERE mailid = '"+email+"'", null);
        return  c;
    }

    public Cursor selectData(String table_name, String col_name, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+table_name+" WHERE "+col_name+" = '"+value+"';", null);
        return c;
    }

    public void deleteData(String table_name, String col_name, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+table_name+" WHERE "+col_name+" = '"+value+"';");
    }

    public void InvoiceInsert(String email){ //COMMIT
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Insert into "+TABLE_invoice+" values(NULL,0.0,'"+email+"');");
        db.execSQL("Insert into "+TABLE_fringe+" values('"+email+"',0);");
    }

    public void InvoiceUpdate(String email,float amt){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_invoice+" WHERE mailid = '"+email+"';", null);
        c.moveToFirst();
        float mon = c.getFloat(c.getColumnIndex("total_cost"))+amt;
        db.execSQL("UPDATE "+TABLE_invoice+" SET total_cost = "+mon+" WHERE mailid = '"+email+"';");
    }

    public void updateFringe(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_fringe+" SET benefit = benefit + 1 WHERE mailid = '"+email+"';");
    }

}
