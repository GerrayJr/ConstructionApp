package com.gerray.fmsystem.LesseeModule;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.fmsystem.R;

public class LesseeRequestHolder extends RecyclerView.ViewHolder {
    public TextView tvDate, tvRoom, tvLessee, tvDescription;

    public LesseeRequestHolder(@NonNull View itemView) {
        super(itemView);
        tvDate = itemView.findViewById(R.id.tv_requestDate);
        tvRoom = itemView.findViewById(R.id.tv_room);
        tvLessee = itemView.findViewById(R.id.tv_lessee);
        tvDescription = itemView.findViewById(R.id.tv_description);
    }
}
