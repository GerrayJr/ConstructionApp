package com.gerray.fmsystem.ContractorModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.fmsystem.ManagerModule.TransactionActivity;
import com.gerray.fmsystem.ManagerModule.WorkOrder.DetailsClass;
import com.gerray.fmsystem.ManagerModule.WorkOrder.WorkViewHolder;
import com.gerray.fmsystem.R;
import com.gerray.fmsystem.Transactions.TransactionDetails;
import com.gerray.fmsystem.Transactions.TransactionViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ContractorTransactions extends AppCompatActivity {

    private DatabaseReference reference;
    FirebaseRecyclerOptions<TransactionDetails> firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<TransactionDetails, TransactionViewModel> adapter;
    FirebaseUser firebaseUser;

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    public void onStart() {
        super.onStart();

        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_transactions);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Transactions");

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<TransactionDetails>().setQuery(reference1, TransactionDetails.class).build();
        adapter = new FirebaseRecyclerAdapter<TransactionDetails, TransactionViewModel>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TransactionViewModel holder, int position, @NonNull TransactionDetails model) {
                if (model.getPayeeID().equals(firebaseUser.getUid())) {
                    holder.transDate.setText(model.getTransactionDate());
                    holder.transDesc.setText(model.getTransactionDescription());
                    holder.transAmount.setText(model.getCost());

                    String consID = model.getPayeeID();
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(consID);
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String firstName = null, secondName = null;
                            if (snapshot.child("firstName").exists()) {
                                firstName = Objects.requireNonNull(snapshot.child("firstName").getValue()).toString().trim();
                            }
                            if (snapshot.child("secondName").exists()) {
                                secondName = Objects.requireNonNull(snapshot.child("secondName").getValue()).toString().trim();
                            }

                            final String payeeName = firstName + " " + secondName;
                            holder.payee.setText(payeeName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    holder.itemView.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.itemView.setLayoutParams(params);
                }
            }

            @NonNull
            @Override
            public TransactionViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TransactionViewModel(LayoutInflater.from(ContractorTransactions.this).inflate(R.layout.transaction_card, parent, false));
            }
        };

        RecyclerView recyclerView1 = findViewById(R.id.recycler_view_transactions);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(adapter);
        adapter.startListening();

    }
}