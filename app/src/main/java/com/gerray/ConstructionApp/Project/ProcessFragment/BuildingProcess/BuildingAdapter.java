package com.gerray.ConstructionApp.Project.ProcessFragment.BuildingProcess;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.ConstructionApp.R;

import java.util.ArrayList;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildViewHolder> {
    private final ArrayList<BuildingItems> mBuildingItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public BuildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.build_list_items, parent, false);
        return new BuildViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildViewHolder holder, int position) {
        BuildingItems currentItem = mBuildingItems.get(position);

        holder.mTextView.setText(currentItem.getBuildingProcess());
    }

    @Override
    public int getItemCount() {
        return mBuildingItems.size();
    }

    public BuildingAdapter(ArrayList<BuildingItems> buildingItemsArrayList) {
        mBuildingItems = buildingItemsArrayList;

    }

    public static class BuildViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;

        public BuildViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.buildTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
