package com.sangharsh.books.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sangharsh.books.R;
import com.sangharsh.books.SangharshBooks;

import java.util.ArrayList;


public class AddDirectoryBottomSheetAdapter extends BottomSheetDialogFragment {
    Context context;
    AppCompatSpinner typeSpinner;
    EditText pdfName, fileName, pdfURL;
    AppCompatButton addButton;
    SangharshBooks sangharshBooks;

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

//        String[] items = new String[]{"Parent Directory","Any Other Directory"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,items);
//        modeSpinner.setAdapter(adapter);

        String[] type = new String[]{"File","PDF"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,type);
        typeSpinner.setAdapter(adapter1);

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


                }else if (typeSpinner.getSelectedItem().equals("PDF")){
                    //pdf hai bawa

                }
            }
        });

        return v;
    }
}
