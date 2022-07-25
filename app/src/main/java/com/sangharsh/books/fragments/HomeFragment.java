package com.sangharsh.books.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sangharsh.books.Callback;
import com.sangharsh.books.R;
import com.sangharsh.books.SangharshBooks;
import com.sangharsh.books.UIUpdateHomeFrag;
import com.sangharsh.books.adapter.DirectoryAdapter;
import com.sangharsh.books.model.Directory;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UIUpdateHomeFrag {

    RecyclerView recyclerView;
    Directory directory;
    ProgressBar progressBar;
    TextView nothingAvailableTV;
    ArrayList<Directory> directories;
    SangharshBooks sangharshBooks;
    DirectoryAdapter directoryAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sangharshBooks = (SangharshBooks) getActivity().getApplication();
        sangharshBooks.resetAddress();
        initUI(view);



        return view;
    }

    private void initUI(View v){
        recyclerView = v.findViewById(R.id.rv_home);
        progressBar = v.findViewById(R.id.progress_home_frag);
        nothingAvailableTV = v.findViewById(R.id.text_nothing_available_home_frag);

        //Log.d("sba path homefrag", "path: "+ sangharshBooks.path);
        FirebaseFirestore.getInstance().collection("directory").whereEqualTo("path",sangharshBooks.path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()){
                        directories = (ArrayList<Directory>)task.getResult().toObjects(Directory.class);
                        directory = task.getResult().toObjects(Directory.class).get(0);
                        //sangharshBooks.addStack("/home");
                        //sangharshBooks.addToPath("/home");
                        //sangharshBooks.addDirectoryToStack(directory);
                        directoryAdapter = new DirectoryAdapter(getContext(),directory,sangharshBooks,HomeFragment.this);
                        recyclerView.setAdapter(directoryAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        progressBar.setVisibility(View.GONE);
                    }else{
                        //first directory is being added
                        sangharshBooks.addStack("/");
                        nothingAvailableTV.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(getContext(), "Something went wrong. Please try again later!"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void update() {
        directoryAdapter.notifyDataSetChanged();
    }
}