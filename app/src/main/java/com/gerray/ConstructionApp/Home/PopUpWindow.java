package com.gerray.ConstructionApp.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.gerray.ConstructionApp.Project.ProjectDetails;
import com.gerray.ConstructionApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class PopUpWindow extends AppCompatActivity {
    private EditText project_name, project_company, project_location;
    private Button btnProjectReg;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .5));

        project_name = findViewById(R.id.proj_name);
        btnProjectReg = findViewById(R.id.info_continue);
        btnProjectReg.setOnClickListener(v ->{}
                //createProject()
//                startActivity(new Intent(this, ProjectDetails.class))
        );
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Projects").child(currentUser.getUid());
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    private void createProject() {
        progressDialog.setMessage("Let us Begin");
        progressDialog.show();
        final String name = project_name.getText().toString().trim();
        DateFormat dateFormat = DateFormat.getDateInstance();
        Date date = new Date();
        final String creationDate = dateFormat.format(date);
        final String projectID = UUID.randomUUID().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        ProjectCreate projectCreate = new ProjectCreate(name, creationDate, projectID);
        databaseReference.child(name).setValue(projectCreate);

        progressDialog.dismiss();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
//        PopUpWindow.this.finish();
//        startActivity(new Intent(this, ProjectDetails.class));
    }
}