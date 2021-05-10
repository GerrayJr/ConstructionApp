package com.gerray.fmsystem.Transactions;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.fmsystem.R;

public class TransactionViewModel extends RecyclerView.ViewHolder {
    public TextView payee, transAmount, transDesc, transDate, personType;

    public TransactionViewModel(@NonNull View itemView) {
        super(itemView);
        payee = itemView.findViewById(R.id.tv_payee);
        transAmount = itemView.findViewById(R.id.tv_transAmount);
        transDate = itemView.findViewById(R.id.tv_transDate);
        transDesc = itemView.findViewById(R.id.tv_transDesc);
        personType = itemView.findViewById(R.id.tv_personDes);

    }
}
