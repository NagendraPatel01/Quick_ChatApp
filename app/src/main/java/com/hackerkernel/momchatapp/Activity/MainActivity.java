package com.hackerkernel.momchatapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.hackerkernel.momchatapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

               if (FirebaseAuth.getInstance().getCurrentUser()==null)
               {
                   startActivity(new Intent(MainActivity.this, RagistrationActivity.class));
                   finish();
               }

               else {
                   startActivity(new Intent(MainActivity.this, HomeActivity.class));
                   finish();

               }

            }
        }, 3000);

    }
}