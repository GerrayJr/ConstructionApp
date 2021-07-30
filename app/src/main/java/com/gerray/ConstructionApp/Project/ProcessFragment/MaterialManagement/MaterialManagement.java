package com.gerray.ConstructionApp.Project.ProcessFragment.MaterialManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.gerray.ConstructionApp.Earthworks;
import com.gerray.ConstructionApp.Finishing;
import com.gerray.ConstructionApp.Landscaping;
import com.gerray.ConstructionApp.R;
import com.gerray.ConstructionApp.Structures;

public class MaterialManagement extends AppCompatActivity implements View.OnClickListener {
    private ImageView earthwork, landscaping, structures, finishing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management);

        earthwork = findViewById(R.id.img_earthwork);
        earthwork.setOnClickListener(this);
        landscaping = findViewById(R.id.img_landscaping);
        landscaping.setOnClickListener(this);
        structures = findViewById(R.id.img_structures);
        structures.setOnClickListener(this);
        finishing = findViewById(R.id.img_finishing);
        finishing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == structures) {
            startActivity(new Intent(this, Structures.class));
        } else if (v == landscaping) {
            startActivity(new Intent(this, Landscaping.class));
        } else if (v == earthwork) {
            startActivity(new Intent(this, Earthworks.class));
        } else if (v == finishing) {
            startActivity(new Intent(this, Finishing.class));
        }

    }
}