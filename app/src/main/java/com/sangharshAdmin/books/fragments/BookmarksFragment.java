package com.sangharshAdmin.books.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sangharshAdmin.books.R;
import com.sangharshAdmin.books.StorageHelper;
import com.sangharshAdmin.books.adapter.PDFAdapter;
import com.sangharshAdmin.books.model.PDFModel;

import java.util.ArrayList;

public class BookmarksFragment extends Fragment {

    RecyclerView recyclerView;
    PDFAdapter pdfAdapter;
    ArrayList<PDFModel> pdfModels;


    public BookmarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        recyclerView = view.findViewById(R.id.rv_bookmarks);
        //linearLayout = view.findViewById(R.id.linerlayout_download_frag);

        pdfModels = new StorageHelper(getActivity()).getArrayListOfPDFModel(StorageHelper.BOOKMARKS);
        pdfAdapter = new PDFAdapter(getActivity().getApplication(),getActivity(),pdfModels,"bookmarks");
        recyclerView.setAdapter(pdfAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                ((AppCompatActivity)getContext()).getSupportFragmentManager().findFragmentById();

                // in here you can do logic when backPress is clicked
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        pdfModels.clear();
        pdfModels.addAll(new StorageHelper(getContext()).getArrayListOfPDFModel(StorageHelper.BOOKMARKS));
        pdfAdapter.notifyDataSetChanged();
        Log.d("sba", "onResume: of bookfrag called");
        super.onResume();
    }
}