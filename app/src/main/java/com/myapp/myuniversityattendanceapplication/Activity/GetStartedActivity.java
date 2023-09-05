package com.myapp.myuniversityattendanceapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.myuniversityattendanceapplication.R;

public class GetStartedActivity extends AppCompatActivity {

    Button getStartedBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        getStartedBtn = findViewById(R.id.GetStartedBtn);

        //OnclickListener implementation to change the activity
        getStartedBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

}
