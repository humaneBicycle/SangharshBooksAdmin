package com.sangharshAdmin.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sangharshAdmin.books.adapter.AddDirectoryBottomSheetAdapter;
import com.sangharshAdmin.books.fragments.BookmarksFragment;
import com.sangharshAdmin.books.fragments.DownloadsFragment;
import com.sangharshAdmin.books.fragments.GreetingFragment;
import com.sangharshAdmin.books.fragments.HomeFragment;
import com.sangharshAdmin.books.fragments.ProfileFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements UIUpdateHomeFrag{

    SmoothBottomBar smoothBottomBar;
    FloatingActionButton fab;
    HomeFragment homeFragment;
    SangharshBooks sangharshBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.Theme_Dark);
                    ((SangharshBooks)getApplication()).setDarkMode(true);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    setTheme(R.style.Theme_Light);
                    ((SangharshBooks)getApplication()).setDarkMode(false);
                    break;
            }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sangharshBooks = (SangharshBooks) getApplication();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("sangharshBooks","Sangharsh Books", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        smoothBottomBar = findViewById(R.id.bottomBar);
        fab = findViewById(R.id.add_home_admin);

        if(savedInstanceState!=null){
            smoothBottomBar.setItemActiveIndex(3);

        }
        if(savedInstanceState==null){
            if(homeFragment==null) {
                homeFragment = new HomeFragment();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.body_holder_main_activity, homeFragment, "home");
                ft1.commit();
            }
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

                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDirectoryBottomSheetAdapter bottomSheet = new AddDirectoryBottomSheetAdapter(MainActivity.this,(SangharshBooks) getApplication(),MainActivity.this,homeFragment);
                bottomSheet.show(getSupportFragmentManager(), "addBottomSheet");
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.greetings_holder, new GreetingFragment(), "greetings");
        ft.commit();


//        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blur);
//        Bitmap newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//// create a canvas where we can draw on
//        Canvas canvas = new Canvas(newBitmap);
//// create a paint instance with alpha
//        Paint alphaPaint = new Paint();
//        alphaPaint.setAlpha(210);
//// now lets draw using alphaPaint instance
//        canvas.drawBitmap(originalBitmap, 0, 0, alphaPaint);
//
//        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), newBitmap);
//        final float roundPx = (float) originalBitmap.getWidth() * 0.06f;
//        roundedBitmapDrawable.setCornerRadius(roundPx);
//        smoothBottomBar.setBackground(roundedBitmapDrawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void update() {
        homeFragment.updateAdapterDataset();
    }


}