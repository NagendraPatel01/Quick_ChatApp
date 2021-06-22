package com.hackerkernel.momchatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackerkernel.momchatapp.Adapter.MessageAdapter;
import com.hackerkernel.momchatapp.Model.Messages;
import com.hackerkernel.momchatapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReciverImage,ReciverUID,ReciverName,SenderUID;
    CircleImageView circleimg;
    TextView recivername;
    FirebaseDatabase database;
    FirebaseAuth auth;
   public static String sImage;
   public static String rImage;
   EditText editmessage;
   CardView sendbtn;
   RecyclerView Recycleview;
   String senderRoom,reciverRoom;
   ArrayList<Messages>messagesArrayList;
   MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        circleimg=findViewById(R.id.circleimg);
        recivername=findViewById(R.id.recivername);
        editmessage=findViewById(R.id.editmessage);
        sendbtn=findViewById(R.id.sendbtn);
        Recycleview=findViewById(R.id.Recycleview);

        messagesArrayList=new ArrayList<>();


        ReciverName=getIntent().getStringExtra("name");
        ReciverImage=getIntent().getStringExtra("ReciverImage");
        ReciverUID=getIntent().getStringExtra("uid");


        Picasso.get().load(ReciverImage).into(circleimg);
        recivername.setText(""+ReciverName);

        SenderUID=auth.getUid();

        senderRoom=SenderUID+ReciverUID;
        reciverRoom=ReciverUID+SenderUID;

       DatabaseReference refrence= database.getReference().child("user").child(auth.getUid());
       DatabaseReference chatrefrence= database.getReference().child("chats").child(senderRoom).child("messages");

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ChatActivity.this);
        Recycleview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);

        messageAdapter=new MessageAdapter(ChatActivity.this,messagesArrayList);
        Recycleview.setAdapter(messageAdapter);

       chatrefrence.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               messagesArrayList.clear();

               for (DataSnapshot dataSnapshot:snapshot.getChildren())
               {

                   Messages messages=dataSnapshot.getValue(Messages.class);
                   messagesArrayList.add(messages);

               }

              messageAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

       refrence.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

              sImage= snapshot.child("imageUri").getValue().toString();
              rImage=ReciverImage;
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

       sendbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String message=editmessage.getText().toString();

               if (message.isEmpty())
               {
                   Toast.makeText(ChatActivity.this, "Please Enter Valid Message", Toast.LENGTH_SHORT).show();
                   return;
               }
               editmessage.setText("");
               Date date=new Date();

               Messages messages=new Messages(message,SenderUID,date.getTime());
               database=FirebaseDatabase.getInstance();
               database.getReference().child("chats")
                       .child(senderRoom)
                       .child("messages")
                       .push()
                       .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {

                       database.getReference().child("chats")
                               .child(reciverRoom)
                               .child("messages")
                               .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                           }
                       });
                   }
               });

           }
       });
    }

}