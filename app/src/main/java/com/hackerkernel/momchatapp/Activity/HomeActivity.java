package com.hackerkernel.momchatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackerkernel.momchatapp.R;
import com.hackerkernel.momchatapp.Adapter.UserAdapter;
import com.hackerkernel.momchatapp.Model.Users;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView recycle;
    FirebaseDatabase database;
    ArrayList<Users>list;
    UserAdapter userAdapter;
    ImageView logout_btn,Setting_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        list=new ArrayList<>();
        recycle=findViewById(R.id.recycle);
        logout_btn=findViewById(R.id.logout_btn);
        Setting_img=findViewById(R.id.Setting_img);

        Setting_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog=new Dialog(HomeActivity.this,R.style.Dialog);
                dialog.setContentView(R.layout.dialog_layout);
                TextView nobtn,yesbtn;

                nobtn=dialog.findViewById(R.id.nobtn);
                yesbtn=dialog.findViewById(R.id.yesbtn);

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                    }
                });

                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        DatabaseReference reference=database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users=dataSnapshot.getValue(Users.class);
                    list.add(users);
                }

                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycle.setLayoutManager(linearLayoutManager);

        userAdapter=new UserAdapter(HomeActivity.this,list);
        recycle.setAdapter(userAdapter);

        if (auth.getCurrentUser()==null)
        {

            startActivity(new Intent(HomeActivity.this, RagistrationActivity.class));
        }
    }
}