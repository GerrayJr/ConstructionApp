package com.gerray.fmsystem.LesseeModule;

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
import com.gerray.fmsystem.ManagerModule.ChatRoom.ChatSelectFM;
import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LesseeChatFragment extends Fragment {
    private FloatingActionButton selectChat;
    private DatabaseReference databaseReference, reference;
    FirebaseRecyclerAdapter<ChatClass, ChatViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<ChatClass> options;
    FirebaseUser firebaseUser;

    public LesseeChatFragment() {
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
        View v = inflater.inflate(R.layout.fragment_lessee_chat, container, false);
        selectChat = v.findViewById(R.id.fab_text);
        selectChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatSelectFM.class);
                startActivityForResult(intent, 1);
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("ChatRooms");

        options = new FirebaseRecyclerOptions.Builder<ChatClass>().setQuery(databaseReference, ChatClass.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatClass, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull final ChatClass model) {
                if (firebaseUser.getUid().equals(model.senderID)) {
                    holder.contactName.setText(model.getReceiverContact());
                    holder.lesseeName.setText(model.getReceiverName());
                    holder.time.setText(String.valueOf(model.getTime()));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("receiverName", model.getSenderName());
                            intent.putExtra("receiverID", model.getReceiverID());
                            intent.putExtra("chatID", model.getChatID());
                            startActivity(intent);
                        }
                    });

                }
                if (firebaseUser.getUid().equals(model.receiverID)) {
                    holder.lesseeName.setText(model.getSenderName());
                    holder.time.setText(String.valueOf(model.getTime()));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("receiverName", model.getSenderName());
                            intent.putExtra("senderName", model.getReceiverContact());
                            intent.putExtra("receiverID", model.getSenderID());
                            intent.putExtra("chatID", model.getChatID());
                            startActivity(intent);
                        }
                    });

                }
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ChatViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.chat_card, parent, false));
            }
        };

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        // Inflate the layout for this fragment

        return v;
    }
}
