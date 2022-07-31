package com.example.homeworkbox;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView assignmentListView;
    RoomDB db;
    AssignmentDAO dao;
    List<Assignment> assignments;
    AssignmentAdapter assignmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignmentListView = (ListView) findViewById(R.id.assignmentListView);

        db = RoomDB.getInstance(this);
        dao = db.assignmentDAO();

        assignments = dao.getAll();

        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(this, assignments);
        assignmentListView.setAdapter(assignmentAdapter);

        Button addAssignmentBtn = (Button) findViewById(R.id.addAssignmentBtn);
        addAssignmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAssignment
                        = new Intent(getApplicationContext(), AddAssignmentActivity.class);
                newAssignmentResultLauncher.launch(newAssignment);
            }
        });

    }

    ActivityResultLauncher<Intent> newAssignmentResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Assignment newAssignment = (Assignment) data.getSerializableExtra("com.example.assignment");
                        dao.insert(newAssignment);
                        assignments.clear();
                        assignments.addAll(dao.getAll());
                        ((AssignmentAdapter) assignmentListView.getAdapter()).notifyDataSetChanged();
                    }
                }
            });
}