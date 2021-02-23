package com.gerray.fmsystem.ManagerModule.ChatRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.CommunicationModule.ChatActivity;
import com.gerray.fmsystem.CommunicationModule.ChatClass;
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.ManagerModule.Lessee.LesseeRecyclerViewHolder;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ChatSelectFM extends AppCompatActivity {
    FirebaseRecyclerOptions<LesCreate> options;
    FirebaseRecyclerAdapter<LesCreate, LesseeRecyclerViewHolder> adapter;
    DatabaseReference dbRef, reference, databaseReference;
    FirebaseAuth auth;

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
        setContentView(R.layout.activity_chat_select_f_m);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();


        dbRef = FirebaseDatabase.getInstance().getReference().child("FacilityOccupants").child(currentUser.getUid());
        options = new FirebaseRecyclerOptions.Builder<LesCreate>().setQuery(dbRef, LesCreate.class).build();
        adapter = new FirebaseRecyclerAdapter<LesCreate, LesseeRecyclerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LesseeRecyclerViewHolder holder, int position, @NonNull final LesCreate model) {
                holder.room.setText(model.getRoom());
                holder.activity.setText(model.getActivityType());
                holder.contactName.setText(model.getContactName());
                holder.lesseeName.setText(model.getLesseeName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ChatSelectFM.this, "Clicked", Toast.LENGTH_SHORT).show();
                        final String senderID = auth.getUid();
                        final String receiverID = model.getUserID();
                        final Date currentTime = Calendar.getInstance().getTime();
                        final String receiverName = model.getLesseeName();
                        final String receiverContact = model.getContactName();
                        final String chatID = String.valueOf(UUID.randomUUID());

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Facilities").child(currentUser.getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String managerName = null, facilityName = null;
                                if (snapshot.child("facilityManager").exists()) {
                                    managerName = snapshot.child("facilityManager").getValue().toString();
                                }
                                if (snapshot.child("name").exists()) {
                                    facilityName = snapshot.child("name").getValue().toString();
                                }
                                ChatClass chatClass = new ChatClass(chatID, senderID, receiverID, currentTime, receiverName, receiverContact, managerName);
                                reference = FirebaseDatabase.getInstance().getReference().child("ChatRooms");
                                reference.child(chatID).setValue(chatClass);

                                Intent intent = new Intent(ChatSelectFM.this, ChatActivity.class);
                                intent.putExtra("receiverName", model.getLesseeName());
                                intent.putExtra("senderName", managerName);
                                intent.putExtra("chatID", chatID);
                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }

            @NonNull
            @Override
            public LesseeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new LesseeRecyclerViewHolder(LayoutInflater.from(ChatSelectFM.this).inflate(R.layout.lessee_cards, parent, false));
            }
        };

        RecyclerView recyclerView = findViewById(R.id.recycler_view_chatSelect);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatSelectFM.this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}