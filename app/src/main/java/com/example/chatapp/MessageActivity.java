package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    ArrayList<Chat> mMsgs;

    MaterialEditText msg_text;
    ImageButton sendBtn;
    TextView receiverUsername; String receiverID;

    RecyclerView recyclerView;
    MessagesAdapter messagesAdapter;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mMsgs = new ArrayList<Chat>();

        //to set receiver username
        receiverUsername = findViewById(R.id.text_username);
        receiverUsername();

        msg_text = findViewById(R.id.editText_msg);
        sendBtn = findViewById(R.id.btn_send_msg);

        //for recycler mesgs container
        recyclerView = findViewById(R.id.recyler_msgs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        receiverID = getIntent().getStringExtra("userID");

        //to send message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!msg_text.equals("")){
                    sendMessage(firebaseUser.getUid(), receiverID, msg_text.getText().toString());
                    msg_text.setText("");
                }else{
                    Toast.makeText(MessageActivity.this, "you must write a text", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void receiverUsername(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(getIntent().getStringExtra("userID"));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    receiverUsername.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        displayMessages(firebaseUser.getUid(), getIntent().getStringExtra("userID"));

    }

    public void sendMessage(String sender, String receiver, String msg){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("msg", msg);

        reference.child("Chats").push().setValue(hashMap);

    }

    public void displayMessages(String senderID, String receiverID) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMsgs.clear();

                for (DataSnapshot msgs : dataSnapshot.getChildren()) {
                    Chat chat = msgs.getValue(Chat.class);

                    if (chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(getIntent().getStringExtra("userID"))
                            ||
                            chat.getSender().equals(getIntent().getStringExtra("userID")) && chat.getReceiver().equals(firebaseUser.getUid())) {
                        mMsgs.add(chat);
                    }
                }
                messagesAdapter = new MessagesAdapter(MessageActivity.this, mMsgs);
                recyclerView.setAdapter(messagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
