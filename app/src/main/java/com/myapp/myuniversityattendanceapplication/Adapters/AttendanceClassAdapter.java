package com.myapp.myuniversityattendanceapplication.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.myuniversityattendanceapplication.Activity.AddStudentsActivity;
import com.myapp.myuniversityattendanceapplication.Activity.StudentsAttendaceActivity;
import com.myapp.myuniversityattendanceapplication.Model.Model;
import com.myapp.myuniversityattendanceapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AttendanceClassAdapter extends FirebaseRecyclerAdapter<Model, AttendanceClassAdapter.Viewholder> {

    public AttendanceClassAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull AttendanceClassAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting data from database using model class and assigning
        holder.classNameTxt.setText(model.getClassName());
        holder.classNameTxt.setOnClickListener(view -> {

            //Calling another intent
            Intent intent = new Intent(view.getContext(), StudentsAttendaceActivity.class);
            //Passing data to the next intent
            intent.putExtra("className", model.getClassName());
            Toast.makeText(view.getContext(), model.getClassName(), Toast.LENGTH_SHORT).show();
            //Starting next intent
            view.getContext().startActivity(intent);
        });


    }


    @NonNull
    @Override
    public AttendanceClassAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_class_file, parent, false);
        return new Viewholder(view);
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    static class Viewholder extends RecyclerView.ViewHolder {


        TextView classNameTxt;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //assigning the address of the materials
            classNameTxt = itemView.findViewById(R.id.ClassNameTxt);


        }
    }

}


