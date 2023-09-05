package com.myapp.myuniversityattendanceapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.myuniversityattendanceapplication.Model.Model;
import com.myapp.myuniversityattendanceapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AddStudentsAdapter extends FirebaseRecyclerAdapter<Model, AddStudentsAdapter.Viewholder> {

    String className;

    public AddStudentsAdapter(@NonNull FirebaseRecyclerOptions<Model> options, String className) {

        super(options);

        //Getting classname form the fragment
        this.className = className;

    }

    @Override
    protected void onBindViewHolder(@NonNull AddStudentsAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting data from database using model class and assigning
        holder.studentNameTxt.setText(model.getName());
        holder.addStudetBtn.setOnClickListener(view -> {

            //HashMap to store data and to add it to firebase
            HashMap<String, Object> studentDetails = new HashMap<>();
            studentDetails.put("name", model.getName());
            studentDetails.put("mail", model.getMail());
            studentDetails.put("id", model.getId());
            studentDetails.put("attendance", "0");
            studentDetails.put("className", className);
            studentDetails.put("totalDays", "0");

            //Adding data to firebase using path
            FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child(model.getId())
                    .setValue(studentDetails).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //Adding data to firebase using path
                            FirebaseDatabase.getInstance().getReference().child("users").child("Students").child(model.getId())
                                    .updateChildren(studentDetails).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {

                                            //Adding data to firebase using path
                                            FirebaseDatabase.getInstance().getReference().child("users").child("Students").child(model.getId())
                                                    .child("class").setValue(className)
                                                    .addOnCompleteListener(task11 -> {
                                                        if (task11.isSuccessful()) {
                                                            //Showing Toast Message to user
                                                            Toast.makeText(view.getContext(), "Student Added Successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            //Showing Toast Message to user
                                                            Toast.makeText(view.getContext(), "Please,Try Again", Toast.LENGTH_SHORT).show();
                                                        }

                                                    });
                                        } else {
                                            //Showing Toast Message to user
                                            Toast.makeText(view.getContext(), "Please,Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            //Showing Toast Message to user
                            Toast.makeText(view.getContext(), "Please,Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });

        });


    }


    @NonNull
    @Override
    public AddStudentsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_student_file, parent, false);
        return new Viewholder(view);
    }

    public void stopListening() {
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    static class Viewholder extends RecyclerView.ViewHolder {


        TextView studentNameTxt;
        Button addStudetBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //assigning the address of the materials
            studentNameTxt = itemView.findViewById(R.id.StudentNameTxt);
            addStudetBtn = itemView.findViewById(R.id.AddStudentBtn);

        }
    }

}

