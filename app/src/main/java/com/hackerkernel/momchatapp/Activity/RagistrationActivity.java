package com.hackerkernel.momchatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hackerkernel.momchatapp.R;
import com.hackerkernel.momchatapp.Model.Users;

import de.hdodenhof.circleimageview.CircleImageView;

public class RagistrationActivity extends AppCompatActivity {

    TextView signin,btn;

    CircleImageView img;
    EditText name,email,password,conformpassword;
    FirebaseAuth auth;
    Uri imageUri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ragistration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.setCancelable(true);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        signin=findViewById(R.id.signin);
        img=findViewById(R.id.img);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        conformpassword=findViewById(R.id.conformpassword);
        btn=findViewById(R.id.btn);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(RagistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                String E_name=name.getText().toString();
                String E_email=email.getText().toString();
                String E_password=password.getText().toString();
                String E_conformpassword=conformpassword.getText().toString();
                String E_status="hey there i am using QuickChat";


                if (TextUtils.isEmpty(E_name) || TextUtils.isEmpty(E_email) || TextUtils.isEmpty(E_password) || TextUtils.isEmpty(E_conformpassword)){

                    progressDialog.dismiss();
                    Toast.makeText(RagistrationActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                }

                else  if (!E_email.matches(emailPattern))
                {
                    email.setError("valid email");
                    Toast.makeText(RagistrationActivity.this, "please enter valid email", Toast.LENGTH_SHORT).show();
                }

                else  if (!E_password.equals(E_conformpassword) )
                {
                    progressDialog.dismiss();
                    Toast.makeText(RagistrationActivity.this, "please enter valid password", Toast.LENGTH_SHORT).show();
                }

                else  if (E_password.length()!=6 )
                {
                    progressDialog.dismiss();

                    Toast.makeText(RagistrationActivity.this, "please enter valid password", Toast.LENGTH_SHORT).show();
                }

                else {

                    auth.createUserWithEmailAndPassword(E_email,E_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                DatabaseReference reference= database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("uplod").child(auth.getUid());

                                if (imageUri!=null)
                                {

                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            if (task.isSuccessful()){

                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        imageURI=uri.toString();
                                                        Users users=new Users(auth.getUid(),E_name,E_email,imageURI,E_status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){

                                                                    startActivity(new Intent(RagistrationActivity.this, HomeActivity.class));
                                                                    finish();
                                                                }

                                                                else {

                                                                    Toast.makeText(RagistrationActivity.this, "Error in Creating new  user ", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else {

                                    String E_status="hey there i am using QuickChat";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/mom-chatapp-a389e.appspot.com/o/friend.jpg?alt=media&token=b532d9d3-bfd0-47a2-b8a8-146e896696a6";
                                    Users users=new Users(auth.getUid(),E_name,E_email,imageURI,E_status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                progressDialog.dismiss();
                                                startActivity(new Intent(RagistrationActivity.this,HomeActivity.class));
                                            }

                                            else {

                                                Toast.makeText(RagistrationActivity.this, "Error in Creating new  user ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            else {

                                progressDialog.dismiss();
                                Toast.makeText(RagistrationActivity.this, "somthing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10){

            if (data!=null){

                imageUri=data.getData();
                img.setImageURI(imageUri);
            }
        }
    }
}