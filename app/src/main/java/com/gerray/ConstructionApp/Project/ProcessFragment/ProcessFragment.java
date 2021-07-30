package com.gerray.ConstructionApp.Project.ProcessFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.gerray.ConstructionApp.Location;
import com.gerray.ConstructionApp.Project.ProcessFragment.Approvals.ApprovalsPage;
import com.gerray.ConstructionApp.Project.ProcessFragment.MaterialManagement.MaterialManagement;
import com.gerray.ConstructionApp.Project.ProcessFragment.Team.Team;
import com.gerray.ConstructionApp.R;

public class ProcessFragment extends Fragment {
    private ImageView location, team, approvals, material;
    private Button btnFinish;

    public ProcessFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_process, container, false);
        location = view.findViewById(R.id.img_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        location = view.findViewById(R.id.img_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Location.class));
            }
        });
        team = view.findViewById(R.id.img_team);
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Team.class));
            }
        });
        approvals = view.findViewById(R.id.img_approvals);
        approvals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ApprovalsPage.class));
            }
        });
        material = view.findViewById(R.id.img_materials);
        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MaterialManagement.class));
            }
        });

        btnFinish = view.findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;

    }
}