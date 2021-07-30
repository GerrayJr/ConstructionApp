package com.gerray.ConstructionApp.Home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.ConstructionApp.R;

public class ProjectViewHolder extends RecyclerView.ViewHolder

{

    public TextView tvName, tvComp, tvloc, tvDate;


    public ProjectViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.projectName);
        tvDate = itemView.findViewById(R.id.tv_creationDate);
    }
}
