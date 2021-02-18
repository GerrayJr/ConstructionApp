package com.gerray.fmsystem.LesseeModule;

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
import com.gerray.fmsystem.ContractorModule.CreateConsultant;
import com.gerray.fmsystem.ManagerModule.Consultants.ConsultantViewHolder;
import com.gerray.fmsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LesseeConsultant extends AppCompatActivity {

    FirebaseRecyclerOptions<CreateConsultant> options;
    FirebaseRecyclerAdapter<CreateConsultant, ConsultantViewHolder> plumbAdapter, doorAdapter, dryAdapter, paintAdapter, exteriorAdapter, electricAdapter, restAdapter, lightAdapter, floorAdapter;
    RecyclerView lightRecycler, doorRecycler, plumbRecycler, exteriorRecycler, electricRecycler, floorRecycler, restRecycler, paintRecycler, dryRecycler;
    DatabaseReference dbRef;

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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Plumbing installation and Repair")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Restroom Maintenance and repairs")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Exterior Maintenance")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Electrical Maintenance")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Door installation and Repairs")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Light Fixture Installation and Repair")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Drywall repair and installation")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Painting and staining")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
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
            protected void onBindViewHolder(@NonNull ConsultantViewHolder holder, int position, @NonNull CreateConsultant model) {
                String specialization = model.getSpecialization();
                if (specialization.equals("Floor repair and installation")) {
                    holder.tvName.setText(model.getConsultantName());
                    holder.tvCategory.setText(model.getCategory());
                    holder.tvloc.setText(model.getConsultantLocation());
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

    private void emailContact() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{});
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(LesseeConsultant.this, "There are no Email Clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}