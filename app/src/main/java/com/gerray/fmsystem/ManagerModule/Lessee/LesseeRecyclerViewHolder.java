package com.gerray.fmsystem.ManagerModule.Lessee;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.fmsystem.R;

public class LesseeRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView contactName, lesseeName, activity, room;

    public LesseeRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        contactName = itemView.findViewById(R.id.tv_contactName);
        lesseeName = itemView.findViewById(R.id.tv_lesseeName);
        activity = itemView.findViewById(R.id.tv_lesseeActivity);
        room = itemView.findViewById(R.id.tv_facilityInd);
    }
}
