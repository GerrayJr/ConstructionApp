package com.gerray.ConstructionApp.Project.ProcessFragment.Approvals;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gerray.ConstructionApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ApprovalsPage extends AppCompatActivity {

    private FirebaseAuth auth;
    private Spinner approvals;
    private Button btnAdd;
    private DatabaseReference databaseReference;

    private FirebaseRecyclerOptions<ApprovalInfo> options;
    private FirebaseRecyclerAdapter<ApprovalInfo, ApprovalViewHolder> adapter;

    private ProgressDialog dialog;

    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.startListening();
        adapter.notifyDataSetChanged();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvals_page);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


//        addApproval = findViewById(R.id.btn_add_approval);
//        addApproval.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ApprovalsPage.this, ApprovalPopUp.class));
//            }
//        });
//        nemaApproval = findViewById(R.id.btn_nema);
//        nemaApproval.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ApprovalsPage.this, NemaActivity.class));
//            }
//        });
//        ncaApproval = findViewById(R.id.btn_nca);
//        ncaApproval.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ApprovalsPage.this, NcaActivity.class));
//            }
//        });
//        cgApprovals = findViewById(R.id.btn_countgovt);
//        cgApprovals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ApprovalsPage.this, CountyGovtActivity.class));
//            }
//        });
//        others = findViewById(R.id.btn_add_approval);
//        others.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ApprovalsPage.this, ApprovalPopUp.class));
//            }
//        });
    }

    private void saveApproval() {
        dialog.setMessage("Saving Approval");
        dialog.show();
        dialog.setCancelable(false);
        String approval = approvals.getSelectedItem().toString().trim();
        if (TextUtils.isEmpty(approval)) {
            Toast.makeText(this, "Select Approval", Toast.LENGTH_SHORT).show();
            return;
        }
        ApprovalInfo approvalInfo = new ApprovalInfo(approval);
        databaseReference.child(approval).setValue(approvalInfo);
        dialog.dismiss();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

    }
}