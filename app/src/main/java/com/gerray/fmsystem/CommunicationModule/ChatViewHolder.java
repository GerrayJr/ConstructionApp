package com.gerray.fmsystem.CommunicationModule;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.fmsystem.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    public TextView contactName, lesseeName, time;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        contactName = itemView.findViewById(R.id.tv_contactName);
        lesseeName = itemView.findViewById(R.id.tv_lesseeName);
        time = itemView.findViewById(R.id.tv_time);
    }
}
