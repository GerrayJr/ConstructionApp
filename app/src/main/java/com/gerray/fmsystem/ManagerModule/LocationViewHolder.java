package com.gerray.fmsystem.ManagerModule;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.fmsystem.R;

public class LocationViewHolder extends RecyclerView.ViewHolder {
    public TextView tvLat, tvLong;

    public LocationViewHolder(@NonNull View itemView) {
        super(itemView);
        tvLat = itemView.findViewById(R.id.tv_latitude);
        tvLong = itemView.findViewById(R.id.tv_longitude);
    }
}
