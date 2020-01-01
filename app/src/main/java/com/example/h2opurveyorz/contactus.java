package com.example.h2opurveyorz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class contactus extends AppCompatActivity {

    TextView ph, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        ph=findViewById(R.id.textView5);
        mail=findViewById(R.id.textView7);
    }

    public void contact(View view){
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        //Another way to pass phone no
//        intent.setData(Uri.parse("tel:" + ph.getText()));
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // add the following line for runtime permission request
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CALL_PHONE},
//                    123);
//            return;
//        }
//        startActivity(intent);
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ph.getText()));
        startActivity(i);

    }

    public void mail(View v){
        Intent email = new Intent(Intent.ACTION_SEND);
        String to=mail.getText().toString();
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});

//need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
