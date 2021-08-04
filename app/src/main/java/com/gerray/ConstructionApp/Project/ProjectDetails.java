package com.gerray.ConstructionApp.Project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gerray.ConstructionApp.R;

public class ProjectDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Button btnNext = findViewById(R.id.btn_profNext);
        btnNext.setOnClickListener(view -> startActivity(new Intent(ProjectDetails.this, ProjectTeam.class)));
    }
}