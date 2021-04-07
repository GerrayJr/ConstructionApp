package com.gerray.fmsystem.ContractorModule;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gerray.fmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ContractorProfile extends Fragment {
    private ImageView consImage;
    private TextView consName, consEmail, consPhone, consSpec, consLoc, consType;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseDatabaseReference, reference;
    FirebaseUser firebaseUser;

    public ContractorProfile() {
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
        View view = inflater.inflate(R.layout.fragment_consultant_profile, container, false);

        consImage = view.findViewById(R.id.consultant_image);
        consName = view.findViewById(R.id.cons_name);
        consEmail = view.findViewById(R.id.cons_email);
        consPhone = view.findViewById(R.id.cons_phone);
        consSpec = view.findViewById(R.id.cons_specs);
        consType = view.findViewById(R.id.cons_employ);
        consLoc = view.findViewById(R.id.cons_loc);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firebaseDatabaseReference.child("Contractor").child(firebaseUser.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("consultantName").exists()) {
                                String name = snapshot.child("consultantName").getValue().toString().trim();
                                consName.setText(name);
                            }
                            if (snapshot.child("emailAddress").exists()) {
                                String emailAddress = snapshot.child("emailAddress").getValue().toString().trim();
                                consEmail.setText(emailAddress);
                            }
                            if (snapshot.child("specialization").exists()) {
                                String specs = snapshot.child("specialization").getValue().toString().trim();
                                consSpec.setText(specs);
                            }
                            if (snapshot.child("phoneNumber").exists()) {
                                String phone = snapshot.child("phoneNumber").getValue().toString().trim();
                                consPhone.setText(phone);
                            }
                            if (snapshot.child("consultantLocation").exists()) {
                                String location = snapshot.child("consultantLocation").getValue().toString().trim();
                                consLoc.setText(location);
                            }
                            if (snapshot.child("category").exists()) {
                                String category = snapshot.child("category").getValue().toString().trim();
                                consType.setText(category);
                            }
                            if (snapshot.child("consultantImageUrl").exists()) {
                                String imageUrl = snapshot.child("consultantImageUrl").getValue().toString().trim();
                                Picasso.with(getContext()).load(imageUrl).into(consImage);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


        return view;

    }
}