package com.gerray.fmsystem.ManagerModule.ChatRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.CommunicationModule.ChatClass;
import com.gerray.fmsystem.LesseeModule.LesCreate;
import com.gerray.fmsystem.ManagerModule.Lessee.LesseeRecyclerViewHolder;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ChatSelectFM extends AppCompatActivity {
    FirebaseRecyclerOptions<LesCreate> options;
    FirebaseRecyclerAdapter<LesCreate, LesseeRecyclerViewHolder> adapter;
    DatabaseReference dbRef, reference;
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
        FirebaseUser currentUser = auth.getCurrentUser();


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
                        String senderID = auth.getUid();
                        String receiverID = model.getLesseeID();
                        Date currentTime = Calendar.getInstance().getTime();
                        String receiverName = model.getLesseeName();
                        String receiverContact = model.getContactName();
                        String chatID = String.valueOf(UUID.randomUUID());

                        ChatClass chatClass = new ChatClass(chatID,senderID,receiverID,currentTime,receiverName,receiverContact);
                        reference = FirebaseDatabase.getInstance().getReference().child("ChatRooms");
                        reference.child(chatID).setValue(chatClass);
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