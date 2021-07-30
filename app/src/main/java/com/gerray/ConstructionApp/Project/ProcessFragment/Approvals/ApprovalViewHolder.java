package com.gerray.ConstructionApp.Project.ProcessFragment.Approvals;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.ConstructionApp.R;

public class ApprovalViewHolder extends RecyclerView.ViewHolder {
    public TextView appName;

    public ApprovalViewHolder(@NonNull View itemView) {
        super(itemView);

        appName = itemView.findViewById(R.id.approvalTxt);
    }
}
