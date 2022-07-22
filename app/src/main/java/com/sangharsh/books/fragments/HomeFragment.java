package com.sangharsh.books.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.sangharsh.books.R;
import com.sangharsh.books.SangharshBooks;
import com.sangharsh.books.adapter.DirectoryAdapter;
import com.sangharsh.books.model.Directory;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    Directory directory;
    ProgressBar progressBar;
    TextView nothingAvailableTV;
    SangharshBooks sangharshBooks;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);



        return view;
    }

    private void initUI(View v){
        recyclerView = v.findViewById(R.id.rv_home);
        progressBar = v.findViewById(R.id.progress_home_frag);
        nothingAvailableTV = v.findViewById(R.id.text_nothing_available_home_frag);
        sangharshBooks = (SangharshBooks) getActivity().getApplication();

        FirebaseFirestore.getInstance().collection("directory").whereEqualTo("depth",0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()){
                        directory = task.getResult().toObjects(Directory.class).get(0);

                        sangharshBooks.addStack("/");
                        //sangharshBooks.addToPath("/");
                        DirectoryAdapter directoryAdapter = new DirectoryAdapter(getContext(),directory,sangharshBooks);
                        recyclerView.setAdapter(directoryAdapter);
                        progressBar.setVisibility(View.GONE);
                    }else{
                        sangharshBooks.addStack("/");
                        nothingAvailableTV.setVisibility(View.VISIBLE);
                    }

                }else{
                    Toast.makeText(getContext(), "Something went wrong. Please try again later!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}