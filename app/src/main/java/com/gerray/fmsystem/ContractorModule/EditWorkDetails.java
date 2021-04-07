package com.gerray.fmsystem.ContractorModule;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gerray.fmsystem.CommunicationModule.ChatActivity;
import com.gerray.fmsystem.CommunicationModule.ChatClass;
import com.gerray.fmsystem.ManagerModule.ChatRoom.ChatSelectFM;
import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class EditWorkDetails extends AppCompatActivity {
    private TextView edWork, edDescription, edStatus, edCost, edFacility, edDate, tvFacility;
    private ImageView imageView;

    private DatabaseReference dbRef, reference1, ref, ref1;
    FirebaseAuth user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_work_details);

        Intent intent = getIntent();
        String workID = Objects.requireNonNull(intent.getExtras()).getString("workID");
        String receiverID = Objects.requireNonNull(intent.getExtras()).getString("userID");
        Toast.makeText(this, receiverID, Toast.LENGTH_SHORT).show();
        edWork = findViewById(R.id.ed_work);
        edDescription = findViewById(R.id.ed_description);
        edStatus = findViewById(R.id.ed_status);
        edCost = findViewById(R.id.ed_cost);
        edFacility = findViewById(R.id.ed_facility);
        edDate = findViewById(R.id.ed_date);
        tvFacility = findViewById(R.id.tv_facility);

        imageView = findViewById(R.id.editWork_image);

        Button btnUpdate = findViewById(R.id.btn_update_work);
        btnUpdate.setOnClickListener(v -> editWork(workID));
        user = FirebaseAuth.getInstance();

        String chatID = String.valueOf(UUID.randomUUID());
        Date currentTime = Calendar.getInstance().getTime();
        String senderID = user.getUid();


        assert workID != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Work Orders").child(workID);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                edWork.setText(Objects.requireNonNull(snapshot.child("workDescription").getValue()).toString());
                edDescription.setText(Objects.requireNonNull(snapshot.child("workDescription").getValue()).toString());
                edStatus.setText(Objects.requireNonNull(snapshot.child("status").getValue()).toString());
                edDate.setText(Objects.requireNonNull(snapshot.child("workDate").getValue()).toString());
                if (snapshot.child("cost").exists()) {
                    edCost.setText(Objects.requireNonNull(snapshot.child("cost").getValue()).toString());
                } else {
                    edCost.setText("Not Set");
                }
                String imageUrl = Objects.requireNonNull(snapshot.child("imageUrl").getValue()).toString();
                Picasso.with(EditWorkDetails.this).load(imageUrl).into(imageView);

                if (snapshot.child("fManagerID").exists()) {
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Facilities").child(Objects.requireNonNull(snapshot.child("fManagerID").getValue()).toString());
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            edFacility.setText(Objects.requireNonNull(snapshot1.child("facilityManager").getValue()).toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                } else {
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Lessees").child(Objects.requireNonNull(snapshot.child("lesseeID").getValue()).toString());
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            tvFacility.setText("Requester:");
                            edFacility.setText(Objects.requireNonNull(snapshot1.child("contactName").getValue()).toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button btnChat = findViewById(R.id.btn_chat);
        btnChat.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditWorkDetails.this);
            alertDialog.setTitle("Title");
            alertDialog.setMessage("Enter Chat Title");
            alertDialog.setIcon(R.drawable.ic_communicate);
            final EditText input = new EditText(EditWorkDetails.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ref = FirebaseDatabase.getInstance().getReference().child("Consultants").child(senderID);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String contractorName = null;
                            if (snapshot.child("consultantName").exists()) {
                                contractorName = Objects.requireNonNull(snapshot.child("consultantName").getValue()).toString();
                            }

                            ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(receiverID);
                            String title = input.getText().toString().trim();
                            String finalContractorName = contractorName;
                            ref1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String receiverContact, fName = null, sName = null;
                                    if (snapshot.child("firstName").exists()) {
                                        fName = snapshot.child("firstName").getValue().toString();
                                    }
                                    if (snapshot.child("secondName").exists()) {
                                        sName = snapshot.child("secondName").getValue().toString();
                                    }
                                    receiverContact = fName + " " + sName;

                                    ChatClass chatClass = new ChatClass(title, chatID, senderID, receiverID, currentTime, null, receiverContact, finalContractorName);
                                    reference1 = FirebaseDatabase.getInstance().getReference().child("ChatRooms");
                                    reference1.child(chatID).setValue(chatClass);
                                    Intent intent = new Intent(EditWorkDetails.this, ChatActivity.class);
                                    intent.putExtra("receiverName", receiverContact);
                                    intent.putExtra("senderName", finalContractorName);
                                    intent.putExtra("chatID", chatID);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            alertDialog.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        });


    }

    public void editWork(String workID) {
        UpdateDialog updateDialog = new UpdateDialog(workID);
        updateDialog.show(getSupportFragmentManager(), "Update Tag");
    }

}