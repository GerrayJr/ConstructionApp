package com.gerray.fmsystem.ManagerModule.Lessee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gerray.fmsystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class LesseeFragment extends Fragment {
    private FloatingActionButton addLessee;

    public LesseeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lessee, container, false);

        addLessee = view.findViewById(R.id.fab_lessee);
        addLessee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchLessee.class));
            }
        });
        return view;
    }
}