package com.hackerkernel.momchatapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hackerkernel.momchatapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    TextView signin,btn;
    EditText edit1,edit2;
    FirebaseAuth auth;
    CircleImageView img;
    Uri imageURI;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin=findViewById(R.id.signin);
        edit1=findViewById(R.id.edit1);
        edit2=findViewById(R.id.edit2);
        btn=findViewById(R.id.btn);
        img=findViewById(R.id.img);

        auth=FirebaseAuth.getInstance();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=edit1.getText().toString();
                String password=edit2.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

                    Toast.makeText(LoginActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }

                else if (!email.matches(emailPattern))
                {

                    edit1.setError("Envalid Email");
                    Toast.makeText(LoginActivity.this, "Envalid Email", Toast.LENGTH_SHORT).show();
                }

                else if (password.length()>6)
                {

                    edit2.setError("Envalid password");
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Password", Toast.LENGTH_SHORT).show();
                }

                else {

                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }

                            else {

                                Toast.makeText(LoginActivity.this, "Error in login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this, RagistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10){

            if (data!=null){

                imageURI=data.getData();
                img.setImageURI(Uri.parse(String.valueOf(imageURI)));
            }
        }
    }
}