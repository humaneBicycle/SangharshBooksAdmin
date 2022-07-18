package com.sangharsh.books;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import me.ibrahimsn.lib.OnItemReselectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    SmoothBottomBar smoothBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(new PreferenceGetter(this).getBoolean(PreferenceGetter.IS_MODE_SELECTED)) {
            if(new PreferenceGetter(this).getBoolean(PreferenceGetter.IS_DARK_MODE_ENABLED)) {
                setTheme(R.style.Theme_Dark);
            }else{
                setTheme(R.style.Theme_Light);
            }
        }else{
            if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
                setTheme(R.style.Theme_Dark);
            }else{
                setTheme(R.style.Theme_Light);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smoothBottomBar = findViewById(R.id.bottomBar);
//        smoothBottomBar.setOnItemReselectedListener(new OnItemReselectedListener() {
//            @Override
//            public void onItemReselect(int i) {
//                if(i==0){
//                    Log.d(TAG, "onItemReselect: ");
//                    //TODO create fragments and replace
//                }
//            }
//        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.greetings_holder, new GreetingFragment(), "greetings");
        ft.commit();
        setTheme(R.style.Theme_Dark);



    }


}