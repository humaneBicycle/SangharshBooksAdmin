package com.sangharshAdmin.book;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sangharshAdmin.book.adapter.DirectoryAdapter;
import com.sangharshAdmin.book.model.Directory;
import com.sangharshAdmin.book.model.ShortTest;
import com.sangharshAdmin.book.model.Test;

import java.util.ArrayList;

public class LongClickOptionsTest extends BottomSheetDialogFragment {
    ShortTest test;
    Context context;
    DirectoryAdapter adapter;
    int testPos;
    ArrayList<ShortTest> tests;
    SangharshBooks sangharshBooks;
    public LongClickOptionsTest(Context context, ShortTest test, DirectoryAdapter adapter,int testPos,ArrayList<ShortTest> tests,SangharshBooks sangharshBooks) {
        this.test = test;
        this.context=context;
        this.adapter = adapter;
        this.testPos=testPos;
        this.tests=tests;

        this.sangharshBooks=sangharshBooks;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.long_click_options,null);

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("directory").document(test.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore.getInstance().collection("directory").whereEqualTo("path",sangharshBooks.getPath()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(!task.getResult().isEmpty()){
                                            Log.i("sba ", "no. of directories at path: "+sangharshBooks.getPath()+" is"+task.getResult().getDocuments().size() +" or "+ task.getResult().toObjects(Directory.class).size());
                                            if(task.getResult().toObjects(Directory.class).size()==1){
                                                String id = task.getResult().getDocuments().get(0).getId();
                                                Directory directory = task.getResult().toObjects(Directory.class).get(0);
                                                directory.getTests().remove(testPos);
                                                FirebaseFirestore.getInstance().collection("directory").document(id).set(directory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                                            tests.remove(testPos);
                                                            adapter.notifyDataSetChanged();
                                                            dismiss();
                                                        }
                                                    }
                                                });

                                            }else {
                                                Toast.makeText(context, "Database Error!", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });




                        }else{
                            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        return v;
    }
}