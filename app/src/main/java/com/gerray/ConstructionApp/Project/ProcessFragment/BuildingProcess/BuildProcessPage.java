package com.gerray.ConstructionApp.Project.ProcessFragment.BuildingProcess;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gerray.ConstructionApp.R;

import java.util.ArrayList;

public class BuildProcessPage extends AppCompatActivity {
    private ArrayList<BuildingItems> buildingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_process_page);

        buildingItems = new ArrayList<>();
        buildingItems.add(new BuildingItems("Earthwork"));
        buildingItems.add(new BuildingItems("Sub-Structure"));
        buildingItems.add(new BuildingItems("Super-Structure"));
        buildingItems.add(new BuildingItems("Finishing"));
        buildingItems.add(new BuildingItems("Landscaping"));


        RecyclerView recyclerView = findViewById(R.id.building_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        recyclerView.setHasFixedSize(true);

        BuildingAdapter adapter = new BuildingAdapter(buildingItems);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BuildingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                buildingItems.get(position);
                if (position == 0) {

                } else {
                }
            }
        });
    }
}
