package com.gerray.fmsystem.ManagerModule.Consultants;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.fmsystem.R;

public class ConsultantViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvCategory, tvloc;


    public ConsultantViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.consCardName);
        tvCategory = itemView.findViewById(R.id.consCardCat);
        tvloc = itemView.findViewById(R.id.consCardLoc);
    }
}
