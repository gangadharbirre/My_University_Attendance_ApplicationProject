package com.myapp.myuniversityattendanceapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.myapp.myuniversityattendanceapplication.Activity.GetStartedActivity;
import com.myapp.myuniversityattendanceapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView imageView;
    TextView userName;
    Button signOutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = (CircleImageView) view.findViewById(R.id.ProfileImageView);
        userName = (TextView) view.findViewById(R.id.UserNameTxt);
        signOutBtn = (Button) view.findViewById(R.id.SignOutBtn);

        //Getting user detials from GoogleSignin
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (acct != null) {
            userName.setText(acct.getDisplayName());
            Picasso.get().load(acct.getPhotoUrl()).into(imageView);
        }


        //implementing onClickListener to make the user signOut
        signOutBtn.setOnClickListener(this::onClick);

        return view;
    }

    private void onClick(View view1) {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        //GoogleSignInClient to access the current user
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //User Signout
                FirebaseAuth.getInstance().signOut();

                //Redirecting to starting Activity
                Intent intent = new Intent(getContext(), GetStartedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        });

    }
}