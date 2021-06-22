package com.hackerkernel.momchatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hackerkernel.momchatapp.R;
import com.hackerkernel.momchatapp.Model.Users;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    ImageView save,profile;
    CircleImageView img;
    EditText edit_name,edit_status;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Uri uri;
    String email;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        save=findViewById(R.id.save);
        img=findViewById(R.id.img);
        edit_name=findViewById(R.id.edit_name);
        edit_status=findViewById(R.id.edit_status);
        profile=findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
            }
        });

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.setCancelable(true);


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference=storage.getReference().child("uplod").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                 email=snapshot.child("email").getValue().toString();
                String status=snapshot.child("status").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String imageUri=snapshot.child("imageUri").getValue().toString();

                edit_name.setText(name);
                edit_status.setText(status);
                Picasso.get().load(imageUri).into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

               String name= edit_name.getText().toString();
               String status= edit_status.getText().toString();

                if (uri !=null)
                {
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

          @Override
            public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {

              storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {

                      String finalimageuri=uri.toString();
                       Users users=new Users(auth.getUid(),name,status,finalimageuri,email);

                       reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull  Task<Void> task) {

                               if (task.isSuccessful())

                               {
                                   progressDialog.dismiss();
                                   Toast.makeText(SettingActivity.this, "Data Successfully Update", Toast.LENGTH_SHORT).show();
                                   startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                               }

                               else {
                                      progressDialog.dismiss();
                                   Toast.makeText(SettingActivity.this, "Somthing Went Wrong", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                  }
              });

                         }
                            });
                }

                else {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String finalimageuri=uri.toString();
                            Users users=new Users(auth.getUid(),name,status,finalimageuri,email);

                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull  Task<Void> task) {

                                    if (task.isSuccessful())

                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingActivity.this, "Data Successfully Update", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingActivity.this,HomeActivity.class));
                                    }

                                    else {
                                           progressDialog.dismiss();
                                        Toast.makeText(SettingActivity.this, "Somthing Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10){

            if (data!=null){

                uri=data.getData();
                img.setImageURI(uri);
            }
        }
    }
}