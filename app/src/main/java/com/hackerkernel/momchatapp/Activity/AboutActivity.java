package com.hackerkernel.momchatapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hackerkernel.momchatapp.R;

public class AboutActivity extends AppCompatActivity {

    RelativeLayout relative1,relative2,relative3,relative4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        relative3=findViewById(R.id.relative3);
        relative1=findViewById(R.id.relative1);
        relative4=findViewById(R.id.relative4);
        relative2=findViewById(R.id.relative2);

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/NagendraPatel01")));



            }
        });

        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/mynetwork/")));


            }
        });

        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://instagram.com/ankit.6614")));

            }
        });

        relative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://m.facebook.com/profile.php")));

            }
        });
    }
}