package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter {

    ArrayList<User> users;
    Context context;
    UsersAdapter.ViewHolder viewHolder;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_users, viewGroup, false);
        viewHolder = new UsersAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        viewHolder.username.setText(users.get(position).getUsername());


        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userID", users.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View rootView;
        TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            username = rootView.findViewById(R.id.text_username);

        }
    }


}
