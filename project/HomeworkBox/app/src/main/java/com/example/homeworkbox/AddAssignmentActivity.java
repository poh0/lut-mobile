package com.example.homeworkbox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddAssignmentActivity extends AppCompatActivity {

    EditText subjectEditText;
    EditText descriptionEditText;
    EditText dateEditText;
    Button createAssignmentBtn;
    DatePickerDialog.OnDateSetListener setListener;

    Assignment assignment;

    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        createAssignmentBtn = (Button) findViewById(R.id.createAssignmentBtn);

        if ((assignment = (Assignment) getIntent().getSerializableExtra("assignment")) != null) {
            subjectEditText.setText(assignment.getSubject());
            descriptionEditText.setText(assignment.getDescription());
            createAssignmentBtn.setText("Update");

            Date deadline = assignment.getDeadlineDate();
            year = deadline.getYear() + 1900;
            month = deadline.getMonth();
            day = deadline.getDate();
            dateEditText.setText(day + "." + month + "." + year);
        } else {
            assignment = new Assignment();

            // Getting current year, month and day for the DatePickerDialog
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddAssignmentActivity.this, setListener, year, month, day);
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                assignment.setDeadlineDate(new Date(year - 1900, month, day));
                month += 1;
                String date = day + "." + month + "." + year;
                dateEditText.setText(date);
            }
        };

        createAssignmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = descriptionEditText.getText().toString();
                String subject = subjectEditText.getText().toString();

                if (description.isEmpty() || subject.isEmpty()) {
                    Toast.makeText(
                            AddAssignmentActivity.this,
                            "Please add subject and description.",
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                assignment.setDescription(description);
                assignment.setSubject(subject);

                Intent intent = new Intent();
                intent.putExtra("com.example.assignment", assignment);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}