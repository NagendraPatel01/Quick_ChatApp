package com.hackerkernel.momchatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackerkernel.momchatapp.Activity.ChatActivity;
import com.hackerkernel.momchatapp.Model.Users;
import com.hackerkernel.momchatapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    Context context;
    ArrayList<Users> list;


    public UserAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.useritem,parent,false);
        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Users users=list.get(position);

        holder.user_name.setText(users.name);
        holder.user_status.setText(users.status);

        //Glide.with(context).load("https://image.tmdb.org/t/p/w500"+list.get(position).imageUri).into(holder.user_img);
        Picasso.get().load(users.imageUri).into(holder.user_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("ReciverImage",users.getImageUri());
                intent.putExtra("uid",users.getUid());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

   public   class Viewholder extends RecyclerView.ViewHolder {

        CircleImageView user_img;
        TextView user_name,user_status;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            user_img=itemView.findViewById(R.id.user_img);
            user_name=itemView.findViewById(R.id.user_name);
            user_status=itemView.findViewById(R.id.user_status);
        }
    }
}
