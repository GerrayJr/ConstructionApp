package com.gerray.fmsystem.ManagerModule.ChatRoom;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IntRange;
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
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.ManagerModule.Assets.AssetPopUp;
import com.gerray.fmsystem.ManagerModule.Assets.AssetViewHolder;
import com.gerray.fmsystem.ManagerModule.Lessee.LesseeRecyclerViewHolder;
import com.gerray.fmsystem.ManagerModule.Lessee.SearchViewHolder;
import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatFragment extends Fragment {
    private FloatingActionButton selectChat;
    private DatabaseReference databaseReference, reference;
    FirebaseRecyclerAdapter<ChatClass, ChatViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<ChatClass> options;
    FirebaseUser firebaseUser;

    public ChatFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        selectChat = view.findViewById(R.id.fab_text);
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
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatClass model) {
                if (firebaseUser.getUid().equals(model.senderID))
                {
                    holder.contactName.setText(model.getReceiverContact());
                    holder.lesseeName.setText(model.getReceiverName());
                    holder.time.setText(String.valueOf(model.getTime()));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
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
//        if (firebaseUser != null) {
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        final String senderID = dataSnapshot.child("senderID").getValue().toString();
//
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });


            RecyclerView recyclerView = view.findViewById(R.id.recycler_view_chat);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();
            // Inflate the layout for this fragment

            return view;
        }
    }
