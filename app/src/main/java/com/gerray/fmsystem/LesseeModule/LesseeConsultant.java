package com.gerray.fmsystem.LesseeModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.gerray.fmsystem.ContractorModule.CreateConsultant;
import com.gerray.fmsystem.ManagerModule.Consultants.ConsultantViewHolder;
import com.gerray.fmsystem.ManagerModule.FacilityManager;
import com.gerray.fmsystem.ManagerModule.Lessee.ConfirmAdd;
import com.gerray.fmsystem.ManagerModule.Lessee.SearchLessee;
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

public class LesseeConsultant extends AppCompatActivity {

    FirebaseRecyclerOptions<CreateConsultant> options;
    FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder> plumbAdapter, doorAdapter, dryAdapter, paintAdapter, exteriorAdapter, electricAdapter, restAdapter, lightAdapter, floorAdapter;
    RecyclerView lightRecycler, doorRecycler, plumbRecycler, exteriorRecycler, electricRecycler, floorRecycler, restRecycler, paintRecycler, dryRecycler;
    DatabaseReference dbRef, reference, databaseReference;
    FirebaseAuth user;

    public void onStart() {
        super.onStart();
        floorAdapter.startListening();
        floorAdapter.notifyDataSetChanged();

        plumbAdapter.startListening();
        plumbAdapter.notifyDataSetChanged();

        doorAdapter.startListening();
        doorAdapter.notifyDataSetChanged();

        dryAdapter.startListening();
        dryAdapter.notifyDataSetChanged();

        paintAdapter.startListening();
        paintAdapter.notifyDataSetChanged();

        exteriorAdapter.startListening();
        exteriorAdapter.notifyDataSetChanged();

        electricAdapter.startListening();
        electricAdapter.notifyDataSetChanged();

        restAdapter.startListening();
        restAdapter.notifyDataSetChanged();

        lightAdapter.startListening();
        lightAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (floorAdapter != null) {
            floorAdapter.stopListening();
        }
        if (plumbAdapter != null) {
            plumbAdapter.stopListening();
        }
        if (doorAdapter != null) {
            doorAdapter.stopListening();
        }
        if (dryAdapter != null) {
            dryAdapter.stopListening();
        }
        if (paintAdapter != null) {
            paintAdapter.stopListening();
        }
        if (exteriorAdapter != null) {
            exteriorAdapter.stopListening();
        }
        if (electricAdapter != null) {
            electricAdapter.stopListening();
        }
        if (restAdapter != null) {
            restAdapter.stopListening();
        }
        if (lightAdapter != null) {
            lightAdapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessee_consultant);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Consultants");
        dbRef.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<CreateConsultant>().setQuery(dbRef, CreateConsultant.class).build();
        plumbAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Plumbing installation and Repair")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    plumbRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };
        restAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Restroom Maintenance and repairs")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    restRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };
        exteriorAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Exterior Maintenance")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    exteriorRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };
        electricAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Electrical Maintenance")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    electricRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };
        doorAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Door installation and Repairs")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    doorRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };
        lightAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Light Fixture Installation and Repair")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    lightRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };
        dryAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Drywall repair and installation")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    dryRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };
        paintAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Painting and staining")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    paintRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };
        floorAdapter = new FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull final CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Floor repair and installation")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactContractor(model.getEmailAddress(), model.getUserID(), model.getConsultantName(), model.getSpecialization());
                        }
                    });
                } else {
                    floorRecycler.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public ConsultantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ConsultantViewHolder(LayoutInflater.from(LesseeConsultant.this).inflate(R.layout.consultants_cards, parent, false));
            }
        };

        plumbRecycler = findViewById(R.id.recycler_view_plumbing);
        plumbRecycler.setLayoutManager(new LinearLayoutManager(this));
        plumbRecycler.setAdapter(plumbAdapter);
        plumbAdapter.startListening();

        electricRecycler = findViewById(R.id.recycler_view_electric);
        electricRecycler.setLayoutManager(new LinearLayoutManager(this));
        electricRecycler.setAdapter(electricAdapter);
        electricAdapter.startListening();

        exteriorRecycler = findViewById(R.id.recycler_view_exterior);
        exteriorRecycler.setLayoutManager(new LinearLayoutManager(this));
        exteriorRecycler.setAdapter(exteriorAdapter);
        exteriorAdapter.startListening();

        restRecycler = findViewById(R.id.recycler_view_rest);
        restRecycler.setLayoutManager(new LinearLayoutManager(this));
        restRecycler.setAdapter(restAdapter);
        restAdapter.startListening();

        doorRecycler = findViewById(R.id.recycler_view_door);
        doorRecycler.setLayoutManager(new LinearLayoutManager(this));
        doorRecycler.setAdapter(doorAdapter);
        doorAdapter.startListening();

        lightRecycler = findViewById(R.id.recycler_view_light);
        lightRecycler.setLayoutManager(new LinearLayoutManager(this));
        lightRecycler.setAdapter(lightAdapter);
        lightAdapter.startListening();

        dryRecycler = findViewById(R.id.recycler_view_dry);
        dryRecycler.setLayoutManager(new LinearLayoutManager(this));
        dryRecycler.setAdapter(dryAdapter);
        dryAdapter.startListening();

        paintRecycler = findViewById(R.id.recycler_view_paint);
        paintRecycler.setLayoutManager(new LinearLayoutManager(this));
        paintRecycler.setAdapter(paintAdapter);
        paintAdapter.startListening();

        floorRecycler = findViewById(R.id.recycler_view_floor);
        floorRecycler.setLayoutManager(new LinearLayoutManager(this));
        floorRecycler.setAdapter(floorAdapter);
        floorAdapter.startListening();

    }


    public void contactContractor(final String emailAddress, final String contractorID, final String contractorName, final String receiverActivity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LesseeConsultant.this);
        alertDialog.setMessage("Contact " + contractorName)
                .setCancelable(false)
                .setPositiveButton("Give Work", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(LesseeConsultant.this,LesseeWorkDetails.class)
                        .putExtra("contractorID", contractorID));
                    }
                })
                .setNegativeButton("Chat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String conID = String.valueOf(contractorID);
                        user = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = user.getCurrentUser();
                        final String chatID = String.valueOf(UUID.randomUUID());
                        final Date currentTime = Calendar.getInstance().getTime();
                        final String senderID = user.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Lessees").child(senderID);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String lesseeName = null;
                                if (snapshot.child("lesseeName").exists()) {
                                    lesseeName = snapshot.child("lesseeName").getValue().toString();
                                }
                                ChatClass chatClass = new ChatClass(chatID, senderID, conID, currentTime, receiverActivity, contractorName, lesseeName);
                                reference = FirebaseDatabase.getInstance().getReference().child("ChatRooms");
                                reference.child(chatID).setValue(chatClass);
                                Intent intent = new Intent(LesseeConsultant.this, ChatActivity.class);
                                intent.putExtra("receiverName", contractorName);
                                intent.putExtra("senderName", lesseeName);
                                intent.putExtra("chatID", chatID);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.setTitle("How do you wish to Communicate?");
        alert.show();
    }

//    public void contactUs(String emailAddress) {
//        Intent i = new Intent(Intent.ACTION_SEND);
//        i.setType("message/rfc822");
//        i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
//        try {
//            startActivity(Intent.createChooser(i, "Send mail..."));
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(LesseeConsultant.this, "There are no Email Clients installed.", Toast.LENGTH_SHORT).show();
//        }
//    }
}