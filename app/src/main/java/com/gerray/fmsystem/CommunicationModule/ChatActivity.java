package com.gerray.fmsystem.CommunicationModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.R;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;

    RecyclerView listOfMessage;
    FirebaseRecyclerOptions<MessageClass> options;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<MessageClass, MessageViewHolder> adapter;
    private EditText edMessage;

    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        final String receiverName = intent.getExtras().getString("receiverName");
        final String senderName = intent.getExtras().getString("senderName");
        final String chatID = intent.getExtras().getString("chatID");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(chatID).child("Messages");

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ChatActivity.this.setTitle(receiverName);

        edMessage = findViewById(R.id.writeMessageEditText);

        Button btnSend = findViewById(R.id.sendChatMessageButton);
        btnSend.setOnClickListener(v -> {
            String message = edMessage.getText().toString().trim();
            long chatTime = new Date().getTime();
            MessageClass messageClass = new MessageClass(message, senderName, chatTime);
            databaseReference.push().setValue(messageClass);
            edMessage.setText("");
            edMessage.requestFocus();
        });
        displayChatMessage();

    }


    private void displayChatMessage() {
        options = new FirebaseRecyclerOptions.Builder<MessageClass>().setQuery(databaseReference, MessageClass.class).build();
        adapter = new FirebaseRecyclerAdapter<MessageClass, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageClass model) {
                holder.messageText.setText(model.getMessageText());
                holder.messageUser.setText(model.getMessageUser());
                holder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new MessageViewHolder(LayoutInflater.from(ChatActivity.this)
                        .inflate(R.layout.message_item, viewGroup, false));
            }
        };

        listOfMessage = (RecyclerView) findViewById(R.id.messagesRecyclerView);
        listOfMessage.setLayoutManager(new LinearLayoutManager(this));
        listOfMessage.setAdapter(adapter);
        adapter.startListening();
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageUser, messageTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (BubbleTextView) itemView.findViewById(R.id.message_text);
            messageUser = (TextView) itemView.findViewById(R.id.message_user);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
        }
    }
}