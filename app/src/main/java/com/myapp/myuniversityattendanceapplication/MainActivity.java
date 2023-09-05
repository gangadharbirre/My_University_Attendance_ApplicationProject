package com.myapp.myuniversityattendanceapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.myuniversityattendanceapplication.Activity.GetStartedActivity;
import com.myapp.myuniversityattendanceapplication.Activity.TeacherActivity;
import com.myapp.myuniversityattendanceapplication.Model.Model;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView nameTxt, mailTxt, totalWorkingDaysTxt, totalPresentsTxt, totalAbsentsTxt, percentageTxt;
    Button askPermissionBtn, signOutBtn;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        circleImageView = findViewById(R.id.ProfileImageView);
        nameTxt = findViewById(R.id.UserNameTxt);
        mailTxt = findViewById(R.id.MailTxt);
        totalWorkingDaysTxt = findViewById(R.id.TotalWorkingDaysTxt);
        totalPresentsTxt = findViewById(R.id.TotalPresentsTxt);
        totalAbsentsTxt = findViewById(R.id.TotalAbsentsTxt);
        percentageTxt = findViewById(R.id.AttendancePercentageTxt);

        askPermissionBtn = findViewById(R.id.AskPermissionBtn);
        signOutBtn = findViewById(R.id.SignOutBtn);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            nameTxt.setText(acct.getDisplayName());
            Picasso.get().load(acct.getPhotoUrl()).into(circleImageView);
            mailTxt.setText(acct.getEmail());
        }

        askPermissionBtn.setOnClickListener(view -> {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            String[] recipients = new String[]{"xyz@gmail.com"};
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Text Here....");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Mail Text Here....");
            emailIntent.setType("text/plain");
            startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
        });

        signOutBtn.setOnClickListener(view -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), GetStartedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            Intent intent = new Intent(MainActivity.this, GetStartedActivity.class);
            startActivity(intent);
        } else {
            String id = Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(getApplicationContext())).getId();
            assert id != null;
            databaseReference.child("AllUsers").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Model model = snapshot.getValue(Model.class);
                    assert model != null;
                    String role = model.getRole();
                    if ("Teacher".equals(role)) {
                        Intent intent = new Intent(getApplicationContext(), TeacherActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        assert acct != null;
                        databaseReference.child("users").child("Students").child(Objects.requireNonNull(acct.getId()))
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        Model model = snapshot.getValue(Model.class);
                                        assert model != null;
                                        String totalWorkingDays = model.getTotalDays().trim();
                                        String totalPresentDays = model.getAttendance().trim();

                                        int workingDays = Integer.parseInt(totalWorkingDays);
                                        int presentDays = Integer.parseInt(totalPresentDays);
                                        int absentDays = workingDays - presentDays;

                                        int percentage = workingDays == 0 ? 0 : (presentDays * 100) / workingDays;

                                        percentageTxt.setText("Attendance Percentage(%): " + percentage);
                                        totalWorkingDaysTxt.setText("Total Working Days: " + model.getTotalDays());
                                        totalPresentsTxt.setText("Total Present Days: " + model.getAttendance());
                                        totalAbsentsTxt.setText("Total Absent Days: " + absentDays);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }
    }
}

