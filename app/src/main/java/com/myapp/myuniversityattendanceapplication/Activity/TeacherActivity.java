package com.myapp.myuniversityattendanceapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.myapp.myuniversityattendanceapplication.Fragments.AttendanceFragment;
import com.myapp.myuniversityattendanceapplication.Fragments.CreateClassFragment;
import com.myapp.myuniversityattendanceapplication.Fragments.ProfileFragment;
import com.myapp.myuniversityattendanceapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeacherActivity extends AppCompatActivity {

    private static final int HOME_MENU_ID = R.id.HomeMenu;
    private static final int ATTENDANCE_MENU_ID = R.id.AttendanceMenu;
    private static final int PROFILE_MENU_ID = R.id.ProfileMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        BottomNavigationView bottomNavigationView = findViewById(R.id.TeacherBottomNavigationView);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.TeacherFragmentContainer, new CreateClassFragment())
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod =
            item -> {
                Fragment fragment = null;

                if (item.getItemId() == HOME_MENU_ID) {
                    fragment = new CreateClassFragment();
                } else if (item.getItemId() == ATTENDANCE_MENU_ID) {
                    fragment = new AttendanceFragment();
                } else if (item.getItemId() == PROFILE_MENU_ID) {
                    fragment = new ProfileFragment();
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.TeacherFragmentContainer, fragment)
                            .commit();
                    return true;
                }

                return false;
            };
}
