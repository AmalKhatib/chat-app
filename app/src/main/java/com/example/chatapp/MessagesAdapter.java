package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    //left = receiver 0 = sender
    final int LEFT_SIDE_TYPE = 0, RIGHT_SIDE_TYPE = 1;

    ArrayList<Chat> chats;
    Context context;
    MessagesAdapter.ViewHolder viewHolder;

    FirebaseUser firebaseUser;

    public MessagesAdapter(Context context, ArrayList<Chat> chats) {
        this.chats = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if(viewType == RIGHT_SIDE_TYPE)
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_sender_item, viewGroup, false);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_receiver_item, viewGroup, false);

        viewHolder = new MessagesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        viewHolder.msg.setText(chats.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View rootView;
        TextView msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            msg = rootView.findViewById(R.id.text_msg_sender);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid())){
            return RIGHT_SIDE_TYPE;
        }else
            return LEFT_SIDE_TYPE;
    }
}
