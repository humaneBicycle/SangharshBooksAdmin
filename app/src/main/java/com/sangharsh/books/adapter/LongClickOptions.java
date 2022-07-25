package com.sangharsh.books.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sangharsh.books.PDFDisplay;
import com.sangharsh.books.R;
import com.sangharsh.books.SangharshBooks;
import com.sangharsh.books.StorageHelper;
import com.sangharsh.books.UIUpdateHomeFrag;
import com.sangharsh.books.model.Directory;
import com.sangharsh.books.model.FileModel;
import com.sangharsh.books.model.PDFModel;

import java.util.ArrayList;

public class LongClickOptions extends BottomSheetDialogFragment {
    SangharshBooks sangharshBooks;
    Context context;
    AppCompatButton deleteButton, bookmarkButton;
    FileModel fileModel;
    PDFModel pdfModel;
    boolean b;
    ArrayList<PDFModel> bookmarkedPdfs;
    int j;
    UIUpdateHomeFrag uiUpdateHomeFrag;


    public LongClickOptions(SangharshBooks sangharshBooks, Context context,FileModel fileModel,UIUpdateHomeFrag uiUpdateHomeFrag){
        this.sangharshBooks = sangharshBooks;
        this.context = context;
        this.fileModel = fileModel;
        this.uiUpdateHomeFrag = uiUpdateHomeFrag;
    }
    public LongClickOptions(SangharshBooks sangharshBooks, Context context, PDFModel pdfModel,UIUpdateHomeFrag uiUpdateHomeFrag){
        this.sangharshBooks = sangharshBooks;
        this.context = context;
        this.pdfModel = pdfModel;
        this.uiUpdateHomeFrag = uiUpdateHomeFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.long_click_options,container);

        deleteButton = v.findViewById(R.id.delete);

        if(getTag().equals("longClickOptionsFile")) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("sba long click", "onCreateView: "+ getTag());
                    deleteFile(sangharshBooks.getPath()+"\\"+fileModel.getName());

                }
            });
        }

        if(getTag().equals("longClickOptionsPDF")){

            bookmarkButton = v.findViewById(R.id.bookmark_long_click_options);
            bookmarkButton.setVisibility(View.VISIBLE);
            bookmarkedPdfs =  new StorageHelper(context).getArrayListOfPDFModel(StorageHelper.BOOKMARKS);
            b=false;
            for(int i =0;i<bookmarkedPdfs.size();i++){
                if(bookmarkedPdfs.get(i).getPointingDir().equals(pdfModel.getPointingDir())){
                    b=true;
                    j=i;
                    break;
                }
            }
            if(b){
                //this pdf is a bookmark
                bookmarkButton.setText("Remove from Bookmark");
            }

            bookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(b){
                        //pdf is a bookmark
                        bookmarkedPdfs.remove(j);
                        new StorageHelper(context).savePDFModel(bookmarkedPdfs,StorageHelper.BOOKMARKS);
                        bookmarkButton.setText(R.string.bookmark);
                    }else{
                        //not add
                        bookmarkedPdfs.add(pdfModel);
                        new StorageHelper(context).savePDFModel(bookmarkedPdfs,StorageHelper.BOOKMARKS);
                        bookmarkButton.setText("Remove from Bookmark");
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("sba long click", "onCreateView: "+ getTag());

                        deletePDF();


                }
            });
        }



        return v;
    }

    private void deleteFile(String pathOfFile) {
        FirebaseFirestore.getInstance().collection("directory").whereEqualTo("path",pathOfFile).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });

    }
    private void deletePDF() {
        FirebaseFirestore.getInstance().collection("directory").whereEqualTo("path",sangharshBooks.getPath()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Directory directory = task.getResult().toObjects(Directory.class).get(0);
                    for(int i =0;i<directory.getPdfModels().size();i++){
                        if(directory.getPdfModels().get(i).getPointingDir().equals(pdfModel.getPointingDir())){
                            ArrayList<PDFModel> bookmarks = new StorageHelper(context).getArrayListOfPDFModel(StorageHelper.BOOKMARKS);
                            for (int k=0;k<bookmarks.size();k++){
                                if(directory.getPdfModels().get(i).getPointingDir().equals(bookmarks.get(k).getPointingDir())){
                                    bookmarks.remove(k);
                                    new StorageHelper(context).savePDFModel(bookmarks,StorageHelper.BOOKMARKS);
                                }
                            }
                            ArrayList<PDFModel> downloads = new StorageHelper(context).getArrayListOfPDFModel(StorageHelper.DOWNLOADED);
                            for (int k=0;k<downloads.size();k++){
                                if(directory.getPdfModels().get(i).getPointingDir().equals(downloads.get(k).getPointingDir())){
                                    downloads.remove(k);
                                    new StorageHelper(context).savePDFModel(downloads,StorageHelper.DOWNLOADED);
                                }
                            }

                            directory.getPdfModels().remove(i);
                            FirebaseFirestore.getInstance().collection("directory").document(task.getResult().getDocuments().get(0).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        FirebaseFirestore.getInstance().collection("directory").document().set(directory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();

                                                    uiUpdateHomeFrag.update();
                                                    dismiss();

                                                }else{
                                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        break;
                        }
                    }


                }else{
                    Toast.makeText(context , "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
