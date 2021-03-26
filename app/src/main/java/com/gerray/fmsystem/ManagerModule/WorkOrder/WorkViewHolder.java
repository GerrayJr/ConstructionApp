package com.gerray.fmsystem.ManagerModule.WorkOrder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.fmsystem.R;

public class WorkViewHolder extends RecyclerView.ViewHolder {
    public TextView tvWork, tvConsultant, tvWorkDate, tvStatus, textView;

    public WorkViewHolder(@NonNull View itemView) {
        super(itemView);
        tvWork = itemView.findViewById(R.id.tv_work);
        tvConsultant = itemView.findViewById(R.id.tv_consultant);
        tvWorkDate = itemView.findViewById(R.id.tv_workDate);
        textView = itemView.findViewById(R.id.tv_facilityInd);
        tvStatus = itemView.findViewById(R.id.tv_status);
    }
}
