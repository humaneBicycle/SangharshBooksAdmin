package com.sangharshAdmin.book.fragments;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sangharshAdmin.book.DirectoryChangeListener;
import com.sangharshAdmin.book.R;
import com.sangharshAdmin.book.SangharshBooks;
import com.sangharshAdmin.book.UIUpdateHomeFrag;
import com.sangharshAdmin.book.adapter.DirectoryAdapter;
import com.sangharshAdmin.book.model.Directory;
import com.sangharshAdmin.book.model.FileModel;
import com.sangharshAdmin.book.model.PDFModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UIUpdateHomeFrag, DirectoryChangeListener {

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
        Log.d("sba", "update: file added listened");
//        directoryAdapter.notifyDataSetChanged();
        //updateAdapterDataset();
    }

    @Override
    public void onFileModelAdded(FileModel fileModel) {
        if(directory!=null && directoryAdapter!=null) {
            //Log.d("sba file creation callback", "onFileModelAdded: not null");
            directory.getFiles().add(fileModel);
            directoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPDFModelAdded(PDFModel pdfModel) {
        if(directory!=null && directoryAdapter!=null) {
            directory.getPdfModels().add(pdfModel);
            directoryAdapter.notifyDataSetChanged();
        }
    }

    public void updateAdapterDataset(){
        Log.d("sba", "update: file added listened");
        directoryAdapter.notifyDataSetChanged();
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_holder_main_activity,new HomeFragment());
        if((directory.getPdfModels().size()+directory.getFiles().size())==0){
            nothingAvailableTV.setVisibility(View.VISIBLE);
        }
        if((directory.getPdfModels().size()+directory.getFiles().size())==1){
            nothingAvailableTV.setVisibility(View.GONE);
        }

    }
}