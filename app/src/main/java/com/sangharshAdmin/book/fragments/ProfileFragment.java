package com.sangharshAdmin.book.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.sangharshAdmin.book.LoginActivity;
import com.sangharshAdmin.book.R;
import com.sangharshAdmin.book.SangharshBooks;

public class ProfileFragment extends Fragment {

    SwitchMaterial switchMaterial;
    TextView aboutUs,emailAuth,logout;
    TextView privacyPolicy;
    SangharshBooks sangharshBooks;

    public ProfileFragment(){}



    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI(view);

        switchMaterial.setChecked(Configuration.UI_MODE_NIGHT_YES == (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK));
        sangharshBooks = (SangharshBooks)getActivity().getApplication();

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (compoundButton.isChecked()) {
                        //Log.d(TAG, "onCheckedChanged: 11");
                        //getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        sangharshBooks.setDarkMode(true);
                    } else {
                        //Log.d(TAG, "onCheckedChanged: 12");
                        //getActivity().recreate();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        sangharshBooks.setDarkMode(false);
                    }





            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Signed Out Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(),LoginActivity.class));
            }
        });
        try{
            emailAuth.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }catch(Exception e){
            Log.i("sba", "email not provided!: "+e.toString());
            emailAuth.setText("EMAIL NOT PROVIDED ERROR");

        }

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void initUI(View view){
        switchMaterial = view.findViewById(R.id.active_mode_theme);
        aboutUs = view.findViewById(R.id.about_us);
        privacyPolicy = view.findViewById(R.id.privacy_policy);
        emailAuth=view.findViewById(R.id.auth_email);
        logout = view.findViewById(R.id.logout);
    }
}