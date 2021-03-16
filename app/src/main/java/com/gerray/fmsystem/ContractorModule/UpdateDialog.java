package com.gerray.fmsystem.ContractorModule;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.gerray.fmsystem.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDialog extends AppCompatDialogFragment {
    private TextInputEditText edCost;
    private Spinner edStatus;
    private final String text;

    public UpdateDialog(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.ed_workdet, null, false);

        edStatus = view.findViewById(R.id.status_spinner);
        edCost = view.findViewById(R.id.update_cost);

        builder.setView(view)
                .setTitle("Update Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateWork(text);
                    }
                });
        return builder.create();
    }

    public void updateWork(String workID)
    {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Work Orders").child(workID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = edStatus.getSelectedItem().toString();
                Integer cost = Integer.valueOf(edCost.getText().toString().trim());

                databaseReference.child("cost").setValue(cost);
                databaseReference.child("status").setValue(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
