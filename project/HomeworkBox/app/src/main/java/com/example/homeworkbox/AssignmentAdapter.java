package com.example.homeworkbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

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

        Assignment currAssignment = (Assignment) getItem(i);
        String subject = currAssignment.getSubject();
        String description = currAssignment.getDescription();

        subjectTextView.setText(subject);
        descriptionTextView.setText(description);

        return v;
    }
}
