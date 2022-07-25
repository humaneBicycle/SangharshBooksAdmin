package com.sangharsh.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sangharsh.books.adapter.AddDirectoryBottomSheetAdapter;
import com.sangharsh.books.fragments.BookmarksFragment;
import com.sangharsh.books.fragments.DownloadsFragment;
import com.sangharsh.books.fragments.GreetingFragment;
import com.sangharsh.books.fragments.HomeFragment;
import com.sangharsh.books.fragments.ProfileFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    SmoothBottomBar smoothBottomBar;
    FloatingActionButton fab;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    setTheme(R.style.Theme_Dark);
                    ((SangharshBooks)getApplication()).setDarkMode(true);
                    Log.d("sba", "onCreate: 21");
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    setTheme(R.style.Theme_Light);
                    ((SangharshBooks)getApplication()).setDarkMode(false);
                    Log.d("sba", "onCreate: 22");
                    // process
                    break;
            }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smoothBottomBar = findViewById(R.id.bottomBar);
        fab = findViewById(R.id.add_home_admin);

        if(savedInstanceState!=null){
            smoothBottomBar.setItemActiveIndex(3);

        }
        if(savedInstanceState==null){
            homeFragment = new HomeFragment();
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.body_holder_main_activity, homeFragment, "home");
            ft1.commit();
        }

        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if(i==0) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    if(homeFragment==null) {
                        homeFragment = new HomeFragment();
                    }
                    fragmentTransaction.replace(R.id.body_holder_main_activity, homeFragment, "home");
                    fragmentTransaction.commit();
                }else if(i==1){
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.body_holder_main_activity, new DownloadsFragment(), "downloads");
                    fragmentTransaction.commit();
                }else if(i==2){

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.body_holder_main_activity, new BookmarksFragment(), "bookmarks");
                    fragmentTransaction.commit();
                }else if(i==3){

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.body_holder_main_activity, new ProfileFragment(), "profile");
                    fragmentTransaction.commit();
                }
                Log.d("sba", "onItemSelect: "+String.valueOf(i));

                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDirectoryBottomSheetAdapter bottomSheet = new AddDirectoryBottomSheetAdapter(MainActivity.this,(SangharshBooks) getApplication());
                bottomSheet.show(getSupportFragmentManager(), "addBottomSheet");
                //bottomSheet.attackCallback(homeFragment);
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.greetings_holder, new GreetingFragment(), "greetings");
        ft.commit();


        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blur);
        Bitmap newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
// create a canvas where we can draw on
        Canvas canvas = new Canvas(newBitmap);
// create a paint instance with alpha
        Paint alphaPaint = new Paint();
        alphaPaint.setAlpha(210);
// now lets draw using alphaPaint instance
        canvas.drawBitmap(originalBitmap, 0, 0, alphaPaint);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), newBitmap);
        final float roundPx = (float) originalBitmap.getWidth() * 0.06f;
        roundedBitmapDrawable.setCornerRadius(roundPx);
        smoothBottomBar.setBackground(roundedBitmapDrawable);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
}