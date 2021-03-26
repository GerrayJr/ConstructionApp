package com.gerray.fmsystem.ContractorModule;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.CommunicationModule.ChatActivity;
import com.gerray.fmsystem.CommunicationModule.ChatClass;
import com.gerray.fmsystem.CommunicationModule.ChatViewHolder;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContractorChat extends Fragment {
    FirebaseRecyclerAdapter<ChatClass, ChatViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<ChatClass> options;
    FirebaseUser firebaseUser;


    public ContractorChat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultant_chat, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("ChatRooms");

        options = new FirebaseRecyclerOptions.Builder<ChatClass>().setQuery(databaseReference, ChatClass.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatClass, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull final ChatClass model) {
                if (firebaseUser.getUid().equals(model.receiverID)) {
                    holder.contactName.setText(model.getSenderName());
                    holder.time.setText(String.valueOf(model.getTime()));
                    holder.itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("receiverName", model.getSenderName());
                        intent.putExtra("receiverID", model.getReceiverID());
                        intent.putExtra("senderName", model.getReceiverName());
                        intent.putExtra("chatID", model.getChatID());
                        startActivity(intent);
                    });

                } else if (firebaseUser.getUid().equals(model.senderID)) {
                    holder.contactName.setText(model.getReceiverContact());
                    holder.time.setText(String.valueOf(model.getTime()));
                    holder.itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("receiverName", model.getReceiverContact());
                        intent.putExtra("receiverID", model.getReceiverID());
                        intent.putExtra("senderName", model.getSenderName());
                        intent.putExtra("chatID", model.getChatID());
                        startActivity(intent);
                    });
                } else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }

            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ChatViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.chat_card, parent, false));
            }
        }

        ;

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_chat);
        recyclerView.setLayoutManager(new

                LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        // Inflate the layout for this fragment

        return view;
    }
}