package com.myapp.myuniversityattendanceapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.myuniversityattendanceapplication.MainActivity;
import com.myapp.myuniversityattendanceapplication.R;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mSignInClient;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressBar;
    protected Spinner spinner;

    private final String[] roles = {"Teacher", "Student"};
    private String userRole = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = new ProgressDialog(this);
        progressBar.setTitle("Please Wait...");
        progressBar.setMessage("We are setting Everything for you...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        spinner = findViewById(R.id.Spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userRole = roles[i];
                Toast.makeText(getApplicationContext(), roles[i], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button signInButton = findViewById(R.id.GoogleSignInBtn);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("721996130793-oflq1tfe8gn5teh5npc0vgdljfktrf2l.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);

        signInButton.setOnClickListener(view -> {
            Intent intent = mSignInClient.getSignInIntent();
            startActivityForResult(intent, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (googleSignInAccountTask.isSuccessful()) {
                progressBar.show();
                try {
                    GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);

                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                String id = googleSignInAccount.getId();
                                String name = googleSignInAccount.getDisplayName();
                                String mail = googleSignInAccount.getEmail();
                                String pic = Objects.requireNonNull(googleSignInAccount.getPhotoUrl()).toString();

                                HashMap<String, Object> user_details = new HashMap<>();
                                user_details.put("id", id);
                                user_details.put("name", name);
                                user_details.put("mail", mail);
                                user_details.put("profilepic", pic);
                                user_details.put("role", userRole);

                                assert id != null;
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(id);
                                userRef.updateChildren(user_details).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DatabaseReference roleRef;
                                        if ("Teacher".equals(userRole)) {
                                            roleRef = FirebaseDatabase.getInstance().getReference().child("users").child("Teachers");
                                        } else {
                                            roleRef = FirebaseDatabase.getInstance().getReference().child("users").child("Students");
                                            user_details.put("totalDays", "0");
                                            user_details.put("attendance", "0");
                                        }

                                        roleRef.child(id).updateChildren(user_details).addOnCompleteListener(task11 -> {
                                            if (task11.isSuccessful()) {
                                                progressBar.cancel();
                                                Class<?> targetActivity = "Teacher".equals(userRole) ? TeacherActivity.class : MainActivity.class;
                                                Intent intent = new Intent(getApplicationContext(), targetActivity);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
