package com.myapp.myuniversityattendanceapplication.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.myuniversityattendanceapplication.Activity.AddStudentsActivity;
import com.myapp.myuniversityattendanceapplication.Model.Model;
import com.myapp.myuniversityattendanceapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CreateClassAdapter extends FirebaseRecyclerAdapter<Model, CreateClassAdapter.Viewholder> {

    public CreateClassAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull CreateClassAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting data from database using model class and assigning
        holder.classNameTxt.setText(model.getClassName());
        holder.classNameTxt.setOnClickListener(view -> {
            //Calling next intent
            Intent intent = new Intent(view.getContext(), AddStudentsActivity.class);
            //Passing class name to next intent
            intent.putExtra("className", model.getClassName());
            view.getContext().startActivity(intent);
        });


    }


    @NonNull
    @Override
    public CreateClassAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_classes_file, parent, false);
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


