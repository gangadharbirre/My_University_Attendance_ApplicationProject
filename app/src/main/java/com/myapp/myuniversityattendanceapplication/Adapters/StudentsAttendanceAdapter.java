package com.myapp.myuniversityattendanceapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.myuniversityattendanceapplication.Model.Model;
import com.myapp.myuniversityattendanceapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class StudentsAttendanceAdapter extends FirebaseRecyclerAdapter<Model, StudentsAttendanceAdapter.Viewholder> {

    String className;

    public StudentsAttendanceAdapter(@NonNull FirebaseRecyclerOptions<Model> options, String className) {

        super(options);
        //Getting className form the fragment
        this.className = className;

    }

    @Override
    protected void onBindViewHolder(@NonNull StudentsAttendanceAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting data from database using model class and assigning
        holder.StudentName.setText(model.getName());
        holder.CurrentAttendance.setText(model.getAttendance());
        holder.TotalWorkingDays.setText("Total Working days: " + model.getTotalDays());

        //Implementing OnClickListener
        holder.absent.setOnClickListener(view -> {

            //Getting the data from firebase
            FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child(model.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            //Assigning the values to model class
                            Model model12 = snapshot.getValue(Model.class);

                            //Incrementing the total working days
                            String current_totalDays = model12.getTotalDays();
                            int totalDays = Integer.parseInt(current_totalDays) + 1;
                            String updated_days = String.valueOf(totalDays);

                            //Setting the updated data to firebase
                            FirebaseDatabase.getInstance().getReference().child("Classes").child(className)
                                    .child(model12.getId()).child("totalDays").setValue(updated_days)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {

                                            //Setting the updated data to firebase
                                            FirebaseDatabase.getInstance().getReference().child("users").child("Students")
                                                    .child(model12.getId()).child("totalDays").setValue(updated_days);

                                            //Showing the toast message to user
                                            Toast.makeText(view.getContext(), "Absent", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Showing the toast message to user
                                            Toast.makeText(view.getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });


            //Showing the toast message to user
            Toast.makeText(view.getContext(), "absent", Toast.LENGTH_SHORT).show();
        });

        //Implementing onClickListener
        holder.present.setOnClickListener(view -> {

            //Getting the data from firebase
            FirebaseDatabase.getInstance().getReference().child("Classes").child(className).child(model.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            //Assigning all the values to model class
                            Model model1 = snapshot.getValue(Model.class);


                            //Incrementing the total working days
                            assert model1 != null;
                            String current_totalDays = model1.getTotalDays();
                            int totalDays = Integer.parseInt(current_totalDays) + 1;
                            String updated_days = String.valueOf(totalDays);


                            //incrementing the attended days
                            String current_attendance = model1.getAttendance();
                            int attendance = Integer.parseInt(current_attendance) + 1;
                            String updated_attendance = String.valueOf(attendance);

                            //setting the updated value to firebase
                            FirebaseDatabase.getInstance().getReference().child("Classes").child(className)
                                    .child(model1.getId()).child("totalDays").setValue(updated_days)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            FirebaseDatabase.getInstance().getReference().child("Classes").child(className)
                                                    .child(model1.getId()).child("attendance").setValue(updated_attendance)
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {

                                                            FirebaseDatabase.getInstance().getReference().child("users").child("Students")
                                                                    .child(model1.getId()).child("attendance").setValue(updated_attendance);

                                                            FirebaseDatabase.getInstance().getReference().child("users").child("Students")
                                                                    .child(model1.getId()).child("totalDays").setValue(updated_days);

                                                            Toast.makeText(view.getContext(), "Present", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(view.getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        } else {
                                            //Showing the Toast message to user
                                            Toast.makeText(view.getContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });


        });


    }


    @NonNull
    @Override
    public StudentsAttendanceAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_file, parent, false);
        return new Viewholder(view);
    }

    public void stopListening() {
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    static class Viewholder extends RecyclerView.ViewHolder {


        TextView StudentName, CurrentAttendance, TotalWorkingDays;
        Button absent, present;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //assigning the address of the materials
            StudentName = (TextView) itemView.findViewById(R.id.StudentName);
            absent = (Button) itemView.findViewById(R.id.absentBtn);
            present = (Button) itemView.findViewById(R.id.PresentBtn);
            CurrentAttendance = (TextView) itemView.findViewById(R.id.Current_Attendance);
            TotalWorkingDays = (TextView) itemView.findViewById(R.id.TotalWorkingDays);


        }
    }

}
