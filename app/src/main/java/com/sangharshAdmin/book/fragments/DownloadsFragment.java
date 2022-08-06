package com.sangharshAdmin.book.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sangharshAdmin.book.R;
import com.sangharshAdmin.book.StorageHelper;
import com.sangharshAdmin.book.adapter.PDFAdapter;
import com.sangharshAdmin.book.model.PDFModel;

import java.util.ArrayList;

public class DownloadsFragment extends Fragment {

    RecyclerView recyclerView;
    //LinearLayout linearLayout;

    public DownloadsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);

        recyclerView = view.findViewById(R.id.rv_downloads);
        //linearLayout = view.findViewById(R.id.linerlayout_download_frag);
        ArrayList<PDFModel> pdfModels = new StorageHelper(getActivity()).getArrayListOfPDFModel(StorageHelper.DOWNLOADED);
        PDFAdapter pdfAdapter = new PDFAdapter(getActivity().getApplication(),getActivity(),pdfModels,"downloads");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(pdfAdapter);

        return view;
    }
}