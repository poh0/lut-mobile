package com.example.homeworkbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView assignmentListView;
    RoomDB db;
    AssignmentDAO dao;
    List<Assignment> assignments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignmentListView = (ListView) findViewById(R.id.assignmentListView);

        db = RoomDB.getInstance(this);
        dao = db.assignmentDAO();

        // ADD SOME TESTING DATA
        Assignment a1 = new Assignment();
        a1.setSubject("Maths");
        a1.setDescription("p.23 ex.3,4,5");
        Assignment a2 = new Assignment();
        a2.setSubject("Chinese");
        a2.setDescription("Revise vocab for the test");

        assignments = new ArrayList<Assignment>();
        assignments.add(a1);
        assignments.add(a2);

        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(this, assignments);
        assignmentListView.setAdapter(assignmentAdapter);

        Button addAssignmentBtn = (Button) findViewById(R.id.addAssignmentBtn);
        addAssignmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAssignment
                        = new Intent(getApplicationContext(), AddAssignmentActivity.class);
                startActivity(newAssignment);
            }
        });
    }
}