package com.sangharsh.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sangharsh.books.adapter.DirectoryAdapter;
import com.sangharsh.books.model.Directory;

public class FileActivity extends AppCompatActivity {
    Directory directory;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        progressBar = findViewById(R.id.progress_file_activity);

        String firebaseDocumentID = getIntent().getStringExtra("id");

        FirebaseFirestore.getInstance().collection("directory").whereEqualTo("uid",firebaseDocumentID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()){
                        directory = task.getResult().toObjects(Directory.class).get(0);
                        recyclerView = findViewById(R.id.rv_file_activity);
                        DirectoryAdapter directoryAdapter = new DirectoryAdapter(FileActivity.this,directory,(SangharshBooks) getApplication());
                        recyclerView.setAdapter(directoryAdapter);
                        progressBar.setVisibility(View.GONE);
                    }else{
                        //task empty
                    }
                }
            }
        });



    }
}