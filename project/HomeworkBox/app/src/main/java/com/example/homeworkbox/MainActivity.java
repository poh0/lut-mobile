package com.example.homeworkbox;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView assignmentListView;
    RoomDB db;
    AssignmentDAO dao;
    List<Assignment> assignments;
    List<Assignment> filteredAssignments;
    AssignmentAdapter assignmentAdapter;

    private int currentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentFilter = 0;

        // Init db
        db = RoomDB.getInstance(this);
        dao = db.assignmentDAO();

        // Get assignments from db
        assignments = dao.getAll();
        filteredAssignments = new ArrayList<Assignment>();
        filteredAssignments.addAll(assignments);

        // Initialize assignmentListView
        assignmentListView = (ListView) findViewById(R.id.assignmentListView);
        assignmentAdapter = new AssignmentAdapter(this, filteredAssignments);
        assignmentListView.setAdapter(assignmentAdapter);
        assignmentListView.setEmptyView(findViewById(R.id.emptyElement));

        // Initialize filter dropdown
        Spinner filterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        String[] options = getResources().getStringArray(R.array.options);

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(
                MainActivity.this, R.layout.spinner_item_layout, options);
        filterAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
        filterSpinner.setAdapter(filterAdapter);

        // Apply filter when selected
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // if chosen filter is the current filter, do nothing
                if (currentFilter == i)
                    return;
                currentFilter = i;
                filter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // auto-generated
            }
        });

        // Initialize addAssignmentBtn
        Button addAssignmentBtn = (Button) findViewById(R.id.addAssignmentBtn);
        addAssignmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the view for adding an assignment
                Intent newAssignment
                        = new Intent(getApplicationContext(), AddAssignmentActivity.class);
                newAssignmentResultLauncher.launch(newAssignment);
            }
        });

        // Click listener for updating an assignment
        assignmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Same as adding a new assignment, but we provide the clicked assignment
                // as extra
                Intent updateAssignment
                        = new Intent(getApplicationContext(), AddAssignmentActivity.class);
                updateAssignment.putExtra("assignment", filteredAssignments.get(i));
                newAssignmentResultLauncher.launch(updateAssignment);
            }
        });

        // Long click listener for deleting and marking as done
        assignmentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {

                final AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setMessage("Choose action");

                Assignment currentAssignment = filteredAssignments.get(itemIndex);
                final String doneTitle = currentAssignment.isDone()
                        ? "Mark as undone"
                        : "Mark as done";

                b.setPositiveButton(doneTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentAssignment.setDone(!currentAssignment.isDone());
                        dao.insert(currentAssignment);
                        assignments.clear();
                        assignments.addAll(dao.getAll());
                        filter();
                    }
                });
                b.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.delete(filteredAssignments.get(itemIndex));
                        assignments.clear();
                        assignments.addAll(dao.getAll());
                        filter();
                    }
                });
                b.show();
                return true;
            }
        });
    }

    // Filtering logic
    // This has to be run every time a change to assignments is made
    public void filter() {
        filteredAssignments.clear();
        if (currentFilter == 0) {
            resetFilter();
            return;
        }
        for (Assignment as : assignments) {
            if (as.isDone() && currentFilter == 1) {
                filteredAssignments.add(as);
            } else if (!as.isDone() && currentFilter == 2) {
                filteredAssignments.add(as);
            }
        }
        // Update list after filtering
        ((AssignmentAdapter) assignmentListView.getAdapter()).notifyDataSetChanged();
    }

    public void resetFilter() {
        filteredAssignments.clear();
        filteredAssignments.addAll(assignments);
        ((AssignmentAdapter) assignmentListView.getAdapter()).notifyDataSetChanged();
    }

    // The old method was deprecated so I had to use this monster
    // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
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
                        filter();
                    }
                }
            });
}