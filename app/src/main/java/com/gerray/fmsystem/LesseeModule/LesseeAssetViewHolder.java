package com.gerray.fmsystem.LesseeModule;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.fmsystem.R;

public class LesseeAssetViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvModel, tvloc, tvDate;
    public ImageView imageView;


    public LesseeAssetViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_asset_name);
        tvModel = itemView.findViewById(R.id.tv_asset_model);
        tvloc = itemView.findViewById(R.id.tv_asset_location);
        tvDate = itemView.findViewById(R.id.tv_asset_date);

        imageView = itemView.findViewById(R.id.asset_image_item);

    }
}
