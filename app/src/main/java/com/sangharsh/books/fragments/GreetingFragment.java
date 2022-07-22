package com.sangharsh.books.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sangharsh.books.R;

import java.util.Calendar;
import java.util.Date;

public class GreetingFragment extends Fragment {
;
    TextView greetings;
    ImageView bell;

    public GreetingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_greeting, container, false);


        bell = view.findViewById(R.id.notification_greetings_flag);
        greetings = view.findViewById(R.id.greetings_frag);
        greetings.setText(getGreetings());

        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private String getGreetings(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String greeting ;
        if(hour>= 12 && hour < 17){
            greeting = getResources().getString(R.string.morning);
        } else if(hour >= 17 && hour < 21){
            greeting = getResources().getString(R.string.evening);
        } else if(hour >= 21){
            greeting = getResources().getString(R.string.night);
        } else {
            greeting = getResources().getString(R.string.morning);
        }
        return greeting;
    }
}