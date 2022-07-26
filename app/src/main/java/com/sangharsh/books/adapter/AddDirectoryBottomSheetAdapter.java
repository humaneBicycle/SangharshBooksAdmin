package com.sangharsh.books.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sangharsh.books.Callback;
import com.sangharsh.books.FileActivity;
import com.sangharsh.books.R;
import com.sangharsh.books.SangharshBooks;
import com.sangharsh.books.model.Directory;
import com.sangharsh.books.model.FileModel;
import com.sangharsh.books.model.PDFModel;

import java.util.ArrayList;

public class AddDirectoryBottomSheetAdapter extends BottomSheetDialogFragment {
    Context context;
    AppCompatSpinner typeSpinner;
    EditText pdfName, fileName, pdfURL;
    AppCompatButton addButton;
    SangharshBooks sangharshBooks;
    TextView pathBottomSheet;
    Callback callback;
    Directory directory;

    public AddDirectoryBottomSheetAdapter(Context context, SangharshBooks sangharshBooks){
        this.context = context;
        this.sangharshBooks = sangharshBooks;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_bottom_sheet,container);

        //modeSpinner = v.findViewById(R.id.mode_selector_bottom_style_spinner);
        typeSpinner = v.findViewById(R.id.type_selector_bottom_style_spinner);
        pdfName = v.findViewById(R.id.pdf_name_edittext);
        pdfURL = v.findViewById(R.id.pdf_url_edittext);
        fileName = v.findViewById(R.id.file_name_edittext);
        addButton = v.findViewById(R.id.bottom_sheet_add_button);
        pathBottomSheet = v.findViewById(R.id.path_bottom_sheet);

//        String[] items = new String[]{"Parent Directory","Any Other Directory"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,items);
//        modeSpinner.setAdapter(adapter);

        String[] type = new String[]{"File","PDF"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,type);
        typeSpinner.setAdapter(adapter1);

        pathBottomSheet.setText(sangharshBooks.path);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    pdfName.setVisibility(View.GONE);
                    pdfURL.setVisibility(View.GONE);
                    fileName.setVisibility(View.VISIBLE);
                }else if(i==1){
                    pdfName.setVisibility(View.VISIBLE);
                    pdfURL.setVisibility(View.VISIBLE);
                    fileName.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typeSpinner.getSelectedItem().equals("File")){
                    //file hai bawa
                    if(!fileName.getText().toString().equals("") && fileName.getText()!=null){
                        //directory = sangharshBooks.getLastActiveDirectory();

//                        if(directory==null){
//                            directory=new Directory(0,new ArrayList<>(),new ArrayList<>(),sangharshBooks.path);
//                        }
                        //l.setDepth(1);
                        //directory.getFiles().add(fileModel);
                        //Log.d("sba", "path while adding "+sangharshBooks.getPath());
                        FirebaseFirestore.getInstance().collection("directory").whereEqualTo("path",sangharshBooks.getPath()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    if(!task.getResult().isEmpty()) {
                                        directory = task.getResult().toObjects(Directory.class).get(0);
                                        FileModel fileModel = new FileModel(fileName.getText().toString(),sangharshBooks.getPath()+"\\"+fileName.getText().toString());

                                        String id = task.getResult().getDocuments().get(0).getId();
                                        boolean b=false;
                                        for(int i =0;i<directory.getFiles().size();i++){
                                            if(fileName.getText().toString().equals(directory.getFiles().get(i).getName())){
                                                b=true;
                                                Log.d("sba bottomsheet", "filename "+fileName.getText().toString()+" directory "+ directory.getFiles().get(i).getName());
                                            }
                                        }
                                        directory.getFiles().add(fileModel);
                                        //Log.d("sba bottomsheet", "onComplete: checked for falseness"+ b);
                                        if(b){
                                            Toast.makeText(context, "This name already exists!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            FirebaseFirestore.getInstance().collection("directory").document(id).set(directory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(context, "Successfully added file!", Toast.LENGTH_SHORT).show();
                                                        if(callback!=null) {
                                                            callback.Callback();
                                                        }
                                                        dismiss();
                                                    } else {
                                                        Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                                        Log.d("sba", "onComplete: " + task.getException());
                                                    }
                                                }
                                            });
                                        }
                                    }else{
                                        ArrayList<FileModel> fileModels = new ArrayList<>();
                                        FileModel fileModel = new FileModel(fileName.getText().toString(),sangharshBooks.getPath()+"\\"+fileName.getText().toString());
                                        fileModels.add(fileModel);
                                        directory = new Directory(0,fileModels,new ArrayList<>(),sangharshBooks.getPath());
                                        //directory.getFiles().add(fileModel);
                                        FirebaseFirestore.getInstance().collection("directory").document().set(directory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context, "Successfully added file!", Toast.LENGTH_SHORT).show();
                                                    //callback.Callback();
                                                    //Log.d("sba", "onComplete: noooooo");
                                                    dismiss();
                                                } else {
                                                    Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                                    Log.d("sba", "onComplete: " + task.getException());
                                                }
                                            }
                                        });

                                    }


                                }
                            }
                        });


                    }


                }else if (typeSpinner.getSelectedItem().equals("PDF")){
                    //pdf hai bawa
                    if(!pdfName.getText().toString().isEmpty() && !pdfURL.getText().toString().isEmpty()){
                        FirebaseFirestore.getInstance().collection("directory").whereEqualTo("path",sangharshBooks.getPath()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    if(!task.getResult().isEmpty()){
                                        //task.getResult().getDocuments().get(0).getId();
                                        directory = task.getResult().toObjects(Directory.class).get(0);
                                        String id = task.getResult().getDocuments().get(0).getId();
                                        boolean b = false;
                                        PDFModel pdfModel = new PDFModel(false,pdfURL.getText().toString(),"",pdfName.getText().toString(),sangharshBooks.getPath()+"\\"+pdfName.getText().toString(),false);
                                        for(int i = 0; i<directory.getPdfModels().size();i++){
                                            if(directory.getPdfModels().get(i).getName().equals(pdfName.getText().toString())){
                                                b=true;
                                                break;
                                            }
                                        }
                                        directory.getPdfModels().add(pdfModel);
                                        if(b){
                                            Toast.makeText(context, "PDF Already exists!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            FirebaseFirestore.getInstance().collection("directory").document(id).set(directory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(context, "PDF added successfully!", Toast.LENGTH_SHORT).show();
                                                        if(callback!=null){
                                                            callback.Callback();
                                                        }else{
                                                            //TODO at fileactivity. update UI

                                                        }
                                                    }else{
                                                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }else {
                                        Log.d("aba addbottomsheet", "onComplete: pdf task is empty");
                                        FirebaseFirestore.getInstance().collection("directory").document().set(directory).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context, "Successfully added file!", Toast.LENGTH_SHORT).show();
                                                    callback.Callback();
                                                    //Log.d("sba", "onComplete: noooooo");
                                                    dismiss();
                                                } else {
                                                    Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                                    Log.d("sba", "onComplete: " + task.getException());
                                                }
                                            }
                                        });
                                    }
                                }else{
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(context, "Please complete the form!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }
    public void attackCallback(Callback callback){
        this.callback = callback;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
