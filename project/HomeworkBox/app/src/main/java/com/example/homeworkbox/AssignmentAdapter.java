package com.example.homeworkbox;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AssignmentAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<Assignment> assignments;

    public AssignmentAdapter(Context context, List<Assignment> as) {
        assignments = as;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return assignments.size();
    }

    @Override
    public Object getItem(int i) {
        return assignments.get(i);
    }

    @Override
    public long getItemId(int i) {
        // prob. not necessary but why not
        return assignments.get(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate(R.layout.assignment_listview_detail, null);
        TextView subjectTextView = (TextView) v.findViewById(R.id.subjectTextView);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.descriptionTextView);
        TextView deadlineTextView = (TextView) v.findViewById(R.id.deadlineTextView);
        RelativeLayout container = (RelativeLayout) v.findViewById(R.id.container);

        Assignment currAssignment = (Assignment) getItem(i);
        String subject = currAssignment.getSubject();
        String description = currAssignment.getDescription();
        Date deadlineDate = currAssignment.getDeadlineDate();
        String deadline = new SimpleDateFormat("dd.MM.yyyy").format(deadlineDate);

        subjectTextView.setText(subject);
        descriptionTextView.setText(description);
        deadlineTextView.setText("Deadline: " + deadline);

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + 24*60*60*1000);

        // Green background if assignment is done
        if (currAssignment.isDone()) {
            container.setBackgroundColor(v.getResources().getColor(R.color.green));
        }
        // Red background if assignment is late (deadline has passed)
        else if (today.after(deadlineDate)) {
            container.setBackgroundColor(v.getResources().getColor(R.color.red));
        }
        // Orange background if deadline is tomorrow
        else if (tomorrow.after(deadlineDate)) {
            container.setBackgroundColor(v.getResources().getColor(R.color.orange));
        }

        return v;
    }

}
